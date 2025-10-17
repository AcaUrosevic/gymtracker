package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Jedan odrađen trening: datum, trener, član i izračunati intenzitet (AVL).
 * Stavke (serije/ponavljanja/težine) su u zasebnoj tabeli (training_record_item).
 *
 * <p><b>Ograničenja po poljima:</b></p>
 * <ul>
 *   <li><b>trainingDate</b> – obavezno; ne sme biti {@code null}. ({@link NotNull})</li>
 *   <li><b>intensity</b> – ≥ 0.00; računa se u servisu (ne sme biti negativno). ({@link DecimalMin})</li>
 *   <li><b>trainer</b> – obavezno; ne sme biti {@code null}. ({@link NotNull}, {@link ManyToOne} optional=false)</li>
 *   <li><b>member</b> – obavezno; ne sme biti {@code null}. ({@link NotNull}, {@link ManyToOne} optional=false)</li>
 * </ul>
 *
 * <p>Napomena: intenzitet se računa po formuli
 * Σ(sets × reps × weight × exercise.effort) u servisnom sloju.</p>
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "training_record")
public class TrainingRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Datum treninga (obavezno). */
    @NotNull
    private LocalDate trainingDate;

    /** Intenzitet (AVL) ≥ 0.00; računa ga servis. */
    @DecimalMin("0.00")
    private double intensity;

    /** Trener (obavezno). */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    /** Član (obavezno). */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<TrainingRecordItem> items = new java.util.ArrayList<>();
}
