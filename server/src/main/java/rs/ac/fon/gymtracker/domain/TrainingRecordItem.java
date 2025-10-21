package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;

/**
 * Stavka u treningu.
 * Primarni ključ je složen: ({@code record_id}, {@code rb}).
 *
 * <p><b>Ograničenja po poljima:</b></p>
 * <ul>
 *   <li><b>record</b> – obavezno; veza na glavu treninga.
 *       ({@link NotNull}, JPA: {@link ManyToOne#optional()} = false)</li>
 *   <li><b>exercise</b> – obavezno; veza na vežbu.
 *       ({@link NotNull}, JPA: {@link ManyToOne#optional()} = false)</li>
 *   <li><b>sets</b> – mora biti ≥ 1. ({@link Min})</li>
 *   <li><b>reps</b> – mora biti ≥ 1. ({@link Min})</li>
 *   <li><b>weight</b> – mora biti ≥ 0.00 (0 je dozvoljen za vežbe bez opterećenja).
 *       ({@link DecimalMin} sa vrednošću "0.0")</li>
 *   <li><b>id</b> – složeni PK ({@code record_id}, {@code rb}); popunjava se van validatora,
 *       kroz servis/repo (nije obavezno za Bean Validation).</li>
 * </ul>
 *
 * Napomena: deo ključa {@code record_id} mapira se iz {@link #record} preko {@link MapsId}.
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter
@Entity
@Table(name = "training_record_item")
public class TrainingRecordItem {

    /** Složeni PK: (record_id, rb). */
    @EmbeddedId
    private TrainingRecordItemId id;

    /** Glava treninga – mapira deo ključa 'record_id'. */
    @NotNull
    @MapsId("recordId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "record_id", nullable = false)
    private TrainingRecord record;

    /** Vežba koja se izvodi u ovoj stavci. */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    /** Broj serija (≥ 1). */
    @Min(1)
    private int sets;

    /** Broj ponavljanja po seriji (≥ 1). */
    @Min(1)
    private int reps;

    /** Prosečna težina po seriji (kg), ≥ 0.00; 0 znači bez opterećenja. */
    @DecimalMin("0.0")
    private double weight;
}
