package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import rs.ac.fon.gymtracker.domain.id.TrainerCertificateId;

import java.time.LocalDate;

/**
 * Veza trenera i sertifikata sa datumom izdavanja.
 *
 * <p><b>Ograničenja po poljima:</b></p>
 * <ul>
 *   <li><b>trainer</b> – obavezno; ne sme biti {@code null}. ({@link NotNull})</li>
 *   <li><b>certificate</b> – obavezno; ne sme biti {@code null}. ({@link NotNull})</li>
 *   <li><b>issuedAt</b> – opciono; ako je postavljeno, mora biti u prošlosti ili danas.
 *       ({@link PastOrPresent})</li>
 *   <li>Primarni ključ je složen: ({@code trainer_id}, {@code certificate_id}) preko
 *       {@link TrainerCertificateId} i {@link MapsId} mapiranja.</li>
 * </ul>
 *
 * <p>Napomena: {@code id} (ugnežđeni PK) se populira iz referenci preko
 * {@link MapsId}, nije predmet ručnog unosa.</p>
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter
@Entity
@Table(
        name = "trainer_certificate",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_trainer_certificate",
                columnNames = {"trainer_id","certificate_id"}
        )
)
public class TrainerCertificate {

    /** Složeni PK: (trainer_id, certificate_id). Popunjava se iz referenci. */
    @EmbeddedId
    private TrainerCertificateId id;

    /** FK na trenera – obavezno. Mapira deo ključa 'trainer_id'. */
    @NotNull
    @MapsId("trainerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    /** FK na sertifikat – obavezno. Mapira deo ključa 'certificate_id'. */
    @NotNull
    @MapsId("certificateId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

    /** Datum izdavanja (opciono; ako postoji, ne sme biti u budućnosti). */
    @PastOrPresent
    private LocalDate issuedAt;
}
