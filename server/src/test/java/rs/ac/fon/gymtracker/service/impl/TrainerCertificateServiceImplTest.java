package rs.ac.fon.gymtracker.service.impl;

import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerCertificateServiceImplTest {

    @Mock
    private TrainerCertificateRepository repo;

    @Mock
    private TrainerRepository trainerRepo;

    @Mock
    private CertificateRepository certificateRepo;

    @InjectMocks
    private TrainerCertificateServiceImpl service;

    private Trainer trainer;
    private Certificate certificate;
    private TrainerCertificateId id;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("Marko");
        trainer.setLastName("Marković");
        trainer.setUsername("marko123");

        certificate = new Certificate();
        certificate.setId(10L);
        certificate.setName("ISSA CPT");
        certificate.setType("Strength");

        id = new TrainerCertificateId(1L, 10L);
    }

    @Test
    void testAssignCreatesNew() {
        var issuedAt = LocalDate.of(2020, 5, 10);

        when(trainerRepo.findById(1L)).thenReturn(Optional.of(trainer));
        when(certificateRepo.findById(10L)).thenReturn(Optional.of(certificate));
        when(repo.existsById(id)).thenReturn(false);
        when(repo.save(any(TrainerCertificate.class))).thenAnswer(inv -> inv.getArgument(0));

        var out = service.assign(1L, 10L, issuedAt);

        assertEquals(id, out.getId());
        assertEquals(trainer, out.getTrainer());
        assertEquals(certificate, out.getCertificate());
        assertEquals(issuedAt, out.getIssuedAt());

        verify(trainerRepo).findById(1L);
        verify(certificateRepo).findById(10L);
        verify(repo).existsById(id);
        verify(repo).save(any(TrainerCertificate.class));
    }

    @Test
    void testAssignUpdatesExisting() {
        var issuedAt = LocalDate.of(2021, 3, 15);
        var existing = new TrainerCertificate();
        existing.setId(id);
        existing.setTrainer(trainer);
        existing.setCertificate(certificate);
        existing.setIssuedAt(LocalDate.of(2019, 1, 1));

        when(trainerRepo.findById(1L)).thenReturn(Optional.of(trainer));
        when(certificateRepo.findById(10L)).thenReturn(Optional.of(certificate));
        when(repo.existsById(id)).thenReturn(true);
        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        var out = service.assign(1L, 10L, issuedAt);

        assertEquals(issuedAt, out.getIssuedAt());
        assertSame(existing, out);

        verify(repo).existsById(id);
        verify(repo).findById(id);
        verify(repo).save(existing);
    }

    @Test
    void testRevoke() {
        service.revoke(1L, 10L);
        verify(repo).deleteById(new TrainerCertificateId(1L, 10L));
    }

    @Test
    void testListForTrainer() {
        var list = List.of(new TrainerCertificate(), new TrainerCertificate());
        when(repo.findByTrainerId(1L)).thenReturn(list);

        var out = service.listForTrainer(1L);

        assertEquals(2, out.size());
        verify(repo).findByTrainerId(1L);
    }

    @Test
    void testExistsTrue() {
        when(repo.existsById(id)).thenReturn(true);
        assertTrue(service.exists(id));
        verify(repo).existsById(id);
    }

    @Test
    void testExistsFalse() {
        when(repo.existsById(id)).thenReturn(false);
        assertFalse(service.exists(id));
        verify(repo).existsById(id);
    }
}