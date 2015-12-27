package tasks;
import java.util.Scanner;

/**
 *
 */
public class View {

    public static void Start()  {
        Controller controller = new Controller();
        System.out.println("Program start");
        while(true) {
            System.out.println("please choose the next variant:");
            System.out.println("quit- 9, future task in period- 1, view all task- 2");

            Scanner br = new Scanner(System.in);
            int choose = br.nextInt();
            switch (choose) {
                case 9:
                    controller.Close();
                    break;
                case 1:
                    System.out.println("write period:");
                    int per = br.nextInt();
                    System.out.println(controller.Period(per).toString());
                    break;
                case 2:
                    controller.AllTask();
                    System.out.println("remove task ID- 2d, Edit Task ID- 2e, Add Task- 2a, view ID- 2v, else for return");
                    String str1=null;
                    while (str1 == null || str1.equals(""))
                        str1 = br.nextLine();

                    if (str1.equals("2d")) { //delete
                        System.out.println("write number task: ");
                        int number = br.nextInt();
                        if (View.Confirm())
                            controller.Remove(number);
                        else System.out.println("task not delete");
                    }
                    if (str1.equals("2e")) {//edit
                        System.out.println("write number task that you want edit: ");
                        int number = br.nextInt();
                        controller.View(number);
                        controller.Edit(number);
                    }
                    if (str1.equals("2a")) {//add
                        controller.AddTask();
                    }
                    if (str1.equals("2v")) {//view
                        System.out.println("write number task: ");
                        int number = br.nextInt();
                        controller.View(number);
                    }
                    break;
            }
        }
    }
    private static boolean Confirm(){
        System.out.println("are you want confirm?");
        Scanner br = new Scanner(System.in);
        String conf="";
        while (true) {
            System.out.println("write Y or n ");
            while(conf.equals(""))
                conf = br.nextLine();
            if (conf.equals("Y")) {
                return true;
            }
            if (conf.equals("n")) {
                return false;
            }
        }
    }
}
