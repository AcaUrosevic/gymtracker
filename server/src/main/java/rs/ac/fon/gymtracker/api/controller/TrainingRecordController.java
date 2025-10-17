package rs.ac.fon.gymtracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.fon.gymtracker.api.dto.TrainingRecordDto;
import rs.ac.fon.gymtracker.api.dto.TrainingRecordItemDto;
import rs.ac.fon.gymtracker.api.mapper.TrainingRecordMapper;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;
import rs.ac.fon.gymtracker.service.TrainingRecordService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/training-records")
@RequiredArgsConstructor
public class TrainingRecordController {

    private final TrainingRecordService service;

    public record CreateRecordRequest(@NotNull LocalDate date, @NotNull Long trainerId, @NotNull Long memberId) {}
    public record AddItemRequest(@NotNull Long exerciseId, int sets, int reps, double weight) {}

    @GetMapping
    public List<TrainingRecordDto> listAll() {
        return service.findAll().stream()
                .map(TrainingRecordMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingRecordDto> get(@PathVariable Long id) {
        return service.findById(id)
                .map(TrainingRecordMapper::toDto)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    public List<TrainingRecordDto> byMember(@PathVariable Long memberId) {
        return service.listByMember(memberId).stream().map(TrainingRecordMapper::toDto).toList();
    }

    @GetMapping("/trainer/{trainerId}")
    public List<TrainingRecordDto> byTrainer(@PathVariable Long trainerId) {
        return service.listByTrainer(trainerId).stream().map(TrainingRecordMapper::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<TrainingRecordDto> create(@Valid @RequestBody CreateRecordRequest req) {
        var saved = service.createRecord(req.date(), req.trainerId(), req.memberId());
        return ResponseEntity.created(URI.create("/api/training-records/" + saved.getId()))
                .body(TrainingRecordMapper.toDto(saved));
    }

    @PostMapping("/{recordId}/items")
    public ResponseEntity<TrainingRecordItemDto> addItem(@PathVariable Long recordId,
                                                         @Valid @RequestBody AddItemRequest req) {
        var item = service.addItem(recordId, req.exerciseId(), req.sets(), req.reps(), req.weight());
        return ResponseEntity.created(URI.create("/api/training-records/" + recordId + "/items/" + item.getId().getRb()))
                .body(TrainingRecordMapper.toItemDto(item));
    }

    @PostMapping("/{recordId}/recalc-intensity")
    public ResponseEntity<TrainingRecordDto> recalc(@PathVariable Long recordId) {
        var updated = service.recalcIntensity(recordId);
        return ResponseEntity.ok(TrainingRecordMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{recordId}/items/{rb}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long recordId, @PathVariable Integer rb) {
        service.deleteItem(new TrainingRecordItemId(recordId, rb));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<TrainingRecordDto> search(
            @RequestParam(required = false) Long trainerId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long exerciseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return service.search(trainerId, memberId, exerciseId, from, to)
                .stream().map(TrainingRecordMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingRecordDto> update(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateRecordRequest req) {
        var updated = service.updateRecord(
                id, req.date(), req.trainerId(), req.memberId(),
                req.items().stream()
                        .map(i -> new TrainingRecordService.ItemInput(i.exerciseId(), i.sets(), i.reps(), i.weight()))
                        .toList()
        );
        return ResponseEntity.ok(TrainingRecordMapper.toDto(updated));
    }

    public record UpdateRecordRequest(
            @NotNull LocalDate date,
            @NotNull Long trainerId,
            @NotNull Long memberId,
            @NotNull List<UpdateItem> items
    ) {
        public record UpdateItem(@NotNull Long exerciseId, int sets, int reps, double weight) {}
    }
}
