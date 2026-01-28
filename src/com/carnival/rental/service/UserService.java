package com.carnival.rental.service;

import com.carnival.rental.entity.User;
import com.carnival.rental.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserService {

  private final UserRepository userRepository;

  public UserService() {
    this.userRepository = new UserRepository();
  }

  // 1. CREATE (Створення)
  public void createUser(String username, String email, String password, String role) {
    // Тут можна додати додаткову бізнес-логіку (наприклад, чи не зайнятий email)
    User user = new User(username, email, password, role);
    userRepository.save(user);
    System.out.println("Сервіс: Користувача збережено -> " + username);
  }

  // 2. READ (Читання всіх)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // 2.1 READ ONE (Пошук по ID)
  public Optional<User> getUserById(UUID id) {
    return userRepository.findById(id);
  }

  // 3. UPDATE (Оновлення)
  public void updateUser(UUID id, String newEmail, String newRole) {
    Optional<User> userOpt = userRepository.findById(id);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      // Оновлюємо поля (в реальному проєкті краще через сеттери, але у нас їх мало, тому так)
      // Оскільки User у нас майже immutable (без сеттерів крім role),
      // ми створюємо оновлену копію або додаємо сеттери в User.
      // ДАВАЙ ДОДАМО СЕТТЕРИ в клас User.java, це простіше.

      // user.setEmail(newEmail); // Треба додати цей метод в Entity
      user.setRole(newRole);

      userRepository.save(user); // Перезаписуємо
      System.out.println("Сервіс: Користувача оновлено.");
    } else {
      System.out.println("Сервіс: Помилка. Користувача з таким ID не знайдено.");
    }
  }

  // 4. DELETE (Видалення)
  public void deleteUser(UUID id) {
    userRepository.delete(id);
    System.out.println("Сервіс: Користувача видалено.");
  }

  // 5. SEARCH (Пошук/Фільтрація)
  public List<User> searchUsers(String query) {
    String lowerQuery = query.toLowerCase();
    return userRepository.findAll().stream()
        .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) ||
            u.getEmail().toLowerCase().contains(lowerQuery))
        .collect(Collectors.toList());
  }
}