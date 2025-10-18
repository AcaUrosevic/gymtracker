package rs.ac.fon.gymtracker.api.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.fon.gymtracker.api.dto.TrainerCertificateDto;
import rs.ac.fon.gymtracker.api.dto.TrainerDto;
import rs.ac.fon.gymtracker.api.dto.TrainerWithCertsDto;
import rs.ac.fon.gymtracker.api.mapper.TrainerCertificateMapper;
import rs.ac.fon.gymtracker.api.mapper.TrainerMapper;
import rs.ac.fon.gymtracker.infrastructure.security.JwtUtil;
import rs.ac.fon.gymtracker.service.TrainerCertificateService;
import rs.ac.fon.gymtracker.service.TrainerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService service;
    private final JwtUtil jwtUtil;
    private final TrainerCertificateService trainerCertificateService;

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    @GetMapping
    public List<TrainerDto> list() {
        return service.findAll().stream().map(TrainerMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainerDto> get(@PathVariable Long id) {
        return service.findById(id).map(TrainerMapper::toDto)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/with-certificates")
    public List<TrainerWithCertsDto> listWithCertificates() {
        var trainers = service.findAll();
        return trainers.stream()
                .map(t -> TrainerMapper.toWithCertsDto(t, trainerCertificateService.listForTrainer(t.getId())))
                .toList();
    }

    @GetMapping("/{id}/certificates")
    public List<TrainerCertificateDto> byTrainer(@PathVariable Long id) {
        return trainerCertificateService.listForTrainer(id).stream()
                .map(TrainerCertificateMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TrainerDto> create(@Valid @RequestBody TrainerDto body) {
        var saved = service.create(TrainerMapper.toEntity(body));
        return ResponseEntity.created(URI.create("/api/trainers/" + saved.getId()))
                .body(TrainerMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainerDto> update(@PathVariable Long id, @Valid @RequestBody TrainerDto body) {
        var updated = service.update(id, TrainerMapper.toEntity(body));
        return ResponseEntity.ok(TrainerMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest req) {
        var t = service.login(req.username(), req.password());
        var userDto = TrainerMapper.toDto(t);

        var token = jwtUtil.generate(
                t.getUsername(),
                Map.of("uid", t.getId(), "fn", t.getFirstName(), "ln", t.getLastName())
        );

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", userDto
        ));
    }

    public record AssignRequest(@NotNull Long certificateId, @PastOrPresent LocalDate issuedAt) {}

    @PostMapping("/{id}/certificates")
    public ResponseEntity<TrainerCertificateDto> assign(@PathVariable Long id,
                                                        @Valid @RequestBody AssignRequest req) {
        try {
            var saved = trainerCertificateService.assign(id, req.certificateId(), req.issuedAt());
            return ResponseEntity.created(URI.create("/api/trainers/" + id + "/certificates/" + req.certificateId()))
                    .body(TrainerCertificateMapper.toDto(saved));
        } catch (IllegalStateException dup) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, dup.getMessage());
        }
    }

    @DeleteMapping("/{id}/certificates/{certificateId}")
    public ResponseEntity<Void> revoke(@PathVariable Long id, @PathVariable Long certificateId) {
        trainerCertificateService.revoke(id, certificateId);
        return ResponseEntity.noContent().build();
    }
}
