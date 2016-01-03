package tasks;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.SortedMap;
import org.apache.log4j.Logger;

import javax.xml.transform.TransformerException;

public class Model {
    private TaskList tasks;
    private File file;
    static String filename = "tasks_File.txt";
    static Logger log = Logger.getLogger(Model.class);

    public Model() {
        try {
            tasks = new ArrayTaskList();
            System.out.println("last work program is "+ TaskIO.ReadXML().toString());
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
    public void ChangeFile(String s){
        file.delete();//true or false no matter
        filename = s;
        file = new File(filename);
        SaveTasks();
        try {
            TaskIO.WriteXML(filename);
        } catch (TransformerException | IOException e) {
            log.error("write in xml" + e);
        }
    }

    public void AllTask() {
        System.out.println(tasks.toString());
    }

    public void Close() {
        try {
            TaskIO.WriteXML(filename);
        } catch (TransformerException | IOException e) {
            Model.log.error("error XML" + e);
        }
        System.exit(0);
    }

    public SortedMap Period(int i) {
        Date d = new Date(System.currentTimeMillis());
        Date d2 = new Date( d.getTime() + (i*1000 * 86400) );

        return ( Tasks.calendar(tasks, d, d2));
    }

    public void Remove(int i) {
        try {
            tasks.remove(tasks.getTask(i));
            log.info("Task remove success");
            SaveTasks();
        } catch (Exception e){
            log.error("task remove Error");
        }
    }

    public Task View(int a) {
        return tasks.getTask(a);
    }

    public void AddTask(Task task) {
        try {
            tasks.add(task);
            log.info("tass add");
            SaveTasks();
        } catch (Exception e) {
            log.error("task add" + e);
        }
    }

    private void SaveTasks () {
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
