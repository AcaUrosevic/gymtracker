package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** Veza trenera i sertifikata + datum izdavanja. */
@Getter @Setter
@Entity @Table(
        name = "trainer_certificate",
        uniqueConstraints = @UniqueConstraint(name="uk_trainer_certificate", columnNames = {"trainer_id","certificate_id"})
)
public class TrainerCertificate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(optional = false) @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    /** Datum izdavanja (nije obavezno, ali ako je postavljeno, ne sme biti u budućnosti). */
    @PastOrPresent
    private LocalDate issuedAt;
}
