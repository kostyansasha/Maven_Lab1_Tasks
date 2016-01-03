package tasks;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Model m = new Model();
        Controller c = new Controller(m);
        View v = new View(c);

        Thread t = new Thread(c);
        t.start();

        v.Start();
    }
}
