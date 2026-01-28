package com.carnival.rental.dto;

public record UserLoginDto(String email, String password) {

  public UserLoginDto {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email обов'язковий для входу");
    }
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Пароль обов'язковий");
    }
  }
}