package tasks;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Controller implements Runnable{
    Model model;
    View view;
    Scanner br;

    public Controller(Model m) {
        this.model = m;
        br = new Scanner(System.in);
    }

    public void Close() {
        model.Close();
    }

    /**
     * method for change name of file for save tasks
     * 
     */
    public void NameSaveFile(){
        String s = null;
        while (s == null || s.equals("")) {
            s = br.nextLine();
        }
        model.ChangeFile(s);
    }

    /**
     *
     * @return a string where each line have the time and  task,
     * if on that time have several tasks, they are written in one line.
     */
    public String Period() {
        int per = br.nextInt();
        String s = "";
        for (Object t: model.Period(per).entrySet()) {
            s = s + t.toString() + "\n";
        }
        return s;
    }

    public void AllTask() {
        model.AllTask();
    }

    public void Remove(int i){
        model.Remove(i);
    }

    public Task View(int a) {
        return (model.View(a));
    }

    /**
     *  method for add new task repeat or not
     *
     * @param name task
     * @param start date is a string
     * @param end date is a string
     * @param interval is a number of seconds
     * @param rep is task repeat or not
     * @param active is active or not
     * @throws ParseException
     */
    public void AddTask(String name, String start, String end, int interval, String rep, boolean active)
            throws ParseException {
        DateFormat dateForm = new SimpleDateFormat(View.dateFormat);
        Task task = null;
        boolean repeat;

        if (rep.equals("y")) {
            repeat = true;
        } else{
            repeat = false;
        }

        // create task
        if (repeat) {
            try {
                task = new Task(name, dateForm.parse(start), dateForm.parse(end), interval);
                task.setActive(active);
            } catch (ParseException e) {
                System.out.println("not correct format date");
            } catch (Exception e) {
                Model.log.error("task create err " + e);
            }
        } else {
            try {
                task = new Task(name, dateForm.parse(start) );
                task.setActive(active);
            } catch (ParseException e) {
                System.out.println("not correct format date");
            } catch (Exception e) {
                Model.log.error("task create err " + e);
            }
        }

        //save
        if (task == null) {
            return;
        }
        System.out.println("are you want save this task:");
        System.out.println(task.toString());
        if (this.view.Confirm()) {
            model.AddTask(task);
            System.out.println("task add");
        } else {
            System.out.println("task not add");
            task = null;
        }
    }

    /**
     * Method for replacing an old to a new task
     *
     * @param task is new
     * @param i is old
     */
    public void Edit(Task task, int i) {
        try {
            model.Remove(i);
            model.AddTask(task);
            Model.log.info("task edit success");
        } catch (Exception e) {
            Model.log.error("task edit err" + e);
        }
    }

    /**
     * notification of the occurrence of the task
     */
    @Override
    public void run() {
        Date current;


        while (true) {
            current = new Date(System.currentTimeMillis() + 3600 * 1000);

            for (Task task : model.tasks) {
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
}
