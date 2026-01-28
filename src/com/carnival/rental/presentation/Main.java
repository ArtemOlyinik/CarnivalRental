package com.carnival.rental.presentation;

import com.carnival.rental.entity.User;
import com.carnival.rental.presentation.pages.AuthView;
import com.carnival.rental.presentation.pages.ProductView;
import com.carnival.rental.presentation.util.ConsoleColors;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    // Єдиний сканер на всю програму (щоб не закривати System.in)
    Scanner scanner = new Scanner(System.in);

    // 1. Етап авторизації
    AuthView authView = new AuthView(scanner);
    User currentUser = authView.showLoginMenu();

    // 2. Головне меню програми
    ProductView productView = new ProductView(scanner);

    boolean running = true;
    while (running) {
      ConsoleColors.printHeader("ГОЛОВНЕ МЕНЮ (" + currentUser.getUsername() + ")");
      System.out.println("1. Каталог костюмів");
      System.out.println("2. Мій профіль (Інфо)");
      System.out.println("0. Вихід");
      System.out.print(ConsoleColors.YELLOW + "Ваш вибір: " + ConsoleColors.RESET);

      String choice = scanner.nextLine();

      switch (choice) {
        case "1" -> productView.showCatalog(currentUser);
        case "2" -> {
          System.out.println("Ваш ID: " + currentUser.getId());
          System.out.println("Email: " + currentUser.getEmail());
          System.out.println("Роль: " + currentUser.getRole());
          System.out.println("\nНатисніть Enter...");
          scanner.nextLine();
        }
        case "0" -> {
          System.out.println("До побачення!");
          running = false;
        }
        default -> ConsoleColors.printError("Невірний вибір.");
      }
    }
  }
}