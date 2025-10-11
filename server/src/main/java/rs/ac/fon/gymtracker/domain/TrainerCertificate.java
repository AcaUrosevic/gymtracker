package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import rs.ac.fon.gymtracker.domain.id.TrainerCertificateId;

import java.time.LocalDate;

/** Veza trenera i sertifikata + datum izdavanja. PK je (trainer_id, certificate_id). */
@Getter @Setter
@Entity
@Table(
        name = "trainer_certificate",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_trainer_certificate", columnNames = {"trainer_id","certificate_id"}
        )
)
public class TrainerCertificate {

    /** Složeni PK: (trainer_id, certificate_id). */
    @EmbeddedId
    private TrainerCertificateId id;

    /** Mapiramo deo ključa 'trainer_id' kao FK na Trainer. */
    @MapsId("trainerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    /** Mapiramo deo ključa 'certificate_id' kao FK na Certificate. */
    @MapsId("certificateId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    /** Datum izdavanja (do danas). */
    @PastOrPresent
    private LocalDate issuedAt;
}
