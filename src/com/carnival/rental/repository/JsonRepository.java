package com.carnival.rental.repository;

import com.carnival.rental.entity.BaseEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class JsonRepository<T extends BaseEntity> implements Repository<T> {

  protected final Gson gson; // Забираємо final і ініціалізацію тут
  protected final Path filePath;
  protected final Type type;

  public JsonRepository(String fileName, Type type) {
    this.filePath = Paths.get("data", fileName);
    this.type = type;
    this.gson = createGson(); // Викликаємо метод створення
    createDataDirectory();
  }

  // Метод, який можна перевизначити в дочірніх класах
  protected Gson createGson() {
    return new GsonBuilder().setPrettyPrinting().create();
  }

  private void createDataDirectory() {
    try {
      if (!Files.exists(filePath.getParent())) {
        Files.createDirectories(filePath.getParent());
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not create data directory", e);
    }
  }

  // Завантаження всіх даних з файлу
  @Override
  public List<T> findAll() {
    if (!Files.exists(filePath)) {
      return new ArrayList<>();
    }
    try {
      String json = Files.readString(filePath);
      if (json.isBlank()) {
        return new ArrayList<>();
      }
      return gson.fromJson(json, type);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read from file: " + filePath, e);
    }
  }

  @Override
  public Optional<T> findById(UUID id) {
    return findAll().stream()
        .filter(entity -> entity.getId().equals(id))
        .findFirst();
  }

  // Збереження однієї сутності (додає або оновлює)
  @Override
  public void save(T entity) {
    List<T> all = findAll();
    // Видаляємо стару версію якщо є (оновлення)
    all.removeIf(e -> e.getId().equals(entity.getId()));
    all.add(entity);
    saveAll(all);
  }

  @Override
  public void delete(UUID id) {
    List<T> all = findAll();
    all.removeIf(e -> e.getId().equals(id));
    saveAll(all);
  }

  // Перезапис всього файлу
  @Override
  public void saveAll(List<T> entities) {
    try {
      String json = gson.toJson(entities);
      Files.writeString(filePath, json);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write to file: " + filePath, e);
    }
  }
}