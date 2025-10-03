package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Paket usluga (npr. Monthly, Quarterly).
 * Sadrži naziv, opis, trajanje u danima i cenu (u dinarima).
 */

@Getter @Setter @NoArgsConstructor
@Entity @Table(name = "service_package")
public class ServicePackage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv paketa, obavezno polje (npr. "Monthly"). */
    @NotBlank @Size(max = 80)
    private String name;

    /** Kratki opis paketa (opciono). */
    @Size(max = 255)
    private String description;

    /** Trajanje paketa u danima (mora biti >= 1). */
    @Min(1)
    private int durationDays;

    /** Cena paketa u dinarima (mora biti >= 0). */
    @Min(0)
    private int price;
}
