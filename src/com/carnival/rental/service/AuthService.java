package com.carnival.rental.service;

import com.carnival.rental.dto.UserLoginDto;
import com.carnival.rental.dto.UserRegisterDto;
import com.carnival.rental.entity.User;
import com.carnival.rental.infrastructure.EmailService;
import com.carnival.rental.repository.UserRepository;
import java.util.Optional;

public class AuthService {

  private final UserRepository userRepository;
  private final EmailService emailService;

  public AuthService() {
    this.userRepository = new UserRepository();
    this.emailService = new EmailService();
  }

  // РЕЄСТРАЦІЯ
  public void register(UserRegisterDto dto) {
    boolean emailExists = userRepository.findAll().stream()
        .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.email()));

    if (emailExists) {
      throw new IllegalArgumentException("Користувач з таким email вже існує!");
    }

    User newUser = new User(dto.username(), dto.email(), dto.password(), "CLIENT");
    userRepository.save(newUser);

    emailService.sendWelcomeEmail(dto.email(), dto.username());
    System.out.println("AUTH: Реєстрація успішна для користувача " + dto.username());
  }

  // ВХІД
  public User login(UserLoginDto dto) {
    Optional<User> userOpt = userRepository.findAll().stream()
        .filter(u -> u.getEmail().equalsIgnoreCase(dto.email()))
        .findFirst();

    if (userOpt.isEmpty()) {
      throw new IllegalArgumentException("Користувача з таким email не знайдено.");
    }

    User user = userOpt.get();

    // Тепер метод getPassword() доступний, і помилки не буде
    if (!user.getPassword().equals(dto.password())) {
      throw new IllegalArgumentException("Невірний пароль!");
    }

    System.out.println("AUTH: Вхід успішний. Вітаємо, " + user.getUsername());
    return user;
  }
}