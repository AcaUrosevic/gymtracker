package rs.ac.fon.gymtracker.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.repository.TrainerRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private TrainerServiceImpl service;

    private Trainer base;

    @BeforeEach
    void setUp() {
        base = new Trainer();
        base.setId(null);
        base.setFirstName("Marko");
        base.setLastName("Marković");
        base.setUsername("marko123");
        base.setEmail("marko@example.com");
        base.setPasswordHash("hashed");
    }

    @AfterEach
    void tearDown() {
        base = null;
    }

    private static Trainer t(Long id, String fn, String ln, String un, String email, String hash) {
        var x = new Trainer();
        x.setId(id);
        x.setFirstName(fn);
        x.setLastName(ln);
        x.setUsername(un);
        x.setEmail(email);
        x.setPasswordHash(hash);
        return x;
    }

    @Test
    void testCreate() {
        var toSave = base;
        var saved = t(1L, "Marko", "Marković", "marko123", "marko@example.com", "hashed");

        when(repo.save(toSave)).thenReturn(saved);

        var out = service.create(toSave);

        assertEquals(1L, out.getId());
        assertEquals("marko123", out.getUsername());
        assertEquals("Marko", out.getFirstName());

        verify(repo).save(toSave);
    }

    @Test
    void testFindByIdFound() {
        when(repo.findById(2L)).thenReturn(Optional.of(t(2L, "Ana", "Anić", "ana22", "ana@example.com", "pass")));

        var out = service.findById(2L);

        assertTrue(out.isPresent());
        assertEquals("ana22", out.get().getUsername());

        verify(repo).findById(2L);
    }

    @Test
    void testFindByIdMissing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        var out = service.findById(99L);

        assertTrue(out.isEmpty());

        verify(repo).findById(99L);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(
                t(1L, "Marko", "Marković", "marko123", "marko@example.com", "hashed"),
                t(2L, "Ana", "Anić", "ana22", "ana@example.com", "pass")
        ));

        var out = service.findAll();

        assertEquals(2, out.size());
        assertEquals("ana22", out.get(1).getUsername());

        verify(repo).findAll();
    }

    @Test
    void testUpdateMergesNonNullFieldsOnly() {
        var current = t(3L, "Petar", "Petrović", "petar", "petar@example.com", "hash");

        when(repo.findById(3L)).thenReturn(Optional.of(current));
        when(repo.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Trainer();
        patch.setEmail("new@example.com");
        patch.setPasswordHash("newHash");

        var updated = service.update(3L, patch);

        assertEquals(3L, updated.getId());
        assertEquals("Petar", updated.getFirstName()); // ostalo staro
        assertEquals("new@example.com", updated.getEmail()); // promenjeno
        assertEquals("newHash", updated.getPasswordHash()); // promenjeno

        verify(repo).findById(3L);
        verify(repo).save(current);
    }

    @Test
    void testUpdateWhenMissingThrows() {
        when(repo.findById(77L)).thenReturn(Optional.empty());

        var patch = new Trainer();
        patch.setUsername("x");

        assertThrows(NoSuchElementException.class, () -> service.update(77L, patch));

        verify(repo).findById(77L);
    }

    @Test
    void testDeleteById() {
        service.deleteById(55L);
        verify(repo).deleteById(55L);
    }

    @Test
    void testFindByUsernameFound() {
        when(repo.findByUsername("marko123")).thenReturn(Optional.of(t(1L, "Marko", "Marković", "marko123", "marko@example.com", "hashed")));

        var out = service.findByUsername("marko123");

        assertTrue(out.isPresent());
        assertEquals("Marko", out.get().getFirstName());

        verify(repo).findByUsername("marko123");
    }

    @Test
    void testFindByUsernameMissing() {
        when(repo.findByUsername("ghost")).thenReturn(Optional.empty());

        var out = service.findByUsername("ghost");

        assertTrue(out.isEmpty());

        verify(repo).findByUsername("ghost");
    }

    @Test
    void testLoginSuccess() {
        var trainer = t(1L, "Marko", "Marković", "marko123", "marko@example.com", "hashed");

        when(repo.findByUsername("marko123")).thenReturn(Optional.of(trainer));
        when(encoder.matches("raw", "hashed")).thenReturn(true);

        var out = service.login("marko123", "raw");

        assertEquals("Marko", out.getFirstName());

        verify(repo).findByUsername("marko123");
        verify(encoder).matches("raw", "hashed");
    }

    @Test
    void testLoginWrongPasswordThrows() {
        var trainer = t(1L, "Marko", "Marković", "marko123", "marko@example.com", "hashed");

        when(repo.findByUsername("marko123")).thenReturn(Optional.of(trainer));
        when(encoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.login("marko123", "wrong"));

        verify(repo).findByUsername("marko123");
        verify(encoder).matches("wrong", "hashed");
    }

    @Test
    void testLoginUnknownUserThrows() {
        when(repo.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.login("ghost", "any"));

        verify(repo).findByUsername("ghost");
    }
}