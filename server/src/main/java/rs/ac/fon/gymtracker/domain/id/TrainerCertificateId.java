package rs.ac.fon.gymtracker.domain.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/** PK za trainer_certificate: (trainer_id, certificate_id). */
@Getter @Setter
@Embeddable
public class TrainerCertificateId implements Serializable {

    @Column(name = "trainer_id", nullable = false)
    private Long trainerId;

    @Column(name = "certificate_id", nullable = false)
    private Long certificateId;

    public TrainerCertificateId() {}

    public TrainerCertificateId(Long trainerId, Long certificateId) {
        this.trainerId = trainerId;
        this.certificateId = certificateId;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainerCertificateId that)) return false;
        return Objects.equals(trainerId, that.trainerId)
                && Objects.equals(certificateId, that.certificateId);
    }

    @Override public int hashCode() {
        return Objects.hash(trainerId, certificateId);
    }
}
