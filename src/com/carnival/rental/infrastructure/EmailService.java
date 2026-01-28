package com.carnival.rental.infrastructure;

public class EmailService {

  public void sendWelcomeEmail(String toEmail, String username) {
    // Імітація відправки
    System.out.println("\n[EMAIL SYSTEM] Sending to: " + toEmail);
    System.out.println("Subject: Ласкаво просимо до Carnival Rental!");
    System.out.println("Body: Вітаємо, " + username + "! Ви успішно зареєструвалися.");
    System.out.println("[EMAIL SENT SUCCESSFULLY]\n");
  }
}