package rs.ac.fon.gymtracker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.Exercise;
import rs.ac.fon.gymtracker.repository.ExerciseRepository;
import rs.ac.fon.gymtracker.service.ExerciseService;

import java.util.Optional;

@Service
@Transactional
public class ExerciseServiceImpl
        extends AbstractJpaCrudService<Exercise, Long, ExerciseRepository>
        implements ExerciseService {

    public ExerciseServiceImpl(ExerciseRepository repo) { super(repo); }

    @Override
    protected Exercise doMergeAndSave(Exercise current, Exercise patch) {
        if (patch.getName() != null)         current.setName(patch.getName());
        if (patch.getDescription() != null)  current.setDescription(patch.getDescription());
        if (patch.getEffort() > 0)           current.setEffort(patch.getEffort());
        return repo.save(current);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Exercise> findByName(String name) { return repo.findByName(name); }
}
