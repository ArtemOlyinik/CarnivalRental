package com.carnival.rental.service;

import com.carnival.rental.entity.ProductType;
import com.carnival.rental.repository.ProductTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductTypeService {

  private final ProductTypeRepository repository;

  public ProductTypeService() {
    this.repository = new ProductTypeRepository();
  }

  // 1. CREATE
  public void createProduct(String name, String description, String size, double price) {
    ProductType product = new ProductType(name, description, size, price);
    repository.save(product);
    System.out.println("SERVICE: Товар створено -> " + name);
  }

  // 2. READ ALL
  public List<ProductType> getAllProducts() {
    return repository.findAll();
  }

  // 3. READ ONE
  public Optional<ProductType> getProductById(UUID id) {
    return repository.findById(id);
  }

  // 4. UPDATE
  public void updateProduct(UUID id, String newName, String newDesc, double newPrice) {
    Optional<ProductType> productOpt = repository.findById(id);
    if (productOpt.isPresent()) {
      ProductType product = productOpt.get();
      product.setName(newName);
      product.setDescription(newDesc);
      product.setPricePerDay(newPrice);

      repository.save(product);
      System.out.println("SERVICE: Товар оновлено.");
    } else {
      System.out.println("SERVICE Помилка: Товар не знайдено.");
    }
  }

  // 5. DELETE
  public void deleteProduct(UUID id) {
    repository.delete(id);
    System.out.println("SERVICE: Товар видалено.");
  }

  // 6. SEARCH (Пошук за назвою або описом)
  public List<ProductType> searchProducts(String query) {
    if (query == null || query.isBlank()) {
      return getAllProducts();
    }

    String lowerQuery = query.toLowerCase();
    return repository.findAll().stream()
        .filter(p -> p.getName().toLowerCase().contains(lowerQuery) ||
            (p.getDescription() != null && p.getDescription().toLowerCase().contains(lowerQuery)))
        .collect(Collectors.toList());
  }
}