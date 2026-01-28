package com.carnival.rental.service;

import com.carnival.rental.dto.UserLoginDto;
import com.carnival.rental.dto.UserRegisterDto;
import com.carnival.rental.entity.User;
import com.carnival.rental.infrastructure.EmailService;
import com.carnival.rental.infrastructure.SecurityService;
import com.carnival.rental.presentation.util.ConsoleColors;
import com.carnival.rental.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class AuthService {

  private final UserRepository userRepository;
  private final EmailService emailService;
  private final SecurityService securityService;

  private final Map<String, VerificationContext> pendingRegistrations = new HashMap<>();

  public AuthService() {
    this.userRepository = new UserRepository();
    this.emailService = new EmailService();
    this.securityService = new SecurityService();
  }

  // 1. ПОЧАТОК РЕЄСТРАЦІЇ
  public void startRegistration(UserRegisterDto dto) {
    // Перевірка на дублікат Email
    boolean emailExists = userRepository.findAll().stream()
        .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.email()));

    if (emailExists) {
      throw new IllegalArgumentException("Користувач з таким email вже існує!");
    }

    // ДОДАНО: Перевірка на дублікат Логіна (раз ми тепер входимо по ньому)
    boolean loginExists = userRepository.findAll().stream()
        .anyMatch(u -> u.getUsername().equalsIgnoreCase(dto.username()));

    if (loginExists) {
      throw new IllegalArgumentException("Цей логін вже зайнятий!");
    }

    String hashedPassword = securityService.hashPassword(dto.password());
    User potentialUser = new User(dto.username(), dto.email(), hashedPassword, "CLIENT");

    String code = String.valueOf(1000 + new Random().nextInt(9000));
    pendingRegistrations.put(dto.email(),
        new VerificationContext(potentialUser, code, LocalDateTime.now()));

    System.out.println(ConsoleColors.BLUE + "\n┌──────────────────────────────────────────┐");
    System.out.println("│ ✉️  Вхідне повідомлення (Simulation)      │");
    System.out.println("├──────────────────────────────────────────┤");
    System.out.printf("│ To:   %-34s │%n", dto.email());
    System.out.printf("│ Code: %-34s │%n", ConsoleColors.YELLOW + code + ConsoleColors.BLUE);
    System.out.println("└──────────────────────────────────────────┘" + ConsoleColors.RESET);
  }

  // 2. ПІДТВЕРДЖЕННЯ КОДУ
  public void confirmRegistration(String email, String inputCode) {
    VerificationContext context = pendingRegistrations.get(email);

    if (context == null) {
      throw new IllegalArgumentException("Заявка на реєстрацію не знайдена або час вичерпано.");
    }

    if (context.timestamp.plusMinutes(30).isBefore(LocalDateTime.now())) {
      pendingRegistrations.remove(email);
      throw new IllegalArgumentException("Час дії коду вичерпано. Почніть реєстрацію заново.");
    }

    if (!context.code.equals(inputCode)) {
      throw new IllegalArgumentException("Невірний код підтвердження!");
    }

    userRepository.save(context.user);
    pendingRegistrations.remove(email);

    ConsoleColors.printSuccess(
        "Акаунт успішно створено! Ласкаво просимо, " + context.user.getUsername());
  }

  // ВХІД (ЗМІНЕНО НА USERNAME)
  public User login(UserLoginDto dto) {
    Optional<User> userOpt = userRepository.findAll().stream()
        // Шукаємо по USERNAME замість EMAIL
        .filter(u -> u.getUsername().equalsIgnoreCase(dto.username()))
        .findFirst();

    if (userOpt.isEmpty()) {
      throw new IllegalArgumentException("Користувача з таким логіном не знайдено.");
    }

    User user = userOpt.get();

    if (!securityService.checkPassword(dto.password(), user.getPassword())) {
      throw new IllegalArgumentException("Невірний логін або пароль!");
    }

    ConsoleColors.printSuccess("Вхід успішний! З поверненням, " + user.getUsername());
    return user;
  }

  private static class VerificationContext {

    User user;
    String code;
    LocalDateTime timestamp;

    public VerificationContext(User user, String code, LocalDateTime timestamp) {
      this.user = user;
      this.code = code;
      this.timestamp = timestamp;
    }
  }
}