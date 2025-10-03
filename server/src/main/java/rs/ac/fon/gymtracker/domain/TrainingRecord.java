package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Jedan odrađen trening: datum, trener, član i izračunati intenzitet (AVL).
 * Stavke (vežbe/serije/ponavljanja/težine) dodajemo kroz TrainingItem entitet.
 */

@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "training_record")
public class TrainingRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Datum treninga. */
    @NotNull
    private LocalDate trainingDate;

    /**
     * Intenzitet (AVL) računat u servisu:
     * Σ(sets × reps × weight × exercise.effort).
     */
    @DecimalMin("0.00")
    private double intensity;

    /** Trener koji je vodio trening (FK ka trainer). */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    /** Član koji je odradio trening (FK ka member). */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
