package com.carnival.rental.dto;

public record ProductDto(String name, String description, String size, double price) {

  public ProductDto {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Назва товару обов'язкова");
    }
    if (price < 0) {
      throw new IllegalArgumentException("Ціна не може бути від'ємною");
    }
  }
}