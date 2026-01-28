package com.carnival.rental.presentation;

import com.carnival.rental.entity.ProductType;
import com.carnival.rental.entity.User;
import com.carnival.rental.service.ProductTypeService;
import com.carnival.rental.service.UserService;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.datafaker.Faker;

public class Main {

  public static void main(String[] args) {
    System.out.println("========== ТЕСТУВАННЯ БІЗНЕС-ЛОГІКИ (SERVICE LAYER) ==========");

    // 1. Ініціалізація сервісів
    UserService userService = new UserService();
    ProductTypeService productService = new ProductTypeService();
    Faker faker = new Faker(new Locale("uk"));

    // --- БЛОК 1: КОРИСТУВАЧІ ---
    System.out.println("\n>>> 1. СТВОРЕННЯ КОРИСТУВАЧА (Create)");
    String newLogin = faker.name().username();
    userService.createUser(newLogin, "test.email@example.com", "12345", "CLIENT");

    // Знаходимо його, щоб отримати ID
    User createdUser = userService.searchUsers(newLogin).get(0);
    UUID userId = createdUser.getId();

    System.out.println(">>> 2. РЕДАГУВАННЯ (Update)");
    // Змінюємо роль на ADMIN
    userService.updateUser(userId, "new.email@example.com", "ADMIN");

    System.out.println(">>> 3. ПОШУК (Search)");
    List<User> searchResult = userService.searchUsers(newLogin);
    searchResult.forEach(u -> System.out.println("Знайдено: " + u));

    // --- БЛОК 2: ТОВАРИ ---
    System.out.println("\n>>> 4. СТВОРЕННЯ ТОВАРІВ (Create Product)");
    // Генеруємо 3 випадкові костюми
    for (int i = 0; i < 3; i++) {
      String costumeName = "Костюм " + faker.superhero().name();
      productService.createProduct(
          costumeName,
          "Розмір " + faker.options().option("S", "M", "L"),
          "M",
          faker.number().numberBetween(100, 1000)
      );
    }

    System.out.println(">>> 5. ОТРИМАННЯ ВСІХ ТОВАРІВ (Read All)");
    List<ProductType> allProducts = productService.getAllProducts();
    System.out.println("Всього товарів у базі: " + allProducts.size());

    if (!allProducts.isEmpty()) {
      ProductType firstProduct = allProducts.get(0);
      System.out.println("Приклад товару: " + firstProduct);

      System.out.println(">>> 6. ОНОВЛЕННЯ ЦІНИ (Update)");
      productService.updateProduct(firstProduct.getId(), firstProduct.getName(),
          "Опис змінено адміністратором", 9999.0);

      System.out.println(">>> 7. ВИДАЛЕННЯ (Delete)");
      // Видаляємо останній доданий товар для тесту
      ProductType lastProduct = allProducts.get(allProducts.size() - 1);
      productService.deleteProduct(lastProduct.getId());
    }

    System.out.println("\n========== ТЕСТУВАННЯ ЗАВЕРШЕНО ==========");
  }
}