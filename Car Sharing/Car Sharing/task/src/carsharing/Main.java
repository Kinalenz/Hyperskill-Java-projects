package carsharing;

public class Main {

    public static void main(String[] args) {
        String databaseFileName = (args.length == 2 && "-databaseFileName".equals(args[0])) ? args[1] : "defaultDB";
        Application app = new Application(databaseFileName);
        app.run();
    }
}
