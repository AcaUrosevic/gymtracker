package rs.ac.fon.gymtracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.Certificate;
import rs.ac.fon.gymtracker.repository.CertificateRepository;
import rs.ac.fon.gymtracker.service.impl.CertificateServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @Mock CertificateRepository repo;
    @InjectMocks CertificateServiceImpl service;

    @Test
    void update_merges_non_null_fields_only() {
        var current = new Certificate();
        current.setName("ACE CPT");
        current.setType("PT");

        when(repo.findById(3L)).thenReturn(Optional.of(current));
        when(repo.save(any(Certificate.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Certificate();
        patch.setType("Strength");

        var updated = service.update(3L, patch);

        assertEquals("ACE CPT", updated.getName());
        assertEquals("Strength", updated.getType());

        verify(repo).save(any(Certificate.class));
    }
}
