package com.carnival.rental.repository;

import com.carnival.rental.entity.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.util.List;

public class OrderRepository extends JsonRepository<Order> {

  public OrderRepository() {
    super("orders.json", new TypeToken<List<Order>>() {
    }.getType());
  }

  // Перевизначаємо створення Gson, щоб він розумів LocalDate
  @Override
  protected Gson createGson() {
    return new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();
  }
}