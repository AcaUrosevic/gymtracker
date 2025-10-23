package rs.ac.fon.gymtracker.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.Certificate;
import rs.ac.fon.gymtracker.repository.CertificateRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateRepository repo;

    @InjectMocks
    private CertificateServiceImpl service;

    private Certificate base;

    @BeforeEach
    void setUp() {
        base = new Certificate();
        base.setId(null);
        base.setName("ISSA CPT");
        base.setType("Strength");
    }

    @AfterEach
    void tearDown() {
        base = null;
    }

    private static Certificate c(Long id, String name, String type) {
        var x = new Certificate();
        x.setId(id);
        x.setName(name);
        x.setType(type);
        return x;
    }

    @Test
    void testCreate() {
        var toSave = base;
        var saved  = c(10L, "ISSA CPT","Strength");

        when(repo.save(toSave)).thenReturn(saved);

        var out = service.create(toSave);

        assertEquals(10L, out.getId());
        assertEquals("ISSA CPT", out.getName());
        assertEquals("Strength", out.getType());

        verify(repo).save(toSave);
    }

    @Test
    void testFindByIdFound() {
        when(repo.findById(5L)).thenReturn(Optional.of(c(5L, "ACE CPT", "PT")));

        var out = service.findById(5L);

        assertTrue(out.isPresent());
        assertEquals(5L, out.get().getId());
        assertEquals("ACE CPT", out.get().getName());

        verify(repo).findById(5L);
    }

    @Test
    void testFindByIdMissing() {
        when(repo.findById(77L)).thenReturn(Optional.empty());

        var out = service.findById(77L);

        assertTrue(out.isEmpty());

        verify(repo).findById(77L);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(
                c(1L, "ISSA CPT", "Strength"),
                c(2L, "USAW Lv1", "WL")
        ));

        var out = service.findAll();

        assertEquals(2, out.size());
        assertEquals("USAW Lv1", out.get(1).getName());

        verify(repo).findAll();
    }

    @Test
    void testUpdateMergesNonNullFieldsOnly() {
        var current = c(3L, "ACE CPT", "PT");

        when(repo.findById(3L)).thenReturn(Optional.of(current));
        when(repo.save(any(Certificate.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Certificate();
        patch.setType("Strength");

        var updated = service.update(3L, patch);

        assertEquals(3L, updated.getId());
        assertEquals("ACE CPT", updated.getName());   // ostalo staro
        assertEquals("Strength", updated.getType());  // promenjeno

        verify(repo).findById(3L);
        verify(repo).save(current);
    }

    @Test
    void testUpdateWhenMissingThrows() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        var patch = new Certificate();
        patch.setName("X");

        assertThrows(NoSuchElementException.class, () -> service.update(99L, patch));

        verify(repo).findById(99L);
    }

    @Test
    void testDeleteById() {
        service.deleteById(44L);
        verify(repo).deleteById(44L);
    }
}
