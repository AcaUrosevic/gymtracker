package rs.ac.fon.gymtracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.ac.fon.gymtracker.api.error.GlobalExceptionHandler;
import rs.ac.fon.gymtracker.domain.*;
import rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId;
import rs.ac.fon.gymtracker.service.TrainingRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrainingRecordControllerTest {

    @Mock private TrainingRecordService service;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        var controller = new TrainingRecordController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void get_returns_record_with_items() throws Exception {
        var rec = sampleRecordWithOneItem(100L);
        given(service.findById(100L)).willReturn(Optional.of(rec));

        mvc.perform(get("/api/training-records/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.trainerName").value("Ana Jovanović"));
    }

    @Test
    void add_item_201() throws Exception {
        var item = new TrainingRecordItem();
        item.setId(new TrainingRecordItemId(100L, 2));
        var ex = new Exercise(); ex.setId(3L); ex.setName("Back Squat");
        item.setExercise(ex); item.setSets(5); item.setReps(5); item.setWeight(90);
        given(service.addItem(eq(100L), eq(3L), eq(5), eq(5), eq(90.0))).willReturn(item);

        var body = """
        {"exerciseId":3,"sets":5,"reps":5,"weight":90.0}
        """;

        mvc.perform(post("/api/training-records/100/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","/api/training-records/100/items/2"))
                .andExpect(jsonPath("$.exerciseName").value("Back Squat"));
    }

    @Test
    void by_member_ok() throws Exception {
        given(service.listByMember(2L)).willReturn(List.of(sampleRecordWithOneItem(101L)));

        mvc.perform(get("/api/training-records/member/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101));
    }

    @Test
    void delete_item_204() throws Exception {
        mvc.perform(delete("/api/training-records/100/items/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(service).deleteItem(Mockito.argThat(id ->
                id.getRecordId().equals(100L) && id.getRb().equals(1)));
    }

    private static TrainingRecord sampleRecordWithOneItem(Long id){
        var trainer = new Trainer(); trainer.setId(1L); trainer.setFirstName("Ana"); trainer.setLastName("Jovanović");
        var member = new Member(); member.setId(2L); member.setFirstName("Petar"); member.setLastName("Petrović");

        var ex = new Exercise(); ex.setId(1L); ex.setName("Bench Press"); ex.setEffort(1.26);

        var rec = new TrainingRecord();
        rec.setId(id); rec.setTrainingDate(LocalDate.of(2025,1,15));
        rec.setTrainer(trainer); rec.setMember(member); rec.setIntensity(0.0);

        var it = new TrainingRecordItem();
        it.setId(new rs.ac.fon.gymtracker.domain.id.TrainingRecordItemId(id,1));
        it.setRecord(rec); it.setExercise(ex); it.setSets(4); it.setReps(8); it.setWeight(60);

        rec.setItems(List.of(it));
        return rec;
    }
}
