package com.carnival.rental.presentation.util;

public class ConsoleColors {

  public static final String RESET = "\033[0m";  // Скидання
  public static final String RED = "\033[0;31m";    // Помилки
  public static final String GREEN = "\033[0;32m";  // Успіх
  public static final String YELLOW = "\033[0;33m"; // Попередження/Меню
  public static final String BLUE = "\033[0;34m";   // Інформація
  public static final String PURPLE = "\033[0;35m"; // Заголовки
  public static final String CYAN = "\033[0;36m";   // Введення даних

  public static void printSuccess(String msg) {
    System.out.println(GREEN + "✔ " + msg + RESET);
  }

  public static void printError(String msg) {
    System.out.println(RED + "✘ " + msg + RESET);
  }

  public static void printHeader(String msg) {
    System.out.println(PURPLE + "\n=== " + msg + " ===" + RESET);
  }
}