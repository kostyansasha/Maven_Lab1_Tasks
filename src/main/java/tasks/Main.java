package tasks;

public class Main {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                    View.Start();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
