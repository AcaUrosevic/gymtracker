package rs.ac.fon.gymtracker.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.Trainer;
import rs.ac.fon.gymtracker.repository.TrainerRepository;
import rs.ac.fon.gymtracker.service.TrainerService;

import java.util.Optional;

@Service
@Transactional
public class TrainerServiceImpl
        extends AbstractJpaCrudService<Trainer, Long, TrainerRepository>
        implements TrainerService {

    private final PasswordEncoder passwordEncoder;

    public TrainerServiceImpl(TrainerRepository repo, PasswordEncoder passwordEncoder) {
        super(repo);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected Trainer doMergeAndSave(Trainer current, Trainer patch) {
        if (patch.getFirstName() != null) current.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null)  current.setLastName(patch.getLastName());
        if (patch.getEmail() != null)     current.setEmail(patch.getEmail());
        if (patch.getUsername() != null)  current.setUsername(patch.getUsername());
        if (patch.getPasswordHash() != null) current.setPasswordHash(patch.getPasswordHash());
        return repo.save(current);
    }

    @Override
    public Trainer login(String username, String rawPassword) {
        var t = repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Bad credentials"));
        if (!passwordEncoder.matches(rawPassword, t.getPasswordHash())) {
            throw new IllegalArgumentException("Bad credentials");
        }
        return t;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findByUsername(String username) {
        return repo.findByUsername(username);
    }
}

