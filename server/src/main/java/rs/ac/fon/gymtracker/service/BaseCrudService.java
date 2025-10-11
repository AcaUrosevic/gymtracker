package rs.ac.fon.gymtracker.service;

import java.util.List;
import java.util.Optional;

public interface BaseCrudService<T, ID> {
    T create(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    T update(ID id, T patch);
    void deleteById(ID id);
}
