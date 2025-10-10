package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/** Sertifikat koji trener može da poseduje (npr. ISSA CPT). */
@Getter @Setter
@Entity @Table(name = "certificate")
public class Certificate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Naziv sertifikata (npr. "ISSA CPT"). */
    @NotBlank @Size(max = 120)
    private String name;

    /** Tip/oblast (npr. "Strength", "Nutrition"). */
    @NotBlank @Size(max = 80)
    private String type;
}
