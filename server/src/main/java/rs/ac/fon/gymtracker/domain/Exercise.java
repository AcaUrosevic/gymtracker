package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Vežba sa koeficijentom napora (effort) koji utiče na izračun intenziteta treninga.
 */

@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "exercise")
public class Exercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv vežbe (npr. "Bench Press"). */
    @NotBlank @Size(max = 80)
    private String name;

    /** Kratki opis vežbe (opciono). */
    @Size(max = 255)
    private String description;

    /**
     * Koeficijent napora vežbe (tipično 0.10–5.00).
     * Koristi se u formuli: intensity = Σ(sets×reps×weight×exercise.effort).
     */
    @DecimalMin("0.10") @DecimalMax("5.00")
    private double effort = 1.0;
}
