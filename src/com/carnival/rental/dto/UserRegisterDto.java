package com.carnival.rental.dto;

public record UserRegisterDto(String username, String email, String password) {

  public UserRegisterDto {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Логін не може бути порожнім");
    }
    if (email == null || !email.contains("@")) {
      throw new IllegalArgumentException("Некоректний формат email");
    }
    if (password == null || password.length() < 4) {
      throw new IllegalArgumentException("Пароль має бути не менше 4 символів");
    }
  }
}