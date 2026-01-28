package com.carnival.rental.service;

import com.carnival.rental.dto.ProductDto;
import com.carnival.rental.entity.ProductType;
import com.carnival.rental.presentation.util.ConsoleColors;
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

  public void createProduct(ProductDto dto) {
    ProductType product = new ProductType(dto.name(), dto.description(), dto.size(), dto.price());
    repository.save(product);
    ConsoleColors.printSuccess("Товар успішно додано до каталогу: " + dto.name());
  }

  public List<ProductType> getAllProducts() {
    return repository.findAll();
  }

  public Optional<ProductType> getProductById(UUID id) {
    return repository.findById(id);
  }

  public void updateProduct(UUID id, ProductDto dto) {
    Optional<ProductType> productOpt = repository.findById(id);
    if (productOpt.isPresent()) {
      ProductType product = productOpt.get();
      product.setName(dto.name());
      product.setDescription(dto.description());
      product.setSize(dto.size());
      product.setPricePerDay(dto.price());

      repository.save(product);
      ConsoleColors.printSuccess("Дані про товар оновлено.");
    } else {
      ConsoleColors.printError("Помилка: Товар з таким ID не знайдено.");
    }
  }

  public void deleteProduct(UUID id) {
    repository.delete(id);
    ConsoleColors.printSuccess("Товар успішно видалено з каталогу.");
  }

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