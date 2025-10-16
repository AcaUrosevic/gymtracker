package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Član teretane. Obavezno sadrži ime i prezime; email je opciono polje,
 * a član može (ali ne mora) imati izabran paket usluga.
 *
 * <p><b>Ograničenja po poljima:</b></p>
 * <ul>
 *   <li><b>firstName</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       maksimalno 60 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 *   <li><b>lastName</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       maksimalno 60 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 *   <li><b>email</b> – opciono; ako je zadat, mora biti ispravnog formata i do 120 znakova.
 *       Dozvoljen je i {@code null}. ({@link Email}, {@link Size#max()})</li>
 *   <li><b>servicePackage</b> – opciono; može biti {@code null}. Ako je postavljen,
 *       mora referisati postojeći {@link ServicePackage} (FK).</li>
 * </ul>
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "member")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ime člana (obavezno, max 60). */
    @NotBlank @Size(max = 60)
    private String firstName;

    /** Prezime člana (obavezno, max 60). */
    @NotBlank @Size(max = 60)
    private String lastName;

    /** Email (opciono). Ako je postavljen: validan format i max 120. */
    @Email @Size(max = 120)
    private String email;

    /** Izabrani paket usluga (može biti null). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;
}
