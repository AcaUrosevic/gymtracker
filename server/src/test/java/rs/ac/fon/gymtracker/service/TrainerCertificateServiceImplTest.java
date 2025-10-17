package rs.ac.fon.gymtracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.Certificate;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.domain.TrainerCertificate;
import rs.ac.fon.gymtracker.domain.id.TrainerCertificateId;
import rs.ac.fon.gymtracker.repository.CertificateRepository;
import rs.ac.fon.gymtracker.repository.TrainerCertificateRepository;
import rs.ac.fon.gymtracker.repository.TrainerRepository;
import rs.ac.fon.gymtracker.service.impl.TrainerCertificateServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerCertificateServiceImplTest {

    @Mock TrainerCertificateRepository repo;
    @Mock TrainerRepository trainerRepo;
    @Mock CertificateRepository certificateRepo;

    @InjectMocks TrainerCertificateServiceImpl service;

    @Test
    void grantCertificate_creates_or_updates_link_with_date() {
        var t = new Trainer(); t.setId(11L);
        var c = new Certificate(); c.setId(22L);
        when(trainerRepo.findById(11L)).thenReturn(Optional.of(t));
        when(certificateRepo.findById(22L)).thenReturn(Optional.of(c));
        when(repo.save(any(TrainerCertificate.class))).thenAnswer(inv -> inv.getArgument(0));

        var issued = LocalDate.of(2024, 3, 5);
        var tc = service.assign(11L, 22L, issued);

        assertEquals(new TrainerCertificateId(11L, 22L), tc.getId());
        assertSame(t, tc.getTrainer());
        assertSame(c, tc.getCertificate());
        assertEquals(issued, tc.getIssuedAt());

        verify(repo).save(any(TrainerCertificate.class));
    }

    @Test
    void revokeCertificate_deletes_link() {
        var id = new TrainerCertificateId(11L, 22L);

        service.revoke(11L, 22L);

        verify(repo).deleteById(id);
    }

    @Test
    void listByTrainer_returns_results() {
        when(repo.findByTrainerId(11L)).thenReturn(List.of(new TrainerCertificate()));

        var list = service.listForTrainer(11L);

        assertEquals(1, list.size());
        verify(repo).findByTrainerId(11L);
    }
}
