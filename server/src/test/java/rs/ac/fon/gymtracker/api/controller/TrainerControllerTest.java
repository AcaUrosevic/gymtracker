package rs.ac.fon.gymtracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.ac.fon.gymtracker.api.error.GlobalExceptionHandler;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.service.TrainerService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrainerControllerTest {

    @Mock private TrainerService service;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        var controller = new TrainerController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void login_ok_200() throws Exception {
        var t = trainer();
        given(service.login(anyString(), anyString())).willReturn(t);

        var body = """
        {"username":"marko","password":"test"}
        """;

        mvc.perform(post("/api/trainers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("marko"))
                .andExpect(jsonPath("$.firstName").value("Marko"));
    }

    private static Trainer trainer(){
        var t=new Trainer();
        t.setId(5L); t.setFirstName("Marko"); t.setLastName("Marković"); t.setEmail("marko@fit.rs"); t.setUsername("marko");
        return t;
    }
}
