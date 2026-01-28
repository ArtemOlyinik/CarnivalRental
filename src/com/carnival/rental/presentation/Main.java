package com.carnival.rental.presentation;

import com.carnival.rental.dto.ProductDto;
import com.carnival.rental.dto.UserLoginDto;
import com.carnival.rental.dto.UserRegisterDto;
import com.carnival.rental.entity.User;
import com.carnival.rental.service.AuthService;
import com.carnival.rental.service.ProductTypeService;

public class Main {

  public static void main(String[] args) {
    System.out.println(">>> ТЕСТУВАННЯ ЕТАПУ 4 (Auth & DTO) <<<");

    AuthService authService = new AuthService();
    ProductTypeService productService = new ProductTypeService();

    try {
      // 1. Тест Реєстрації (має прийти "лист" в консоль)
      System.out.println("\n--- 1. РЕЄСТРАЦІЯ ---");
      UserRegisterDto regDto = new UserRegisterDto("novachok", "new.user@gmail.com",
          "securePass123");
      authService.register(regDto);

      // 2. Тест Входу
      System.out.println("\n--- 2. ВХІД (Login) ---");
      UserLoginDto loginDto = new UserLoginDto("new.user@gmail.com", "securePass123");
      User loggedUser = authService.login(loginDto);
      System.out.println("Поточний користувач: " + loggedUser.getRole());

      // 3. Тест Товарів через DTO
      System.out.println("\n--- 3. СТВОРЕННЯ ТОВАРУ ЧЕРЕЗ DTO ---");
      ProductDto prodDto = new ProductDto("Маска Бетмена", "Пластикова маска", "Universal", 150.0);
      productService.createProduct(prodDto);

    } catch (Exception e) {
      System.out.println("ПОМИЛКА: " + e.getMessage());
    }
  }
}