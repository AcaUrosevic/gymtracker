package rs.ac.fon.gymtracker.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rs.ac.fon.gymtracker.domain.*;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainingRecordItemRepositoryTest {

    @Autowired TrainerRepository trainerRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ServicePackageRepository servicePackageRepository;
    @Autowired ExerciseRepository exerciseRepository;
    @Autowired TrainingRecordRepository trainingRecordRepository;
    @Autowired TrainingRecordItemRepository itemRepository;

    @Test
    void save_and_get_item_by_embedded_id() {
        var sp = new ServicePackage();
        sp.setName("TestPack");
        sp.setDescription("desc");
        sp.setDurationDays(30);
        sp.setPrice(1000);
        servicePackageRepository.save(sp);

        var trn = new Trainer();
        trn.setFirstName("Milan");
        trn.setLastName("Milić");
        trn.setUsername("milan1");
        trainerRepository.save(trn);

        var m = new Member();
        m.setFirstName("Petar");
        m.setLastName("Petrović");
        m.setServicePackage(sp);
        memberRepository.save(m);

        var ex = new Exercise();
        ex.setName("Bench Press X");
        ex.setDescription("desc");
        ex.setEffort(1.2);
        exerciseRepository.save(ex);

        var record = new TrainingRecord();
        record.setTrainingDate(LocalDate.of(2025, 1, 15));
        record.setIntensity(0.0);
        record.setTrainer(trn);
        record.setMember(m);
        trainingRecordRepository.save(record);

        var item = new TrainingRecordItem();
        item.setRecord(record);
        item.setExercise(ex);
        item.setSets(4);
        item.setReps(8);
        item.setWeight(60.0);

        var id = new TrainingRecordItemId(record.getId(), 1);
        item.setId(id);

        itemRepository.save(item);

        var found = itemRepository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(4, found.get().getSets());
        assertEquals(8, found.get().getReps());
        assertEquals(60.0, found.get().getWeight(), 0.0001);
        assertEquals("Bench Press X", found.get().getExercise().getName());
    }
}
