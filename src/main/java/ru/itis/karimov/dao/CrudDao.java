package ru.itis.karimov.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T, ID> {
    Optional<T> findById(ID id);
    T save(T entity);
    List<T> findAll();
    void update(T entity);
    void deleteById(ID id);
}
