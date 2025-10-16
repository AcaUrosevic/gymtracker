package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Trener koji vodi treninge; čuvamo osnovne podatke, korisničko ime i (opciono) hash lozinke.
 *
 * <p><b>Ograničenja po poljima:</b></p>
 * <ul>
 *   <li><b>firstName</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       max 60 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 *   <li><b>lastName</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       max 60 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 *   <li><b>email</b> – opciono; ako je postavljen, mora biti validan email i max 120 znakova.
 *       ({@link Email}, {@link Size#max()})</li>
 *   <li><b>username</b> – obavezno; ne sme biti {@code null} ni prazan/praznine;
 *       max 60 znakova. ({@link NotBlank}, {@link Size#max()})</li>
 *   <li><b>passwordHash</b> – opciono; ako je postavljen, max 255 znakova. ({@link Size#max()})</li>
 * </ul>
 *
 * <p>Napomena: jedinstvenost korisničkog imena je obezbeđena na nivou baze (unique key).</p>
 *
 * @author Aleksandar Urošević
 */
@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "trainer")
public class Trainer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ime trenera (obavezno, max 60). */
    @NotBlank @Size(max = 60)
    private String firstName;

    /** Prezime trenera (obavezno, max 60). */
    @NotBlank @Size(max = 60)
    private String lastName;

    /** Email (opciono, validan format, max 120). */
    @Email @Size(max = 120)
    private String email;

    /** Korisničko ime (obavezno, max 60). */
    @NotBlank @Size(max = 60)
    private String username;

    /** Hash lozinke (opciono, max 255). */
    @Size(max = 255)
    private String passwordHash;
}
