package rs.ac.fon.gymtracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.Exercise;
import rs.ac.fon.gymtracker.repository.ExerciseRepository;
import rs.ac.fon.gymtracker.service.impl.ExerciseServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    @Mock ExerciseRepository repo;
    @InjectMocks ExerciseServiceImpl service;

    @Test
    void update_merges_non_null_fields_only() {
        var current = new Exercise();
        current.setName("Bench Press");
        current.setDescription("old");
        current.setEffort(1.0);

        when(repo.findById(10L)).thenReturn(Optional.of(current));
        when(repo.save(any(Exercise.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Exercise();
        patch.setDescription("new");

        var updated = service.update(10L, patch);

        assertEquals("Bench Press", updated.getName());
        assertEquals("new", updated.getDescription());
        assertEquals(1.0, updated.getEffort(), 0.0001);

        verify(repo).save(any(Exercise.class));
    }
}
