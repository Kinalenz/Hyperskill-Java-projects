package calculator;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LinkedHashMap<String, Double> earned = new LinkedHashMap<>();
        earned.put("Bubblegum", 202.0);
        earned.put("Toffee", 118.0);
        earned.put("Ice cream", 2250.0);
        earned.put("Milk chocolate", 1680.0);
        earned.put("Doughnut", 1075.0);
        earned.put("Pancake", 80.0);

        double income = earned.values().stream().mapToDouble(Double::doubleValue).sum();

        System.out.println("Earned amount:");
        earned.forEach((key, value) -> System.out.printf("%s: $%.1f%n", key, value));
        System.out.println();

        System.out.printf("Income: $%.1f%n", income);
        System.out.println("Staff expenses:");
        double staffExpenses = Double.parseDouble(scanner.next());
        System.out.println("Other expenses:");
        double otherExpenses = Double.parseDouble(scanner.next());

        double netIncome = income - staffExpenses - otherExpenses;

        System.out.printf("Net income: $%.1f", netIncome);

    }
}