package com.carnival.rental.service;

import com.carnival.rental.entity.Order;
import com.carnival.rental.entity.ProductType;
import com.carnival.rental.entity.User;
import com.carnival.rental.presentation.util.ConsoleColors;
import com.carnival.rental.repository.OrderRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService() {
    this.orderRepository = new OrderRepository();
  }

  // Створення замовлення з перевіркою доступності
  public void createOrder(User user, ProductType product, LocalDate start, LocalDate end) {
    // 1. Перевірка дат
    if (!isProductAvailable(product.getId(), start, end)) {
      throw new IllegalArgumentException("На жаль, цей костюм вже зайнятий у вибрані дати.");
    }

    // 2. Створення
    Order order = new Order(user.getId(), product.getId(), product.getName(), start, end,
        product.getPricePerDay());

    // 3. Збереження
    orderRepository.save(order);
    ConsoleColors.printSuccess(
        "Замовлення успішно оформлено! Сума до сплати: " + order.getTotalPrice() + " грн");
  }

  // Перевірка перетину дат
  public boolean isProductAvailable(UUID productId, LocalDate start, LocalDate end) {
    List<Order> orders = orderRepository.findAll();

    for (Order order : orders) {
      // Перевіряємо тільки активні замовлення для цього товару
      if (order.isActive() && order.getProductId().equals(productId)) {
        // Логіка перетину інтервалів:
        // (StartA <= EndB) and (EndA >= StartB)
        boolean overlap = !start.isAfter(order.getEndDate()) && !end.isBefore(order.getStartDate());

        if (overlap) {
          return false; // Зайнято
        }
      }
    }
    return true;
  }

  // Отримати історію замовлень користувача
  public List<Order> getUserOrders(UUID userId) {
    return orderRepository.findAll().stream()
        .filter(o -> o.getUserId().equals(userId))
        .collect(Collectors.toList());
  }
}