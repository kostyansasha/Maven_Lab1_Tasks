package tasks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * Class that produce pattern MVC
 *
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
public class Controller implements Runnable{
    static  Logger  log = Logger.getLogger(Controller.class);
    private Model   model;
    private View    view;
    private Scanner br;

    public Controller() {
        this.model = new Model();
        this.view = new View(this);
        br = new Scanner(System.in);
        log.info("initialization Controller success");
    }

    /**
     * the main level of work with the console
     */
    public void start() {
        while(true) {
            switch (view.level1()) {
                case 9:
                    close();
                    break;
                case 5:
                    nameSaveFile();
                    break;
                case 1:
                    view.print(period());
                    break;
                case 2:
                    allTask();
                    String str = "";

                    while (!str.equals("0")) {
                        str = view.level2();

                        if (str.equals("2d")) {                     //delete
                            view.print("write number task: ");
                            int number = br.nextInt();

                            if (view.confirm()) {
                                remove(number);
                            } else {
                                view.print("task not delete");
                            }
                        }

                        if (str.equals("2e")) {                     //edit
                            view.print("write number task that you want edit: ");
                            int number = br.nextInt();

                            view.print(view(number).toString());
                            view.edit(number);
                        }

                        if (str.equals("2a")) {                     //add
                            view.addTask();
                        }

                        if (str.equals("2v")) {                     //view
                            view.print("write number task: ");
                            int number = br.nextInt();

                            view.print(view(number).toString());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * method for close the program
     */
    public void close() {
        model.close();
    }

    /**
     * method for change name of file for save tasks
     *
     */
    public void nameSaveFile() {
        view.print("write new name");
        String s = null;

        while (s == null || s.equals("")) {
            s = br.nextLine();
        }

        model.changeFile(s);
    }

    /**
     * @return a string where each line have the time and  task,
     * if on that time have several tasks, they are written in one line.
     */
    public String period() {
        view.print("write period (days): ");
        int     per;
        String  s = "";

        per = br.nextInt();

        for (Object t : model.period(per).entrySet()) {
            s = s + t.toString() + "\n";
        }

        return s;
    }

    /**
     * method for view all tasks of list
     */
    public void allTask() {
        view.print(model.getTasks().toString());
    }

    /**
     * remove specific task in list
     * @param i number in list
     */
    public void remove(int i){
        model.remove(i);
    }

    /**
     * view specific task in list more detail
     * @param a number in list
     * @return Task
     */
    public Task view(int a) {
        return model.view(a);
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
    public void addTask(String name, String start, String end, int interval, String rep, boolean active)
            throws ParseException {
        DateFormat  dateForm = new SimpleDateFormat(View.dateFormat);
        Task        task = null;
        boolean     repeat;

        repeat = rep.equals("y");

        // create task
        if (repeat) {
            try {
                task = new Task(name, dateForm.parse(start), dateForm.parse(end), interval);
                task.setActive(active);
            } catch (ParseException e) {
                System.out.println("not correct format date");
            } catch (Exception e) {
                log.error("task create err " + e);
            }
        } else {
            try {
                task = new Task(name, dateForm.parse(start) );
                task.setActive(active);
            } catch (ParseException e) {
                System.out.println("not correct format date");
            } catch (Exception e) {
                log.error("task create err " + e);
            }
        }

        //save
        if (task == null) {
            return;
        }

        System.out.println("are you want save this task:");
        System.out.println(task.toString());

        if (this.view.confirm()) {
            model.addTask(task);
            System.out.println("task add");
        } else {
            System.out.println("task not add");
        }
    }

    /**
     * Method for replacing an old to a new task
     *
     * @param task is new
     * @param i is old
     */
    public void edit(Task task, int i) {
        try {
            model.remove(i);
            model.addTask(task);
            log.info("task edit success");
        } catch (Exception e) {
            log.error("task edit err" + e);
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

            for (Task task : model.getTasks()) {
                Date next = task.nextTimeAfter(current);

                if (next != null) {
                    Date occ = new Date(current.getTime() + 60 * 1000);
                    if (occ.compareTo(next) >= 0) {
                        System.out.println("you have task after one hour at " + task.nextTimeAfter(current).toString()
                                + " in " + task.toString());
                    }
                }
            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
