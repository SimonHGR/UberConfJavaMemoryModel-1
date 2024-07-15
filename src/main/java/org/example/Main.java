package org.example;

record Message(String msg) {}

public class Main {
  public static void main(String[] args) {
    Object thing = new Message("Java 21");

    System.out.println(switch (thing) {
      case Message(String m) -> "Hello " + m + " world!";
      default -> throw new UnsupportedOperationException("Bad!");
    });
  }
}
