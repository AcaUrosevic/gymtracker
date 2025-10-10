package rs.ac.fon.gymtracker.infrastructure.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import rs.ac.fon.gymtracker.domain.Member;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.domain.TrainingRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class JsonExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void writes_training_record_to_json_file() throws IOException {
        // arrange
        var trainer = new Trainer();
        trainer.setFirstName("Milan");
        trainer.setLastName("Milić");
        trainer.setUsername("milan");

        var member = new Member();
        member.setFirstName("Petar");
        member.setLastName("Petrović");

        var tr = new TrainingRecord();
        tr.setTrainingDate(LocalDate.of(2025, 1, 15));
        tr.setIntensity(123.45);
        tr.setTrainer(trainer);
        tr.setMember(member);

        Path out = tempDir.resolve("training-record.json");


        // act
        JsonExporter.write(out, tr);
        System.out.println(Files.readString(out));

        // assert – parsiramo umesto string contains
        var mapper = new ObjectMapper().findAndRegisterModules();
        JsonNode root = mapper.readTree(out.toFile());

        assertEquals("2025-01-15", root.get("trainingDate").asText());
        assertEquals(123.45, root.get("intensity").asDouble(), 0.0001);
        assertEquals("Petar", root.get("member").get("firstName").asText());
        assertEquals("Milan", root.get("trainer").get("firstName").asText());
    }
}
