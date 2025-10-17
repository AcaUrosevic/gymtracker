package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Sertifikat koji trener može da poseduje (npr. "ISSA CPT").
 *
 * <p><b>Ograničenja po poljima:</b></p>
 * <ul>
 *   <li><b>name</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       maksimalno 120 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 *   <li><b>type</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       maksimalno 80 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 * </ul>
 *
 * <p>Primer valjanog unosa: name = "ISSA CPT", type = "Strength".</p>
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter
@Entity @Table(name = "certificate")
public class Certificate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv sertifikata (obavezno, max 120). */
    @NotBlank @Size(max = 120)
    private String name;

    /** Tip/oblast sertifikata (obavezno, max 80). */
    @NotBlank @Size(max = 80)
    private String type;
}
