package rs.ac.fon.gymtracker.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rs.ac.fon.gymtracker.domain.Certificate;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.domain.TrainerCertificate;
import rs.ac.fon.gymtracker.domain.id.TrainerCertificateId;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainerCertificateRepositoryTest {

    @Autowired TrainerRepository trainerRepository;
    @Autowired CertificateRepository certificateRepository;
    @Autowired TrainerCertificateRepository trainerCertificateRepository;

    @Test
    void save_and_find_by_composite_id() {
        var t = new Trainer();
        t.setFirstName("Test");
        t.setLastName("Trener");
        t.setUsername("tt_user");
        trainerRepository.save(t);

        var c = new Certificate();
        c.setName("Test Cert");
        c.setType("PT");
        certificateRepository.save(c);

        var tc = new TrainerCertificate();
        tc.setTrainer(t);
        tc.setCertificate(c);
        tc.setIssuedAt(LocalDate.now());
        tc.setId(new TrainerCertificateId(t.getId(), c.getId()));

        trainerCertificateRepository.save(tc);

        var found = trainerCertificateRepository.findById(new TrainerCertificateId(t.getId(), c.getId()));
        assertTrue(found.isPresent());
        assertEquals("tt_user", found.get().getTrainer().getUsername());
        assertEquals("Test Cert", found.get().getCertificate().getName());
    }
}
