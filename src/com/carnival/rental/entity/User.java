package com.carnival.rental.entity;

public class User extends BaseEntity {

  private String username;
  private String email;
  private String password;
  private String role; // "ADMIN" або "CLIENT"

  public User(String username, String email, String password, String role) {
    // Валідація вхідних даних
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    if (email == null || !email.contains("@")) {
      throw new IllegalArgumentException("Invalid email format");
    }
    if (role == null || role.isBlank()) {
      throw new IllegalArgumentException("Role cannot be empty");
    }

    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  // Геттери
  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }

  // Сеттери (за потреби теж можна додати валідацію)
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return "User{" + super.toString() + ", username='" + username + "', role='" + role + "'}";
  }
}