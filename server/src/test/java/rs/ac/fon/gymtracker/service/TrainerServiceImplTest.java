package rs.ac.fon.gymtracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.repository.TrainerRepository;
import rs.ac.fon.gymtracker.service.impl.TrainerServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock TrainerRepository repo;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks TrainerServiceImpl service;

    @Test
    void login_ok_when_password_matches() {
        var t = new Trainer();
        t.setUsername("milan");
        t.setPasswordHash("$hash$");
        when(repo.findByUsername("milan")).thenReturn(Optional.of(t));
        when(passwordEncoder.matches("sifra", "$hash$")).thenReturn(true);

        var result = service.login("milan", "sifra");

        assertSame(t, result);
        verify(repo).findByUsername("milan");
        verify(passwordEncoder).matches("sifra", "$hash$");
    }

    @Test
    void login_throws_when_bad_credentials() {
        when(repo.findByUsername("milan")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.login("milan", "x"));
    }

    @Test
    void update_merges_non_null_fields_only() {
        var current = new Trainer();
        current.setFirstName("Old");
        current.setLastName("Name");
        current.setEmail("old@x");
        current.setUsername("olduser");

        when(repo.findById(1L)).thenReturn(Optional.of(current));
        when(repo.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Trainer();
        patch.setFirstName("New");

        var updated = service.update(1L, patch);

        assertEquals("New", updated.getFirstName());
        assertEquals("Name", updated.getLastName());
        assertEquals("old@x", updated.getEmail());
        assertEquals("olduser", updated.getUsername());

        var captor = ArgumentCaptor.forClass(Trainer.class);
        verify(repo).save(captor.capture());
        assertEquals("New", captor.getValue().getFirstName());
    }
}
