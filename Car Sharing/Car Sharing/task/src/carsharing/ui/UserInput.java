package carsharing.ui;


import java.util.Scanner;

public class UserInput {

    private final Scanner scanner;

    public UserInput() {
        scanner = new Scanner(System.in);
    }

    public String readLine() {
        return scanner.nextLine().trim();
    }

    public int readInt() {
        return Integer.parseInt(scanner.nextLine().trim());
    }

    public void close() {
        scanner.close();
    }
}
