package com.carnival.rental.presentation.forms;

import com.carnival.rental.dto.UserLoginDto;
import com.carnival.rental.dto.UserRegisterDto;
import com.carnival.rental.presentation.util.ConsoleColors;
import java.util.Scanner;

public class UserForm {

  private final Scanner scanner;

  public UserForm(Scanner scanner) {
    this.scanner = scanner;
  }

  public UserRegisterDto register() {
    ConsoleColors.printHeader("ФОРМА РЕЄСТРАЦІЇ");

    System.out.print("Введіть логін: " + ConsoleColors.CYAN);
    String username = scanner.nextLine();

    System.out.print(ConsoleColors.RESET + "Введіть email: " + ConsoleColors.CYAN);
    String email = scanner.nextLine();

    System.out.print(ConsoleColors.RESET + "Введіть пароль: " + ConsoleColors.CYAN);
    String password = scanner.nextLine();
    System.out.print(ConsoleColors.RESET);

    return new UserRegisterDto(username, email, password);
  }

  public UserLoginDto login() {
    ConsoleColors.printHeader("ФОРМА ВХОДУ");

    System.out.print("Введіть email: " + ConsoleColors.CYAN);
    String email = scanner.nextLine();

    System.out.print(ConsoleColors.RESET + "Введіть пароль: " + ConsoleColors.CYAN);
    String password = scanner.nextLine();
    System.out.print(ConsoleColors.RESET);

    return new UserLoginDto(email, password);
  }
}