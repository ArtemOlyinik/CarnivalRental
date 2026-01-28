package com.carnival.rental.presentation.forms;

import com.carnival.rental.dto.ProductDto;
import com.carnival.rental.presentation.util.ConsoleColors;
import java.util.Scanner;

public class ProductForm {

  private final Scanner scanner;

  public ProductForm(Scanner scanner) {
    this.scanner = scanner;
  }

  public ProductDto createProduct() {
    ConsoleColors.printHeader("ДОДАВАННЯ ТОВАРУ");

    System.out.print("Назва костюма: " + ConsoleColors.CYAN);
    String name = scanner.nextLine();

    System.out.print(ConsoleColors.RESET + "Опис: " + ConsoleColors.CYAN);
    String desc = scanner.nextLine();

    System.out.print(ConsoleColors.RESET + "Розмір (S/M/L): " + ConsoleColors.CYAN);
    String size = scanner.nextLine();

    double price = 0;
    while (true) {
      System.out.print(ConsoleColors.RESET + "Ціна за добу: " + ConsoleColors.CYAN);
      try {
        price = Double.parseDouble(scanner.nextLine());
        break;
      } catch (NumberFormatException e) {
        ConsoleColors.printError("Будь ласка, введіть число!");
      }
    }
    System.out.print(ConsoleColors.RESET);

    return new ProductDto(name, desc, size, price);
  }
}