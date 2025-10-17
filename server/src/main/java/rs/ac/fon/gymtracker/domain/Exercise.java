package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domen klasa koja opisuje jednu vežbu.
 *
 * <p><b>Ograničenja polja:</b></p>
 * <ul>
 *   <li><b>name</b> – obavezno; ne sme biti {@code null} ni prazan string;
 *       maksimalno 80 karaktera (@NotBlank, @Size(max=80)).</li>
 *   <li><b>description</b> – opciono; ako je postavljeno, maksimalno 255
 *       karaktera (@Size(max=255)).</li>
 *   <li><b>effort</b> – obavezno; realan broj u opsegu <b>[0.10, 5.00]</b>
 *       (@DecimalMin("0.10"), @DecimalMax("5.00")).
 *       Podrazumevana vrednost je 1.0. Koristi se u formuli:
 *       {@code intensity = Σ(sets × reps × weight × exercise.effort)}.</li>
 * </ul>
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "exercise")
public class Exercise {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv vežbe (npr. "Bench Press"). */
    @NotBlank
    @Size(max = 80)
    private String name;

    /** Kratki opis vežbe (opciono, do 255 karaktera). */
    @Size(max = 255)
    private String description;

    /**
     * Koeficijent napora vežbe (tipično 0.10–5.00).
     * Koristi se u formuli: intensity = Σ(sets×reps×weight×exercise.effort).
     */
    @DecimalMin("0.10")
    @DecimalMax("5.00")
    private double effort = 1.0;
}
