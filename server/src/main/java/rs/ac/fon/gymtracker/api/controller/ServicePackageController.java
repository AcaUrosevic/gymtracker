package rs.ac.fon.gymtracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.fon.gymtracker.api.dto.ServicePackageDto;
import rs.ac.fon.gymtracker.api.mapper.ServicePackageMapper;
import rs.ac.fon.gymtracker.service.ServicePackageService;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@Validated
public class ServicePackageController {

    private final ServicePackageService service;

    @GetMapping
    public List<ServicePackageDto> list() {
        return service.findAll().stream().map(ServicePackageMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePackageDto> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ServicePackageMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServicePackageDto> create(@Valid @RequestBody ServicePackageDto body) {
        var saved = service.create(ServicePackageMapper.toEntity(body));
        return ResponseEntity.created(URI.create("/api/packages/" + saved.getId()))
                .body(ServicePackageMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePackageDto> update(@PathVariable Long id,
                                                    @Valid @RequestBody ServicePackageDto body) {
        var updated = service.update(id, ServicePackageMapper.toEntity(body));
        return ResponseEntity.ok(ServicePackageMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
