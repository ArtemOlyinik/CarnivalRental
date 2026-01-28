package com.carnival.rental.repository;

import com.carnival.rental.entity.ProductType;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class ProductTypeRepository extends JsonRepository<ProductType> {

  public ProductTypeRepository() {
    // Зберігаємо у файл "products.json"
    super("products.json", new TypeToken<List<ProductType>>() {
    }.getType());
  }
}