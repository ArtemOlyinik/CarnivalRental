package com.carnival.rental.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Order extends BaseEntity {

  private UUID userId;
  private UUID productId;
  private String productName; // Зберігаємо назву, щоб не шукати товар, якщо його видалять
  private LocalDate startDate;
  private LocalDate endDate;
  private double totalPrice;
  private boolean active; // true = в оренді, false = повернуто

  public Order(UUID userId, UUID productId, String productName, LocalDate startDate,
      LocalDate endDate, double pricePerDay) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Дата початку не може бути пізніше дати завершення");
    }
    if (startDate.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Не можна бронювати в минулому");
    }

    this.userId = userId;
    this.productId = productId;
    this.productName = productName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.active = true;

    // Розрахунок ціни
    long days = ChronoUnit.DAYS.between(startDate, endDate);
    if (days == 0) {
      days = 1; // Мінімум 1 день
    }
    this.totalPrice = days * pricePerDay;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public boolean isActive() {
    return active;
  }

  public void closeOrder() {
    this.active = false;
  }

  @Override
  public String toString() {
    return String.format("Замовлення: %s | Дати: %s - %s | Сума: %.2f | Статус: %s",
        productName, startDate, endDate, totalPrice, (active ? "Активне" : "Завершене"));
  }
}