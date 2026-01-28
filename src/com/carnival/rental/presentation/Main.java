package com.carnival.rental.presentation;

import com.carnival.rental.dto.ProductDto;
import com.carnival.rental.entity.User;
import com.carnival.rental.presentation.pages.AuthView;
import com.carnival.rental.presentation.pages.ProductView;
import com.carnival.rental.presentation.util.ConsoleColors;
import com.carnival.rental.service.OrderService;
import com.carnival.rental.service.ProductTypeService;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // --- ГЕНЕРАЦІЯ ПОЧАТКОВИХ ДАНИХ ---
    ProductTypeService productService = new ProductTypeService();
    seedData(productService);
    // ----------------------------------

    // 1. Етап авторизації
    AuthView authView = new AuthView(scanner);
    User currentUser = authView.showLoginMenu();

    // 2. Головне меню
    ProductView productView = new ProductView(scanner);

    boolean running = true;
    while (running) {
      ConsoleColors.printHeader("ГОЛОВНЕ МЕНЮ (" + currentUser.getUsername() + ")");
      // ЗМІНЕНО ТЕКСТ:
      System.out.println("1. Каталог костюмів та реквізиту");
      System.out.println("2. Мій профіль (Інфо)");
      System.out.println("0. Вихід");
      System.out.print(ConsoleColors.YELLOW + "Ваш вибір: " + ConsoleColors.RESET);

      String choice = scanner.nextLine();

      switch (choice) {
        case "1" -> productView.showCatalog(currentUser);
        case "2" -> {
          ConsoleColors.printHeader("МІЙ ПРОФІЛЬ");
          System.out.println(
              "Ім'я:    " + ConsoleColors.CYAN + currentUser.getUsername() + ConsoleColors.RESET);
          System.out.println("Email:   " + currentUser.getEmail());

          String roleDisplay = currentUser.getRole().equals("ADMIN")
              ? ConsoleColors.RED + "АДМІНІСТРАТОР" + ConsoleColors.RESET
              : ConsoleColors.GREEN + "Клієнт" + ConsoleColors.RESET;
          System.out.println("Статус:  " + roleDisplay);

          System.out.println(
              "\n" + ConsoleColors.BLUE + "--- Історія Оренд ---" + ConsoleColors.RESET);

          var orderService = new OrderService();
          var orders = orderService.getUserOrders(currentUser.getId());

          if (orders.isEmpty()) {
            System.out.println("У вас поки немає активних замовлень.");
          } else {
            for (var order : orders) {
              String status =
                  order.isActive() ? ConsoleColors.GREEN + "АКТИВНЕ" + ConsoleColors.RESET
                      : "ЗАВЕРШЕНЕ";
              System.out.printf("• %s | %s - %s | %.2f грн | %s%n",
                  order.getProductName(), order.getStartDate(), order.getEndDate(),
                  order.getTotalPrice(), status);
            }
          }

          System.out.println("\nНатисніть Enter, щоб повернутися назад...");
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

  // Метод для наповнення каталогу (тільки якщо він порожній)
  private static void seedData(ProductTypeService service) {
    if (service.getAllProducts().isEmpty()) {
      System.out.println("Генерація початкового каталогу...");

      // Костюми
      service.createProduct(
          new ProductDto("Костюм Людина-Павук", "Класичний костюм Marvel, спандекс", "M", 250.0));
      service.createProduct(
          new ProductDto("Плаття Ельзи (Frozen)", "Блакитне плаття зі шлейфом", "S", 300.0));
      service.createProduct(
          new ProductDto("Мантія Гаррі Поттера", "Грифіндор, з шарфом та емблемою", "L", 200.0));
      service.createProduct(
          new ProductDto("Костюм Пірата", "Капелюх, камзол, пов'язка на око", "XL", 220.0));
      service.createProduct(
          new ProductDto("Костюм Динозавра (Надувний)", "T-Rex, працює від батарейок", "Universal",
              400.0));

      // Реквізит
      service.createProduct(
          new ProductDto("Шолом Дарта Вейдера", "Реквізит, пластик, зі звуком (Star Wars)",
              "OneSize", 150.0));
      service.createProduct(
          new ProductDto("Молот Тора (Мйольнір)", "Важкий реквізит, реалістичний пластик",
              "OneSize", 120.0));
      service.createProduct(
          new ProductDto("Щит Капітана Америка", "Металевий сплав (алюміній), діаметр 60см",
              "OneSize", 180.0));
      service.createProduct(
          new ProductDto("Чарівна паличка", "Дерево, ручна робота, зі світловим ефектом", "OneSize",
              90.0));
      service.createProduct(
          new ProductDto("Світловий меч (Синій)", "Зі звуком та світлом, ударостійкий", "OneSize",
              160.0));

      System.out.println("Каталог успішно наповнено (10 товарів).\n");
    }
  }
}