package rs.ac.fon.gymtracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.ac.fon.gymtracker.api.error.GlobalExceptionHandler;
import rs.ac.fon.gymtracker.domain.Member;
import rs.ac.fon.gymtracker.domain.ServicePackage;
import rs.ac.fon.gymtracker.service.MemberService;
import rs.ac.fon.gymtracker.service.ServicePackageService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest {

    @Mock private MemberService memberService;
    @Mock private ServicePackageService packageService;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        var controller = new MemberController(memberService, packageService);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        new ObjectMapper().findAndRegisterModules();
    }

    @Test
    void list_ok() throws Exception {
        var pkg = pkg();
        var m1 = member(10L,"Petar","Petrović","petar@example.com",pkg);
        given(memberService.findAll()).willReturn(List.of(m1));

        mvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("petar@example.com"))
                .andExpect(jsonPath("$[0].packageId").value(1));
    }

    @Test
    void create_binds_package_201() throws Exception {
        var req = """
        {"id":null,"firstName":"Ivana","lastName":"Ivić","email":"ivana@example.com","packageId":1,"packageName":null}
        """;
        given(packageService.findById(1L)).willReturn(Optional.of(pkg()));
        var saved = member(20L,"Ivana","Ivić","ivana@example.com", pkg());
        given(memberService.create(any(Member.class))).willReturn(saved);

        mvc.perform(post("/api/members").contentType(MediaType.APPLICATION_JSON).content(req))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","/api/members/20"))
                .andExpect(jsonPath("$.id").value(20));
    }

    private static ServicePackage pkg(){
        var p=new ServicePackage(); p.setId(1L); p.setName("Monthly"); return p;
    }
    private static Member member(Long id, String fn, String ln, String mail, ServicePackage sp){
        var m=new Member(); m.setId(id); m.setFirstName(fn); m.setLastName(ln); m.setEmail(mail); m.setServicePackage(sp); return m;
    }
}
