package tasks;
import java.io.File;
import java.util.Date;
import java.util.SortedMap;
import org.apache.log4j.Logger;

public class Model {
    TaskList tasks;
    File file;
    static Logger log = Logger.getLogger(Model.class);

    public Model() {
        try {
            tasks = new ArrayTaskList();
            file = new File("tasks_File.txt");
            TaskIO.readText(tasks, file);
            log.info("initialization task success");
        } catch (Exception e) {
            System.out.println("initialization task error");
            log.debug("initialization  task error in class Model" + e.getMessage());
        }
    }

    public void AllTask() {
        System.out.println(tasks.toString());
    }
    public void Close() {
        System.exit(0);
    }

    public SortedMap Period(int i) {
        Date d = new Date(System.currentTimeMillis());
        Date d2 = new Date( d.getTime() + (i*1000 * 86400) );

        return ( Tasks.calendar(tasks, d, d2 ));
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
            log.error("task add"+e);
        }
    }

    private void SaveTasks () {
        try {
            TaskIO.writeText(tasks, file);
        } catch (Exception e ){
            log.error("write in file Error"+e);
        }
    }
}
