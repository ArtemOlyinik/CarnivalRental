package com.carnival.rental.entity;

public class ProductType extends BaseEntity {

  private String name;
  private String description;
  private String size; // S, M, L, XL
  private double pricePerDay;

  public ProductType(String name, String description, String size, double pricePerDay) {
    validate(name, pricePerDay);
    this.name = name;
    this.description = description;
    this.size = size;
    this.pricePerDay = pricePerDay;
  }

  // Виніс валідацію в окремий метод, щоб використовувати і в конструкторі, і в сеттерах
  private void validate(String name, double price) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Назва товару не може бути порожньою");
    }
    if (price < 0) {
      throw new IllegalArgumentException("Ціна не може бути від'ємною");
    }
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getSize() {
    return size;
  }

  public double getPricePerDay() {
    return pricePerDay;
  }

  // --- СЕТТЕРИ (для Update) ---
  public void setName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Назва не може бути порожньою");
    }
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public void setPricePerDay(double pricePerDay) {
    if (pricePerDay < 0) {
      throw new IllegalArgumentException("Ціна не може бути від'ємною");
    }
    this.pricePerDay = pricePerDay;
  }
  // ----------------------------

  @Override
  public String toString() {
    return "Костюм: " + name + " (" + size + ") - " + pricePerDay + " грн/доба";
  }
}