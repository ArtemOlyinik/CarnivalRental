package com.carnival.rental.dto;

public record UserLoginDto(String username, String password) {

  public UserLoginDto {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Логін обов'язковий для входу");
    }
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Пароль обов'язковий");
    }
  }
}