package rs.ac.fon.gymtracker.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;

/** Stavka u treningu: (record_id, rb) je primarni ključ. */
@Getter @Setter
@Entity @Table(name = "training_record_item")
public class TrainingRecordItem {

    /** Složeni PK: (record_id, rb). */
    @EmbeddedId
    private TrainingRecordItemId id;

    /** Veza na glavu treninga – mapiramo deo ključa 'record_id'. */
    @MapsId("recordId") // povezuje id.recordId sa FK kolonom
    @ManyToOne(optional = false)
    @JoinColumn(name = "record_id", nullable = false)
    private TrainingRecord record;


    /** Vezba koja se izvodi u ovoj stavci. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Min(1) private int sets;

    @Min(1) private int reps;

    /** Prosečna težina po seriji (kg), 0 za vežbe bez opterećenja. */
    @DecimalMin("0.0") private double weight;

}
