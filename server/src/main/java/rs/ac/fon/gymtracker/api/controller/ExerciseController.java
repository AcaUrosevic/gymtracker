package rs.ac.fon.gymtracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.fon.gymtracker.api.dto.ExerciseDto;
import rs.ac.fon.gymtracker.api.mapper.ExerciseMapper;
import rs.ac.fon.gymtracker.service.ExerciseService;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService service;

    @GetMapping
    public List<ExerciseDto> list() {
        return service.findAll().stream().map(ExerciseMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDto> get(@PathVariable Long id) {
        return service.findById(id).map(ExerciseMapper::toDto)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExerciseDto> create(@Valid @RequestBody ExerciseDto body) {
        var saved = service.create(ExerciseMapper.toEntity(body));
        return ResponseEntity.created(URI.create("/api/exercises/" + saved.getId()))
                .body(ExerciseMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDto> update(@PathVariable Long id, @Valid @RequestBody ExerciseDto body) {
        var updated = service.update(id, ExerciseMapper.toEntity(body));
        return ResponseEntity.ok(ExerciseMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
