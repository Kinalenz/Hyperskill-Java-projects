package lastpencil;

import java.util.*;


class PencilGame {
    Scanner scanner = new Scanner(System.in);

    int numberOfPencils = 0;
    String player1;
    String player2;
    String currentPlayer;
    String pencil;

    public PencilGame(){
        this.player1 = "Anakin";
        this.player2 = "ObiWan";
        this.currentPlayer = "";
        this.pencil = "|";
    }


    void askNumberOfPencils() {
        System.out.println("How many pencils would you like to use:");

        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int pencils = Integer.parseInt(input);
                if (pencils <= 0) {
                    System.out.println("The number of pencils should be positive");
                } else {
                    this.numberOfPencils = pencils;
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("The number of pencils should be numeric");
            }
        }
    }


    void printPencils() {
        System.out.println(pencil.repeat(numberOfPencils));
    }


    void askForFirstPlayer() {
        System.out.printf("Who will be the first (%s, %s):\n", player1, player2);

        while (true) {
            String player = scanner.nextLine().trim();

            if (player.equals(player1) || player.equals(player2)) {
                this.currentPlayer = player;
                break;
            }

            System.out.printf("Choose between '%s' and '%s'\n", player1, player2);
        }
    }


    void humanMove() {
        System.out.printf("%s's turn!\n", currentPlayer);

        while (true) {
            String input = scanner.nextLine();

            try {
                int pencils = Integer.parseInt(input);

                if (pencils < 1 || pencils > 3) {
                    System.out.println("Possible values: '1', '2' or '3'");
                } else if (pencils > numberOfPencils) {
                    System.out.println("Too many pencils were taken");
                } else {
                    numberOfPencils -= pencils;
                    currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Possible values: '1', '2' or '3'");
            }
        }
    }


    void botMove() {
        System.out.printf("%s's turn:\n", currentPlayer);

        int pencilsToTake;

        if (numberOfPencils % 4 == 1) {
            Random rand = new Random();
            pencilsToTake = rand.nextInt(3) + 1;
        } else if (numberOfPencils % 4 == 2) {
            pencilsToTake = 1;
        } else if (numberOfPencils % 4 == 3) {
            pencilsToTake = 2;
        } else {
            pencilsToTake = 3;
        }

        pencilsToTake = Math.min(pencilsToTake, numberOfPencils);

        System.out.println(pencilsToTake);
        numberOfPencils -= pencilsToTake;
        currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
    }


    void printWinnersName() {
        String winner = currentPlayer.equals(player1) ? player1 : player2;
        System.out.println(winner + " won!");
    }


    void startGame() {
        askNumberOfPencils();
        askForFirstPlayer();

        while (numberOfPencils > 0) {
            printPencils();

            if (currentPlayer.equals(player1)) {
                humanMove();
            } else {
                botMove();
            }
        }

        printWinnersName();
    }
}

public class Main {
    public static void main(String[] args) {
        PencilGame game = new PencilGame();
        game.startGame();
    }
}

