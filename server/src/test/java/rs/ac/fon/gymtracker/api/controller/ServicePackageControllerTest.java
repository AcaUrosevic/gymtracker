package rs.ac.fon.gymtracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.ac.fon.gymtracker.api.error.GlobalExceptionHandler;
import rs.ac.fon.gymtracker.domain.ServicePackage;
import rs.ac.fon.gymtracker.service.ServicePackageService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ServicePackageControllerTest {

    @Mock private ServicePackageService service;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        var controller = new ServicePackageController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void list_returns_all() throws Exception {
        var p1 = pkg(1L,"Monthly","30 days",30,3000);
        var p2 = pkg(2L,"Quarter","90 days",90,8000);
        given(service.findAll()).willReturn(List.of(p1,p2));

        mvc.perform(get("/api/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Monthly"))
                .andExpect(jsonPath("$[1].price").value(8000));
    }

    @Test
    void get_found_200() throws Exception {
        var p = pkg(5L,"Annual","365",365,25000);
        given(service.findById(5L)).willReturn(Optional.of(p));

        mvc.perform(get("/api/packages/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.durationDays").value(365));
    }

    @Test
    void get_missing_404() throws Exception {
        given(service.findById(99L)).willReturn(Optional.empty());
        mvc.perform(get("/api/packages/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_201() throws Exception {
        var reqJson = """
          {"id":null,"name":"Monthly","description":"30","durationDays":30,"price":3000}
        """;
        var saved = pkg(10L,"Monthly","30",30,3000);
        given(service.create(any(ServicePackage.class))).willReturn(saved);

        mvc.perform(post("/api/packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/packages/10"))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void update_200() throws Exception {
        var reqJson = """
          {"id":5,"name":"Monthly","description":"NEW","durationDays":30,"price":3000}
        """;
        var updated = pkg(5L,"Monthly","NEW",30,3000);
        given(service.update(eq(5L), any(ServicePackage.class))).willReturn(updated);

        mvc.perform(put("/api/packages/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("NEW"));
    }

    @Test
    void delete_204() throws Exception {
        mvc.perform(delete("/api/packages/7")).andExpect(status().isNoContent());
        verify(service).deleteById(7L);
    }

    private static ServicePackage pkg(Long id, String name, String desc, int days, int price){
        var p=new ServicePackage();
        p.setId(id); p.setName(name); p.setDescription(desc); p.setDurationDays(days); p.setPrice(price);
        return p;
    }
}
