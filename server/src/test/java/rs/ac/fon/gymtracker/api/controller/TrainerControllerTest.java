package rs.ac.fon.gymtracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.ac.fon.gymtracker.api.error.GlobalExceptionHandler;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.infrastructure.security.JwtUtil;
import rs.ac.fon.gymtracker.service.TrainerService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrainerControllerTest {

    @Mock private TrainerService service;
    @Mock private JwtUtil jwtUtil;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        var controller = new TrainerController(service, jwtUtil);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void login_ok_200() throws Exception {
        var t = trainer();
        given(service.login(anyString(), anyString())).willReturn(t);
        given(jwtUtil.generate(anyString(), ArgumentMatchers.<Map<String,Object>>any()))
                .willReturn("fake.jwt.token");
        var body = """
                    {"username":"marko","password":"test"}
                   """;

        mvc.perform(post("/api/trainers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake.jwt.token"))
                .andExpect(jsonPath("$.user.username").value("marko"))
                .andExpect(jsonPath("$.user.firstName").value("Marko"));
    }

    private static Trainer trainer(){
        var t=new Trainer();
        t.setId(5L); t.setFirstName("Marko"); t.setLastName("Marković"); t.setEmail("marko@fit.rs"); t.setUsername("marko");
        return t;
    }
}
