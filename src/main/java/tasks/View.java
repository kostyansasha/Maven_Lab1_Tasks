package tasks;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 *
 */
public class View {
    private Controller controller;
    static final String dateFormat = "yyyy-MM-dd HH:mm";

    public View(Controller c){
        this.controller = c;
        controller.view = this;
    }

    public void Start()  {
        System.out.println("Program start");
        Scanner br = new Scanner(System.in);

        while(true) {
            System.out.println("please choose the next variant:");
            System.out.println("quit- 9, future task in period- 1, view all task- 2, change name file for save task- 5");

            int choose = br.nextInt();
            switch (choose) {
                case 9:
                    controller.Close();
                    break;
                case 5:
                    System.out.println("write new name");
                    controller.NameSaveFile();
                    break;
                case 1:
                    System.out.println("write period:");
                    System.out.println(controller.Period());
                    break;
                case 2:
                    controller.AllTask();
                    String str1 = "";

                    while (!str1.equals("0")) {
                        System.out.println("remove task ID- 2d, Edit Task ID- 2e, Add Task- 2a, view ID- 2v, return- 0");
                        str1 = null;
                        while (str1 == null || str1.equals(""))
                            str1 = br.nextLine();

                        if (str1.equals("2d")) { //delete
                            System.out.println("write number task: ");
                            int number = br.nextInt();
                            if (Confirm()) {
                                controller.Remove(number);
                            } else {
                                System.out.println("task not delete");
                            }
                        }

                        if (str1.equals("2e")) {//edit
                            System.out.println("write number task that you want edit: ");
                            int number = br.nextInt();
                            controller.View(number);
                            Edit(number);
                        }

                        if (str1.equals("2a")) {//add
                            AddTask();
                        }

                        if (str1.equals("2v")) {//view
                            System.out.println("write number task: ");
                            int number = br.nextInt();
                            System.out.println(controller.View(number).toString());
                        }
                    }
                    break;
            }
        }
    }
    boolean Confirm(){
        System.out.println("are you want confirm?");
        Scanner br = new Scanner(System.in);
        String conf="";

        while (true) {
            System.out.println("write Y or n ");
            while (conf.equals("")) {
                conf = br.nextLine();
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
    public void AddTask() {
        Scanner scan = new Scanner(System.in);
        String start = null; // parameters for future task
        String end = null;
        String repeat = "";
        int interval = 0;

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
        String act = "";
        boolean active = false;
        while (!act.equals("y") && !act.equals("n") ) {
            act = scan.nextLine();
        }
        if (act.equals("y")) {
            active = true;
        }

        //add task
        if (repeat.equals("y")) { // for repeat
            try {

                controller.AddTask(name, start, end, interval, repeat, active); // send parameters task in controller
            } catch (ParseException e) {
                System.out.println("not correct");
            }
        } else {
            try {               // for not repeat
                controller.AddTask(name, start, end, interval, repeat, active);
            } catch (ParseException e) {
                System.out.println("not correct");
            }
        }
    }

    public void Edit(int i){
        Task task = controller.View(i).clone();
        Scanner s = new Scanner(System.in);
        DateFormat dateForm = new SimpleDateFormat(dateFormat);
        String choose;
        String str = null;

        while (true) {
            if (task.isRepeated()) {
                System.out.println("name- 2en, date- 2ed, endDate- 2ee, interval- 2ei, active/unact- 2ea, view-v, confirm- cof ");
            }
            else {
                System.out.println("name- 2en, date- 2ed, active/unact- 2ea, view-v, confirm- cof ");
            }

            choose = s.nextLine();
            if (choose.equals("2en")) {//name
                System.out.println("write new name");
                task.setTitle(s.nextLine());
            }

            if (choose.equals("2ed")){
                System.out.println("number format: " + dateFormat);
                while (str == null || str.equals("")) {
                    str = s.nextLine();
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
                        end = s.nextLine( );
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
                        interval = s.nextInt();
                    }
                    task.setTime(task.getTime(), task.getEndTime(), interval);
                }
            }

            if (choose.equals("2ea")) {
                System.out.println("if task active write Y, else N");
                while(true) {
                    String act = s.nextLine();
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

            if (this.Confirm()){
                controller.Edit(task, i);
                break;
            } else {
                task = null;
            }
        }

    }
}
