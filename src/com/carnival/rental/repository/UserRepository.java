package com.carnival.rental.repository;

import com.carnival.rental.entity.User;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class UserRepository extends JsonRepository<User> {

  public UserRepository() {
    // Вказуємо назву файлу "users.json" і тип List<User>
    super("users.json", new TypeToken<List<User>>() {
    }.getType());
  }
}