package com.carnival.rental.presentation;

import com.carnival.rental.entity.User;
import com.carnival.rental.repository.UserRepository;
import java.util.List;
import java.util.Locale;
import net.datafaker.Faker;

public class Main {

  public static void main(String[] args) {
    System.out.println(">>> Розпочинаємо тестування системи...");

    // 1. Ініціалізація репозиторію та генератора даних (Українська локаль)
    UserRepository userRepository = new UserRepository();
    Faker faker = new Faker(new Locale("uk"));

    // 2. Генерація фейкових користувачів
    System.out.println("Генерація випадкових користувачів...");
    for (int i = 0; i < 5; i++) {
      try {
        // Використовуємо реалістичні імена, транслітеруючи в логін, або просто username
        String username = faker.name().username();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        // Випадково призначаємо роль
        String role = (i % 2 == 0) ? "CLIENT" : "ADMIN";

        User user = new User(username, email, password, role);
        userRepository.save(user);
        System.out.println(" + Створено: " + username);

      } catch (Exception e) {
        System.out.println("Помилка генерації користувача: " + e.getMessage());
      }
    }

    // 3. Зчитування даних з файлу
    System.out.println("\n>>> Зчитування з файлу (users.json):");
    List<User> usersFromFile = userRepository.findAll();

    if (usersFromFile.isEmpty()) {
      System.out.println("Файл порожній або не знайдений!");
    } else {
      for (User u : usersFromFile) {
        System.out.println(u);
      }
    }

    System.out.println("\nВсього користувачів у системі: " + usersFromFile.size());
  }
}