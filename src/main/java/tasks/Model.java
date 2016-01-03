package tasks;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.SortedMap;
import org.apache.log4j.Logger;
import javax.xml.transform.TransformerException;

/**
 * Class that produce pattern MVC
 *
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
public class Model {
    static  String      filename = "tasks_File.txt";
    static  Logger      log = Logger.getLogger(Model.class);
    private TaskList    tasks;
    private File        file;

    public Model() {
        try {
            tasks = new ArrayTaskList();
            System.out.println("last work program is "+ TaskIO.readXML().toString());
            System.out.println("name file for save task is  "  + filename);
            file  = new File(filename);
            TaskIO.readText(tasks, file);

            log.info("initialization task success");
        } catch (Exception e) {
            log.debug("initialization  task error in class Model" + e.getMessage());
        }
    }

    /**
     * method for change name of file for save tasks
     * @param s new name
     */
    public void changeFile(String s){
        file.delete();                              //true or false no matter
        filename = s;
        file = new File(filename);
        saveTasks();
        try {
            TaskIO.writeXML(filename);
        } catch (TransformerException | IOException e) {
            log.error("write in xml" + e);
        }
    }

    /**
     * write parametrs in XML file, and close the program
     */
    public void close() {
        try {
            TaskIO.writeXML(filename);
        } catch (TransformerException | IOException e) {
            Model.log.error("error XML" + e);
        }
        System.exit(0);
    }

    /**
     *
     * @param i amount of day for view tasks
     * @return SortedMap with date
     */
    public SortedMap period(int i) {
        Date d = new Date(System.currentTimeMillis());
        Date d2 = new Date( d.getTime() + (i*1000 * 86400) );

        return ( Tasks.calendar(tasks, d, d2));
    }

    /**
     * remove specific task in list
     * @param i number in list
     */
    public void remove(int i) {
        try {
            tasks.remove(tasks.getTask(i));
            log.info("Task remove success");
            saveTasks();
        } catch (Exception e){
            log.error("task remove Error");
        }
    }

    /**
     * view specific task in list more detail
     * @param a number in list
     * @return Task
     */
    public Task view(int a) {
        return tasks.getTask(a);
    }

    /**
     * add task in list
     * @param task of type Task
     */
    public void addTask(Task task) {
        try {
            tasks.add(task);
            log.info("tass add");
            saveTasks();
        } catch (Exception e) {
            log.error("task add" + e);
        }
    }

    /**
     * method for save list in file
     */
    private void saveTasks () {
        try {
            TaskIO.writeText(tasks, file);
        } catch (Exception e ){
            log.error("write in file Error"+e);
        }
    }

    /**
     * get task for controller if it needs
     * @return task list
     */
    public TaskList getTasks () {
        return tasks;
    }
}
