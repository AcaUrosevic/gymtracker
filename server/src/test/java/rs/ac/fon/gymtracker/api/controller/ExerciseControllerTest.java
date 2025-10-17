package rs.ac.fon.gymtracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.ac.fon.gymtracker.api.error.GlobalExceptionHandler;
import rs.ac.fon.gymtracker.domain.Exercise;
import rs.ac.fon.gymtracker.service.ExerciseService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ExerciseControllerTest {

    private ExerciseService service;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = org.mockito.Mockito.mock(ExerciseService.class);

        var controller = new ExerciseController(service);

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void list_ok() throws Exception {
        var e1 = ex(1L, "Bench Press", "Compound press", 1.26);
        var e2 = ex(2L, "Back Squat", "Compound squat", 1.32);
        given(service.findAll()).willReturn(List.of(e1, e2));

        mvc.perform(get("/api/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Bench Press"))
                .andExpect(jsonPath("$[1].effort").value(1.32));
    }

    @Test
    void get_found_200() throws Exception {
        var e = ex(10L, "Biceps Curl", "Isolation", 1.05);
        given(service.findById(10L)).willReturn(Optional.of(e));

        mvc.perform(get("/api/exercises/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Biceps Curl"))
                .andExpect(jsonPath("$.effort").value(1.05));
    }

    @Test
    void get_missing_404() throws Exception {
        given(service.findById(77L)).willReturn(Optional.empty());

        mvc.perform(get("/api/exercises/77"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_201() throws Exception {
        var reqJson = """
          {"id":null,"name":"Overhead Press","description":"Shoulder press","effort":1.20}
        """;
        var saved = ex(5L, "Overhead Press", "Shoulder press", 1.20);
        given(service.create(any(Exercise.class))).willReturn(saved);

        mvc.perform(post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/exercises/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Overhead Press"));
    }

    @Test
    void update_200() throws Exception {
        var reqJson = """
          {"id":3,"name":"Bench Press","description":"UPDATED","effort":1.25}
        """;
        var updated = ex(3L, "Bench Press", "UPDATED", 1.25);
        given(service.update(eq(3L), any(Exercise.class))).willReturn(updated);

        mvc.perform(put("/api/exercises/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("UPDATED"))
                .andExpect(jsonPath("$.effort").value(1.25));
    }

    @Test
    void delete_204() throws Exception {
        mvc.perform(delete("/api/exercises/9"))
                .andExpect(status().isNoContent());
        verify(service).deleteById(9L);
    }

    private static Exercise ex(Long id, String name, String desc, double effort) {
        var e = new Exercise();
        e.setId(id);
        e.setName(name);
        e.setDescription(desc);
        e.setEffort(effort);
        return e;
    }
}
