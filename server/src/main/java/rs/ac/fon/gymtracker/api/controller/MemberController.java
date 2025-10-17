package rs.ac.fon.gymtracker.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.fon.gymtracker.api.dto.MemberDto;
import rs.ac.fon.gymtracker.api.mapper.MemberMapper;
import rs.ac.fon.gymtracker.service.MemberService;
import rs.ac.fon.gymtracker.service.ServicePackageService;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService service;
    private final ServicePackageService packageService;

    @GetMapping
    public List<MemberDto> list() {
        return service.findAll().stream().map(MemberMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> get(@PathVariable Long id) {
        return service.findById(id).map(MemberMapper::toDto)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MemberDto> create(@Valid @RequestBody MemberDto body) {
        var pkg = body.packageId() != null
                ? packageService.findById(body.packageId()).orElse(null)
                : null;

        var saved = service.create(MemberMapper.toEntity(body, pkg));
        return ResponseEntity.created(URI.create("/api/members/" + saved.getId()))
                .body(MemberMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> update(@PathVariable Long id, @Valid @RequestBody MemberDto body) {
        var pkg = body.packageId() != null
                ? packageService.findById(body.packageId()).orElse(null)
                : null;

        var updated = service.update(id, MemberMapper.toEntity(body, pkg));
        return ResponseEntity.ok(MemberMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<MemberDto> search(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "packageId", required = false) Long packageId
    ) {
        var list = service.search(q, packageId);
        return list.stream().map(MemberMapper::toDto).toList();
    }


}
