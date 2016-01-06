package tasks;

/**
 * The start point om program
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
public class Main {
    public static void main(String[] args) {
        Controller c = new Controller();
        Thread t = new Thread(c);

        t.setDaemon(true);
        t.start();                      //Thread for reminders

        c.start();                      //work with console
    }
}
