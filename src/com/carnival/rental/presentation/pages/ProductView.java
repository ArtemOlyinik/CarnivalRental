package com.carnival.rental.presentation.pages;

import com.carnival.rental.dto.ProductDto;
import com.carnival.rental.entity.ProductType;
import com.carnival.rental.entity.User;
import com.carnival.rental.presentation.forms.ProductForm;
import com.carnival.rental.presentation.util.ConsoleColors;
import com.carnival.rental.service.OrderService;
import com.carnival.rental.service.ProductTypeService;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ProductView {

  private final ProductTypeService productService;
  private final OrderService orderService;
  private final ProductForm productForm;
  private final Scanner scanner;

  public ProductView(Scanner scanner) {
    this.scanner = scanner;
    this.productService = new ProductTypeService();
    this.orderService = new OrderService();
    this.productForm = new ProductForm(scanner);
  }

  public void showCatalog(User user) {
    boolean running = true;
    while (running) {
      // ЗМІНЕНО: Заголовок
      ConsoleColors.printHeader("КАТАЛОГ КОСТЮМІВ ТА РЕКВІЗИТУ");

      List<ProductType> products = productService.getAllProducts();

      if (products.isEmpty()) {
        System.out.println(ConsoleColors.YELLOW + "Каталог порожній." + ConsoleColors.RESET);
      } else {
        // ЗМІНЕНО: Форматування таблиці (розширено другу колонку до 38 символів)
        // %-4s (№) | %-38s (Назва) | %-10s (Розмір) | %-10s (Ціна)
        System.out.printf("%-4s | %-38s | %-10s | %-10s%n", "№", "Назва", "Розмір", "Ціна");
        // Лінія розділювача підігнана під нову ширину (4+3+38+3+10+3+10 = ~71)
        System.out.println("-".repeat(72));

        for (int i = 0; i < products.size(); i++) {
          ProductType p = products.get(i);
          System.out.printf("%-4d | %-38s | %-10s | %.2f грн%n",
              (i + 1), p.getName(), p.getSize(), p.getPricePerDay());
        }
      }

      System.out.println("\n" + ConsoleColors.BLUE + "ДІЇ:" + ConsoleColors.RESET);

      if ("CLIENT".equals(user.getRole())) {
        System.out.println("1. Орендувати костюм або реквізит");
        System.out.println("2. Пошук");
      } else {
        System.out.println("1. Пошук");
        System.out.println(ConsoleColors.RED + "ADMIN:" + ConsoleColors.RESET);
        System.out.println("2. Додати товар");
        System.out.println("3. Редагувати товар");
        System.out.println("4. Видалити товар");
      }

      System.out.println("0. Назад");
      System.out.print(ConsoleColors.YELLOW + "Ваш вибір: " + ConsoleColors.RESET);

      String choice = scanner.nextLine();

      try {
        if ("CLIENT".equals(user.getRole())) {
          switch (choice) {
            case "1" -> rentProduct(user, products);
            case "2" -> search();
            case "0" -> running = false;
            default -> ConsoleColors.printError("Невірний вибір.");
          }
        } else {
          switch (choice) {
            case "1" -> search();
            case "2" -> addProduct();
            case "3" -> editProduct(products);
            case "4" -> deleteProduct(products);
            case "0" -> running = false;
            default -> ConsoleColors.printError("Невірний вибір.");
          }
        }
      } catch (Exception e) {
        ConsoleColors.printError("Помилка: " + e.getMessage());
      }
    }
  }

  private void rentProduct(User user, List<ProductType> products) {
    System.out.print("Введіть номер товару (№): ");
    try {
      int index = Integer.parseInt(scanner.nextLine()) - 1;

      if (index < 0 || index >= products.size()) {
        ConsoleColors.printError("Невірний номер товару!");
        return;
      }

      ProductType selectedProduct = products.get(index);
      System.out.println(
          "Ви обрали: " + ConsoleColors.CYAN + selectedProduct.getName() + ConsoleColors.RESET);

      boolean dateValid = false;
      while (!dateValid) {
        try {
          System.out.println("\nВведіть дати оренди (формат РРРР-ММ-ДД, наприклад 2026-05-20):");

          System.out.print("Дата початку: ");
          LocalDate start = LocalDate.parse(scanner.nextLine());

          System.out.print("Дата повернення: ");
          LocalDate end = LocalDate.parse(scanner.nextLine());

          orderService.createOrder(user, selectedProduct, start, end);
          dateValid = true;

          System.out.println("Натисніть Enter...");
          scanner.nextLine();

        } catch (NumberFormatException e) {
          ConsoleColors.printError("Це не схоже на дату.");
        } catch (DateTimeParseException e) {
          ConsoleColors.printError("Невірний формат дати! Спробуйте ще раз (РРРР-ММ-ДД).");
        } catch (IllegalArgumentException e) {
          ConsoleColors.printError(e.getMessage() + " Спробуйте інші дати.");
        }
      }

    } catch (NumberFormatException e) {
      ConsoleColors.printError("Введіть число (номер товару)!");
    }
  }

  private void search() {
    System.out.print("Введіть запит: ");
    String query = scanner.nextLine();
    List<ProductType> results = productService.searchProducts(query);
    System.out.println("Знайдено: " + results.size());
    results.forEach(p -> System.out.println("- " + p.getName() + " (" + p.getSize() + ")"));
    System.out.println("Enter...");
    scanner.nextLine();
  }

  private void addProduct() {
    ProductDto dto = productForm.createProduct();
    productService.createProduct(dto);
  }

  private void editProduct(List<ProductType> products) {
    System.out.print("Введіть номер товару для редагування: ");
    int index = Integer.parseInt(scanner.nextLine()) - 1;
    if (isValidIndex(index, products)) {
      ProductType p = products.get(index);
      System.out.println("Редагування: " + p.getName());
      ProductDto dto = productForm.createProduct();
      productService.updateProduct(p.getId(), dto);
    }
  }

  private void deleteProduct(List<ProductType> products) {
    System.out.print("Введіть номер товару для видалення: ");
    int index = Integer.parseInt(scanner.nextLine()) - 1;
    if (isValidIndex(index, products)) {
      productService.deleteProduct(products.get(index).getId());
    }
  }

  private boolean isValidIndex(int index, List<ProductType> products) {
    if (index < 0 || index >= products.size()) {
      ConsoleColors.printError("Невірний номер!");
      return false;
    }
    return true;
  }
}