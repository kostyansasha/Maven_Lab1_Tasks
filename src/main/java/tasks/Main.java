package tasks;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        final Model m = new Model();
        Controller c = new Controller(m);
        View v = new View(c);

        /**
         * notification of the occurrence of the task
         */
        Runnable r = new Runnable() {
            Date current;
            Model mm = m;

            @Override
            public void run() {

                while (true) {
                    current = new Date(System.currentTimeMillis() + 3600 * 1000);

                    for (Task task : mm.tasks) {
                        Date next = task.nextTimeAfter(current);
                        if (next != null) {
                            Date occ = new Date(current.getTime() + 60 * 1000);
                            if (occ.compareTo(next) >= 0) {
                                System.out.println("you have task after one hour " + task.toString());
                            }
                        }
                    }

                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(r);

        t.start();
        v.Start();
    }
}
