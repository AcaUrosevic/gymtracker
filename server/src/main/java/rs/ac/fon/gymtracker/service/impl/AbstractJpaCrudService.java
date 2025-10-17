package rs.ac.fon.gymtracker.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.service.BaseCrudService;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractJpaCrudService<T, ID, R extends JpaRepository<T, ID>>
        implements BaseCrudService<T, ID> {

    protected final R repo;

    protected AbstractJpaCrudService(R repo) { this.repo = repo; }

    @Override public T create(T entity) { return repo.save(entity); }

    @Override @Transactional(readOnly = true)
    public Optional<T> findById(ID id) { return repo.findById(id); }

    @Override @Transactional(readOnly = true)
    public List<T> findAll() { return repo.findAll(); }

    @Override
    public T update(ID id, T patch) {
        var current = repo.findById(id).orElseThrow();
        return doMergeAndSave(current, patch);
    }

    protected T doMergeAndSave(T current, T patch) {
        throw new UnsupportedOperationException("Override doMergeAndSave in subclass");
    }

    @Override public void deleteById(ID id) { repo.deleteById(id); }
}
