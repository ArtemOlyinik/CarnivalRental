package com.carnival.rental.infrastructure;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityService {

  // Хешування пароля (для реєстрації)
  public String hashPassword(String plainPassword) {
    // gensalt() генерує випадкову "сіль", щоб навіть однакові паролі мали різний хеш
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
  }

  // Перевірка пароля (для входу)
  public boolean checkPassword(String plainPassword, String hashedPassword) {
    try {
      return BCrypt.checkpw(plainPassword, hashedPassword);
    } catch (IllegalArgumentException e) {
      // Якщо раптом у базі старий незахешований пароль - повертаємо false
      return false;
    }
  }
}