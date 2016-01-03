package tasks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Class that produce pattern MVC
 *
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
public class View {
    static final String dateFormat = "yyyy-MM-dd HH:mm";
    private Controller  controller;
    private Scanner     scan;

    public View(Controller c){
        this.controller = c;
        this.scan = new Scanner(System.in);
        System.out.println("Program start");
    }

    /**
     * the start view in console
     * @return number that write in console
     */
    public int level1()  {
            System.out.println("please choose the next variant:");
            System.out.println("quit- 9, future task in period- 1, view all task- 2, change name file for save task- 5");
            int choose;
            choose = scan.nextInt();

            switch (choose) {
                case 9:
                    return 9;
                case 5:
                    return 5;
                case 1:
                    return 1;
                case 2:
                    return 2;
                default:
                    break;
            }
        return 0;
    }

    /**
     * For reflect the action when user choose "view all task" in Level1
     * @return string of choose user
     */
    public String level2() {
        System.out.println("remove task ID- 2d, Edit Task ID- 2e, Add Task- 2a, view ID- 2v, return- 0");
        String str1 = null;

        while (str1 == null || str1.equals("")) {
            str1 = scan.nextLine();
        }
        return str1;
    }

    /**
     * Print String in console
     * @param s is string with text
     */
    public void print(String s) {
        System.out.println(s);
    }

    /**
     * Confirmation of action for Users
     * @return true or false
     */
    boolean confirm(){
        System.out.println("are you want confirm?");
        String conf="";

        while (true) {
            System.out.println("write Y or n ");
            while (conf.equals("")) {
                conf = scan.nextLine();
            }

            if (conf.equals("Y")) {
                return true;
            }

            if (conf.equals("n")) {
                return false;
            }
        }
    }

    /**
     * form of add task
     */
    public void addTask() {
        String  start = null; // parameters for future task
        String  end = null;   //
        String  repeat = "";  //
        int     interval = 0; //

        System.out.println("write new name");
        String name = scan.nextLine();
        System.out.println("write start time");
        System.out.println("number format: " + dateFormat);

        while (start == null || start.equals("")) {
            start = scan.nextLine();
        }

        System.out.println("is task repeat? write y or n");
        while (!repeat.equals("y") && !repeat.equals("n")) {
            repeat = scan.nextLine();
        }

        if (repeat.equals("y")) {
            System.out.println("write endDate");
            System.out.println("number format: " + dateFormat);

            while (end == null || end.equals("")) {
                end = scan.nextLine();
            }

            System.out.println(" vvod interval");
            while (interval <= 0)
                interval = scan.nextInt();
        }

        System.out.println("if task active write y, else n");
        String  act = "";
        boolean active = false;

        while (!act.equals("y") && !act.equals("n") ) {
            act = scan.nextLine();
        }

        if (act.equals("y")) {
            active = true;
        }

        //add task
        if (repeat.equals("y")) {                       // for repeat
            try {
                controller.addTask(name, start, end, interval, repeat, active); // send parameters task in controller
            } catch (ParseException e) {
                System.out.println("not correct");
            }
        } else {
            try {                                       // for not repeat
                controller.addTask(name, start, end, interval, repeat, active);
            } catch (ParseException e) {
                System.out.println("not correct");
            }
        }
    }

    /**
     * form of edit task
     * @param i is number of task for edit
     */
    public void edit(int i){
        Task        task = controller.view(i).clone();
        DateFormat  dateForm = new SimpleDateFormat(dateFormat);
        String      choose;
        String      str = null;

        if (task == null) {
            return;
        }

        while (true) {
            if (task.isRepeated()) {
                System.out.println("name- 2en, date- 2ed, endDate- 2ee, interval- 2ei, active/unact- 2ea, view-v, confirm- cof ");
            } else {
                System.out.println("name- 2en, date- 2ed, active/unact- 2ea, view-v, confirm- cof ");
            }

            choose = scan.nextLine();
            if (choose.equals("2en")) {//name
                System.out.println("write new name");
                task.setTitle(scan.nextLine());
            }

            if (choose.equals("2ed")) {
                System.out.println("number format: " + dateFormat);
                while (str == null || str.equals("")) {
                    str = scan.nextLine();
                }

                try {
                    task.setTime(dateForm.parse(str));
                } catch (ParseException e) {
                    System.out.println("not correct format date");
                }
            }

            if (task.isRepeated()) {
                if (choose.equals("2ee")) {
                    System.out.println(" vvod endDate");
                    System.out.println("number format: " + dateFormat);

                    String end = null;
                    while (end == null || end.equals("")) {
                        end = scan.nextLine( );
                    }

                    try {
                        task.setTime(task.getTime(), dateForm.parse(end), task.getRepeatInterval());
                    } catch (ParseException e) {
                        System.out.println("not correct format date");
                    }
                }

                if (choose.equals("2ei")) {
                    System.out.println(" write interval");

                    int interval = 0;
                    while (interval <= 0) {
                        interval = scan.nextInt();
                    }

                    task.setTime(task.getTime(), task.getEndTime(), interval);
                }
            }

            if (choose.equals("2ea")) {
                System.out.println("if task active write Y, else N");

                while(true) {
                    String act = scan.nextLine();
                    if (act.equals("Y")) {
                        task.setActive(true);
                        break;
                    }

                    if (act.equals("N")) {
                        task.setActive(false);
                        break;
                    }
                }
            }
            if (choose.equals("v")){//view
                System.out.println(task.toString());
            }

            if (choose.equals("cof")) {
                if (this.confirm()) {
                    controller.edit(task, i);
                    break;
                } else {
                    task = null;
                }
            }
        }

    }
}
