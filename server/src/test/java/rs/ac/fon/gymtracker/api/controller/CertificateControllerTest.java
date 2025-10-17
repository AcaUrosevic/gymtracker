package rs.ac.fon.gymtracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.ac.fon.gymtracker.api.error.GlobalExceptionHandler;
import rs.ac.fon.gymtracker.domain.Certificate;
import rs.ac.fon.gymtracker.service.CertificateService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CertificateControllerTest {

    private CertificateService service;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = org.mockito.Mockito.mock(CertificateService.class);

        var controller = new CertificateController(service);

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void list_ok() throws Exception {
        var c1 = cert(1L, "ACE CPT", "PT");
        var c2 = cert(2L, "S&C Lv1", "S&C");
        given(service.findAll()).willReturn(List.of(c1, c2));

        mvc.perform(get("/api/certificates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("ACE CPT"))
                .andExpect(jsonPath("$[1].type").value("S&C"));
    }

    @Test
    void get_found_200() throws Exception {
        var c = cert(10L, "USAW Lv1", "WL");
        given(service.findById(10L)).willReturn(Optional.of(c));

        mvc.perform(get("/api/certificates/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("USAW Lv1"))
                .andExpect(jsonPath("$.type").value("WL"));
    }

    @Test
    void get_missing_404() throws Exception {
        given(service.findById(77L)).willReturn(Optional.empty());

        mvc.perform(get("/api/certificates/77"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_201() throws Exception {
        var reqJson = """
          {"id":null,"name":"NASM CPT","type":"PT"}
        """;
        var saved = cert(5L, "NASM CPT", "PT");
        given(service.create(any(Certificate.class))).willReturn(saved);

        mvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/certificates/5"))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("NASM CPT"));
    }

    @Test
    void update_200() throws Exception {
        var reqJson = """
          {"id":3,"name":"ACE CPT","type":"UPDATED"}
        """;
        var updated = cert(3L, "ACE CPT", "UPDATED");
        given(service.update(eq(3L), any(Certificate.class))).willReturn(updated);

        mvc.perform(put("/api/certificates/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("UPDATED"));
    }

    @Test
    void delete_204() throws Exception {
        mvc.perform(delete("/api/certificates/9"))
                .andExpect(status().isNoContent());
        verify(service).deleteById(9L);
    }

    private static Certificate cert(Long id, String name, String type) {
        var c = new Certificate();
        c.setId(id);
        c.setName(name);
        c.setType(type);
        return c;
    }
}
