package com.carnival.rental.presentation.pages;

import com.carnival.rental.dto.UserLoginDto;
import com.carnival.rental.dto.UserRegisterDto;
import com.carnival.rental.entity.User;
import com.carnival.rental.presentation.forms.UserForm;
import com.carnival.rental.presentation.util.ConsoleColors;
import com.carnival.rental.service.AuthService;
import java.util.Scanner;

public class AuthView {

  private final AuthService authService;
  private final UserForm userForm;
  private final Scanner scanner;

  public AuthView(Scanner scanner) {
    this.scanner = scanner;
    this.authService = new AuthService();
    this.userForm = new UserForm(scanner);
  }

  public User showLoginMenu() {
    while (true) {
      System.out.println(
          "\n" + ConsoleColors.PURPLE + "=== ВІТАЄМО У CARNIVAL RENTAL ===" + ConsoleColors.RESET);
      System.out.println("1. Вхід");
      System.out.println("2. Реєстрація");
      System.out.println("0. Вихід");
      System.out.print(ConsoleColors.YELLOW + "Ваш вибір: " + ConsoleColors.RESET);

      String choice = scanner.nextLine();

      try {
        switch (choice) {
          case "1" -> {
            UserLoginDto loginDto = userForm.login();
            return authService.login(loginDto);
          }
          case "2" -> handleRegistration(); // Винесли логіку в окремий метод для чистоти
          case "0" -> {
            System.out.println("До побачення!");
            System.exit(0);
          }
          default -> ConsoleColors.printError("Невірний вибір, спробуйте ще раз.");
        }
      } catch (Exception e) {
        ConsoleColors.printError("Помилка: " + e.getMessage());
      }
    }
  }

  // Логіка реєстрації з 3 спробами
  private void handleRegistration() {
    // 1. Зчитуємо дані
    UserRegisterDto registerDto = userForm.register();

    // 2. Відправляємо код
    try {
      authService.startRegistration(registerDto);
    } catch (Exception e) {
      ConsoleColors.printError(e.getMessage());
      return; // Якщо помилка тут (наприклад, email зайнятий), то виходимо одразу
    }

    // 3. Цикл підтвердження (3 спроби)
    int attempts = 3;
    boolean success = false;

    while (attempts > 0) {
      System.out.print(
          ConsoleColors.YELLOW + "Введіть код з Email (залишилось спроб: " + attempts + "): "
              + ConsoleColors.RESET);
      String code = scanner.nextLine();

      try {
        authService.confirmRegistration(registerDto.email(), code);
        success = true;
        break; // Успіх! Виходимо з циклу
      } catch (Exception e) {
        attempts--;
        ConsoleColors.printError(e.getMessage()); // "Невірний код"
        if (attempts > 0) {
          System.out.println("Спробуйте ще раз.");
        }
      }
    }

    if (!success) {
      ConsoleColors.printError("Вичерпано ліміт спроб. Реєстрацію скасовано.");
    }
  }
}