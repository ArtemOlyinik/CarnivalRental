package com.carnival.rental.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {

  List<T> findAll();

  Optional<T> findById(UUID id);

  void save(T entity);

  void delete(UUID id);

  void saveAll(List<T> entities); // Метод для збереження всього списку (для файлів це перезапис)
}