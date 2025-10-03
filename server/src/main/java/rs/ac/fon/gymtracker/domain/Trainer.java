package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Trener koji vodi treninge; čuvamo osnovne podatke i korisničko ime.
 * Lozinka se čuva kao hash (passwordHash).
 */

@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "trainer")
public class Trainer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ime trenera. */
    @NotBlank @Size(max = 60)
    private String firstName;

    /** Prezime trenera. */
    @NotBlank @Size(max = 60)
    private String lastName;

    /** Email adresa (opciono, validan format). */
    @Email @Size(max = 120)
    private String email;

    /** Korisničko ime (obavezno, jedinstveno u bazi). */
    @NotBlank @Size(max = 60)
    private String username;

    /** Hash lozinke (opciono u ovoj fazi). */
    @Size(max = 255)
    private String passwordHash;
}
