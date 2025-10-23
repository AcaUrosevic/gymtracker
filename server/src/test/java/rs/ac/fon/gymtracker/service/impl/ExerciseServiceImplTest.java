package rs.ac.fon.gymtracker.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.Exercise;
import rs.ac.fon.gymtracker.repository.ExerciseRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository repo;

    @InjectMocks
    private ExerciseServiceImpl service;

    private Exercise base;

    @BeforeEach
    void setUp() {
        base = new Exercise();
        base.setId(null);
        base.setName("Bench Press");
        base.setDescription("Compound upper body press");
        base.setEffort(1.25);
    }

    @AfterEach
    void tearDown() {
        base = null;
    }

    private static Exercise e(Long id, String name, String desc, double effort) {
        var x = new Exercise();
        x.setId(id);
        x.setName(name);
        x.setDescription(desc);
        x.setEffort(effort);
        return x;
    }

    @Test
    void testCreate() {
        var toSave = base;
        var saved  = e(10L, "Bench Press", "Compound upper body press", 1.25);

        when(repo.save(toSave)).thenReturn(saved);

        var out = service.create(toSave);

        assertEquals(10L, out.getId());
        assertEquals("Bench Press", out.getName());
        assertEquals("Compound upper body press", out.getDescription());
        assertEquals(1.25, out.getEffort());

        verify(repo).save(toSave);
    }

    @Test
    void testFindByIdFound() {
        when(repo.findById(5L)).thenReturn(Optional.of(
                e(5L, "Squat", "Compound lower body", 1.30)
        ));

        var out = service.findById(5L);

        assertTrue(out.isPresent());
        assertEquals(5L, out.get().getId());
        assertEquals("Squat", out.get().getName());
        assertEquals(1.30, out.get().getEffort());

        verify(repo).findById(5L);
    }

    @Test
    void testFindByIdMissing() {
        when(repo.findById(77L)).thenReturn(Optional.empty());

        var out = service.findById(77L);

        assertTrue(out.isEmpty());

        verify(repo).findById(77L);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(
                e(1L, "Bench Press", "Press", 1.25),
                e(2L, "Deadlift", "Hinge", 1.40)
        ));

        var out = service.findAll();

        assertEquals(2, out.size());
        assertEquals("Deadlift", out.get(1).getName());
        assertEquals(1.40, out.get(1).getEffort());

        verify(repo).findAll();
    }

    @Test
    void testUpdateMergesNonNullAndPositiveEffort() {
        var current = e(3L, "Bench Press", "Press", 1.20);

        when(repo.findById(3L)).thenReturn(Optional.of(current));
        when(repo.save(any(Exercise.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Exercise();
        patch.setName("Incline Bench Press");
        patch.setDescription("Incline press");
        patch.setEffort(1.35);

        var updated = service.update(3L, patch);

        assertEquals(3L, updated.getId());
        assertEquals("Incline Bench Press", updated.getName());
        assertEquals("Incline press", updated.getDescription());
        assertEquals(1.35, updated.getEffort());

        verify(repo).findById(3L);
        verify(repo).save(current);
    }

    @Test
    void testUpdateDoesNotChangeEffortWhenNonPositive() {
        var current = e(4L, "Row", "Pull", 1.10);

        when(repo.findById(4L)).thenReturn(Optional.of(current));
        when(repo.save(any(Exercise.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Exercise();
        patch.setName(null);
        patch.setDescription(null);
        patch.setEffort(0.0); // <= 0 => ne menja effort prema implementaciji

        var updated = service.update(4L, patch);

        assertEquals(4L, updated.getId());
        assertEquals("Row", updated.getName());
        assertEquals("Pull", updated.getDescription());
        assertEquals(1.10, updated.getEffort());

        verify(repo).findById(4L);
        verify(repo).save(current);
    }

    @Test
    void testUpdateWhenMissingThrows() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        var patch = new Exercise();
        patch.setName("Overhead Press");

        assertThrows(NoSuchElementException.class, () -> service.update(99L, patch));

        verify(repo).findById(99L);
    }

    @Test
    void testDeleteById() {
        service.deleteById(44L);
        verify(repo).deleteById(44L);
    }

    @Test
    void testFindByNameFound() {
        when(repo.findByName("Bench Press")).thenReturn(Optional.of(
                e(7L, "Bench Press", "Press", 1.25)
        ));

        var out = service.findByName("Bench Press");

        assertTrue(out.isPresent());
        assertEquals(7L, out.get().getId());
        assertEquals("Bench Press", out.get().getName());

        verify(repo).findByName("Bench Press");
    }

    @Test
    void testFindByNameMissing() {
        when(repo.findByName("Nonexistent")).thenReturn(Optional.empty());

        var out = service.findByName("Nonexistent");

        assertTrue(out.isEmpty());

        verify(repo).findByName("Nonexistent");
    }
}
