package rs.ac.fon.gymtracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.fon.gymtracker.api.dto.CertificateDto;
import rs.ac.fon.gymtracker.api.mapper.CertificateMapper;
import rs.ac.fon.gymtracker.service.CertificateService;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService service;

    @GetMapping
    public List<CertificateDto> list() {
        return service.findAll().stream().map(CertificateMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> get(@PathVariable Long id) {
        return service.findById(id).map(CertificateMapper::toDto)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CertificateDto> create(@Valid @RequestBody CertificateDto body) {
        var saved = service.create(CertificateMapper.toEntity(body));
        return ResponseEntity.created(URI.create("/api/certificates/" + saved.getId()))
                .body(CertificateMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateDto> update(@PathVariable Long id, @Valid @RequestBody CertificateDto body) {
        var updated = service.update(id, CertificateMapper.toEntity(body));
        return ResponseEntity.ok(CertificateMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
