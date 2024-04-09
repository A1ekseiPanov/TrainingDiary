package ru.panov.dao;

import java.util.List;
import java.util.Optional;

public interface AbstractBaseDAO<K, E> {
    Optional<E> findById(K id);
    List<E> findAll();
    E save(E entity);
}