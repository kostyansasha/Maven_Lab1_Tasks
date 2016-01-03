package tasks;

public class Main {
    public static void main(String[] args) {
        Controller c = new Controller();
        Thread t = new Thread(c);

        t.start(); //Thread for reminders
        c.Start(); //work with console
    }
}
