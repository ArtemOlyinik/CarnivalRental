package com.carnival.rental.entity;

public class User extends BaseEntity {

  private String username;
  private String email;
  private String password;
  private String role;

  public User(String username, String email, String password, String role) {
    // Валідація (та сама, що була)
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

  //СЕТТЕРИ
  public void setEmail(String email) {
    if (email == null || !email.contains("@")) {
      throw new IllegalArgumentException("Invalid email format");
    }
    this.email = email;
  }

  public void setRole(String role) {
    if (role == null || role.isBlank()) {
      throw new IllegalArgumentException("Role cannot be empty");
    }
    this.role = role;
  }
  // ----------------------------------

  @Override
  public String toString() {
    return "User{" + super.toString() + ", username='" + username + "', email='" + email
        + "', role='" + role + "'}";
  }
}