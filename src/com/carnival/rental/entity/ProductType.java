package com.carnival.rental.entity;

public class ProductType extends BaseEntity {

  private String name;
  private String description;
  private String size; // S, M, L, XL
  private double pricePerDay;

  public ProductType(String name, String description, String size, double pricePerDay) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Product name cannot be empty");
    }
    if (pricePerDay < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }

    this.name = name;
    this.description = description;
    this.size = size;
    this.pricePerDay = pricePerDay;
  }

  public String getName() {
    return name;
  }

  public double getPricePerDay() {
    return pricePerDay;
  }

  @Override
  public String toString() {
    return "ProductType{" + super.toString() + ", name='" + name + "', price=" + pricePerDay + "}";
  }
}