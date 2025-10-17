package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Paket usluga (npr. "Monthly", "Quarterly").
 * Sadrži naziv, opciono opis, trajanje u danima i cenu (u dinarima).
 *
 * <p><b>Ograničenja po poljima:</b></p>
 * <ul>
 *   <li><b>name</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       maksimalno 80 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 *   <li><b>description</b> – opciono; ako je postavljeno, do 255 znakova. ({@link Size#max()})</li>
 *   <li><b>durationDays</b> – mora biti ≥ 1 (broj dana trajanja). ({@link Min})</li>
 *   <li><b>price</b> – mora biti ≥ 0 (iznos u dinarima). ({@link Min})</li>
 * </ul>
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "service_package")
public class ServicePackage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv paketa (obavezno, max 80). */
    @NotBlank @Size(max = 80)
    private String name;

    /** Kratki opis (opciono, max 255). */
    @Size(max = 255)
    private String description;

    /** Trajanje u danima (≥ 1). */
    @Min(1)
    private int durationDays;

    /** Cena u dinarima (≥ 0). */
    @Min(0)
    private int price;
}
