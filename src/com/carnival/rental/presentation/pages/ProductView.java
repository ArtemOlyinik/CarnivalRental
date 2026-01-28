package com.carnival.rental.presentation.pages;

import com.carnival.rental.dto.ProductDto;
import com.carnival.rental.entity.ProductType;
import com.carnival.rental.entity.User;
import com.carnival.rental.presentation.forms.ProductForm;
import com.carnival.rental.presentation.util.ConsoleColors;
import com.carnival.rental.service.ProductTypeService;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ProductView {

  private final ProductTypeService productService;
  private final ProductForm productForm;
  private final Scanner scanner;

  public ProductView(Scanner scanner) {
    this.scanner = scanner;
    this.productService = new ProductTypeService();
    this.productForm = new ProductForm(scanner);
  }

  public void showCatalog(User user) {
    boolean running = true;
    while (running) {
      ConsoleColors.printHeader("КАТАЛОГ КОСТЮМІВ");

      // 1. Виведення списку
      List<ProductType> products = productService.getAllProducts();
      if (products.isEmpty()) {
        System.out.println(ConsoleColors.YELLOW + "Каталог порожній." + ConsoleColors.RESET);
      } else {
        // Красивий табличний вивід
        System.out.printf("%-36s | %-20s | %-10s | %-10s%n", "ID", "Назва", "Розмір", "Ціна");
        System.out.println("-".repeat(85));
        for (ProductType p : products) {
          System.out.printf("%-36s | %-20s | %-10s | %.2f грн%n",
              p.getId(), p.getName(), p.getSize(), p.getPricePerDay());
        }
      }

      // 2. Меню дій (Адаптивне)
      System.out.println("\n" + ConsoleColors.BLUE + "МЕНЮ:" + ConsoleColors.RESET);
      System.out.println("1. Пошук");

      // Показуємо ці пункти ТІЛЬКИ АДМІНУ
      if ("ADMIN".equals(user.getRole())) {
        System.out.println(ConsoleColors.RED + "ADMIN:" + ConsoleColors.RESET);
        System.out.println("2. Додати товар");
        System.out.println("3. Редагувати товар");
        System.out.println("4. Видалити товар");
      }

      System.out.println("0. Назад у головне меню");
      System.out.print(ConsoleColors.YELLOW + "Ваш вибір: " + ConsoleColors.RESET);

      String choice = scanner.nextLine();

      try {
        switch (choice) {
          case "1" -> search();
          case "2" -> {
            if (checkAdmin(user)) {
              addProduct();
            }
          }
          case "3" -> {
            if (checkAdmin(user)) {
              editProduct();
            }
          }
          case "4" -> {
            if (checkAdmin(user)) {
              deleteProduct();
            }
          }
          case "0" -> running = false;
          default -> ConsoleColors.printError("Невірний вибір.");
        }
      } catch (Exception e) {
        ConsoleColors.printError("Помилка виконання операції: " + e.getMessage());
      }
    }
  }

  // --- Допоміжні методи ---

  private boolean checkAdmin(User user) {
    if (!"ADMIN".equals(user.getRole())) {
      ConsoleColors.printError("У вас немає прав для цієї операції!");
      return false;
    }
    return true;
  }

  private void search() {
    System.out.print("Введіть пошуковий запит: ");
    String query = scanner.nextLine();
    List<ProductType> results = productService.searchProducts(query);
    System.out.println("Знайдено товарів: " + results.size());
    for (ProductType p : results) {
      System.out.println(" - " + p.getName() + " (" + p.getPricePerDay() + " грн)");
    }
    System.out.println("Натисніть Enter щоб продовжити...");
    scanner.nextLine();
  }

  private void addProduct() {
    ProductDto dto = productForm.createProduct();
    productService.createProduct(dto);
    ConsoleColors.printSuccess("Товар успішно додано!");
  }

  private void editProduct() {
    System.out.print("Введіть ID товару для редагування: ");
    String idStr = scanner.nextLine();
    // Тут ми для спрощення просимо ввести нові дані повністю
    System.out.println("(Введіть нові дані)");
    ProductDto dto = productForm.createProduct();
    productService.updateProduct(UUID.fromString(idStr), dto);
    ConsoleColors.printSuccess("Товар оновлено!");
  }

  private void deleteProduct() {
    System.out.print("Введіть ID товару для видалення: ");
    String idStr = scanner.nextLine();
    productService.deleteProduct(UUID.fromString(idStr));
    ConsoleColors.printSuccess("Товар видалено!");
  }
}