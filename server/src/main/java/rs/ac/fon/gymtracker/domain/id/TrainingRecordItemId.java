package rs.ac.fon.gymtracker.domain.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/** Kompozitni ključ za stavku treninga: (record_id, rb). */
@Setter
@Getter
@Embeddable
public class TrainingRecordItemId implements Serializable {

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "rb", nullable = false)
    private Integer rb;

    public TrainingRecordItemId() {}

    public TrainingRecordItemId(Long recordId, Integer rb) {
        this.recordId = recordId;
        this.rb = rb;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingRecordItemId that)) return false;
        return Objects.equals(recordId, that.recordId) && Objects.equals(rb, that.rb);
    }
    @Override public int hashCode() {
        return Objects.hash(recordId, rb);
    }
}
