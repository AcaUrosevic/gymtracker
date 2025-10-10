package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Član teretane; opciono može imati izabran paket usluga.
 */

@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "member")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ime člana. */
    @NotBlank @Size(max = 60)
    private String firstName;

    /** Prezime člana. */
    @NotBlank @Size(max = 60)
    private String lastName;

    /** Email adresa člana (opciono, validan format). */
    @Email @Size(max = 120)
    private String email;

    /** Izabrani paket usluga (FK ka service_package), može biti null. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;
}
