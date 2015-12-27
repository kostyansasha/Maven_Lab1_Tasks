/**
 * Class that describes the Task
 *
 * @author Sasha Kostyan
 * @version %I%, %G%
 */

//package ua.sumdu.j2se.kostyan.tasks;
package tasks;
import java.io.Serializable;
import java.util.Date;

public class Task implements Cloneable, Serializable {
    private String title;
    private Date time = new Date(0);

    private Date start = new Date(0);
    private Date end = new Date(0);
    private int interval;

    private boolean active;
    private boolean repeat;

    //неакт задача що констр задачу €ка викон у задан час
    // без повторень ≥з заданою назвою
    /**
     * Constructor(String title, int time)
     * constructs a task which is executed in a given time
     * without repetitions with a given name
     *
     * @param title name Task
     * @param time  the beginning of the task
     * @throws Exception which show that the task has not been created
     */
    public Task(String title, Date time) throws Exception{

        if (title == null) {
            throw new Exception ("Title can not be null");
        }

        if (time.getTime() < 0) {
            throw new Exception ("time can not be < 0");
        }

        this.title = title;
        setTime(time);
    }

    // неактив задача €ка викон у заданому пром≥жку часу (≥ поч, ≥ к≥н)
    // ≥з заданим ≥нтервалом ≥ маЇ задану назву
    /**
     * Constructor(String title, int start, int end, int interval
     * Inactive task which is executed in a given period of time (start, end)
     * with a specified frequency and has given name
     *
     * @param title    name Task
     * @param start    the beginning of the task
     * @param end      of task
     * @param interval through which to repeat the task
     * @throws Exception which show that the task has not been created
     */
    public Task(String title, Date start, Date end, int interval) throws Exception {

        if (title == null) {
            throw new Exception ("Title can not be null");
        }

        if ( interval == 0) {
            throw new Exception ("interval can not be 0");
        }

        if ( start.after(end) /*time > end*/ ) {
            throw new Exception ("start can not be > endTime");
        }

        if ( interval >= end.getTime() - start.getTime()) {
            throw new Exception ("interval can not be >= EndTime - Time");
        }

        setTime(start, end, interval);
        this.title = title;
    }

    /**
     *
     * @return name of task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the name of task
     * @param title is name of task
     */
    public void setTitle(String title) {

        this.title = title;
    }

    /**
     * method of task status
     *
     * @param active <code>true</code> if the task active
     *               <code>false</code> if the task does not active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     *
     * @return status of task
     */
    public boolean isActive() {

        return active;
    }


    // методы дл€ счит и изм времени состо€ни€ что не повтор€ютс€
    /**
     * Reading time status of tasks that are not repeated
     *
     * @return if task is repeated back task start time
     *          else return time
     */
    public Date getTime() {
        if (repeat) {
            return new Date(start.getTime());   //???
        }

        return new Date(time.getTime());
    }

    /**
     * Set time of tasks that are not repeated
     * if the problem is repeated, it has become such, not repeated.
     *
     * @param time is time for set
     */
    public void setTime(Date time) {
        if (repeat) {
            repeat = false;
        }

        this.time.setTime(time.getTime());
    }

    // метод дл€ счит и изм зад что повтор€ютс€
    /**
     * Reading start time status of tasks that are repeated
     *
     * @return if task is repeated back task start time
     *          else return time
     */
    public Date getStartTime() {
        if (repeat) {
            return new Date(start.getTime());   //???
        }

        return new Date(time.getTime()); //- end; ???? час виконан€ задачи
    }

    /**
     * Reading end time of tasks that are repeated
     *
     * @return end time
     */
    public Date getEndTime() {
        if (repeat) {
            return new Date(end.getTime());   //???
        }

        return new Date(time.getTime());
    }

    /**
     * method for reading interval of tasks that are repeated
     *
     * @return interval and if task not repeat return 0
     */
    public int getRepeatInterval() {
        if (!repeat) {
            return 0;   //???
        }

        return interval;
    }

    /**
     * method for setting the task that is repeated
     *
     * @param start     of task
     * @param end       of task
     * @param interval  of task
     */
    public void setTime(Date start, Date end, int interval) {
        if (!repeat) {
            repeat = true;
        }

        this.start.setTime(start.getTime());
        this.end.setTime(end.getTime());
        this.interval = interval;
    }

    //метод дл€ проверки поdторени€ задачи
    /**
     * method to check the recurrence of the problem
     *
     * @return <code>true</code> if the task repeat
     *               <code>false</code> if the task does not repeat
     */
    public boolean isRepeated() {
        return repeat;
    }

    // врем€ следуйщ выполнени€ задачи
    /**
     * method returns the next time the task after a specified
     * time current, if subsequent to the time the problem is
     * not fulfilled, the method must return null (in old version is -1).
     *
     * @param current after which the task is executed
     * @return time the next execution
     */
    public Date nextTimeAfter(Date current) {
        if (active) {
            if (time.after(current)) {
                return time;
            }

            if (repeat) {
                if (start.after(current)) {
                    return start;
                } else {
                    long i = start.getTime();
                    while (i <= current.getTime()) {
                        i += interval*1000;
                    }
                    if (i > end.getTime()) {
                        return null;
                    }
                    return new Date(i);
                }
            }
        }
        return null;
    }

///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * special metods for check that task are equals
     * @param o task for check
     * @return true if tasks are equals, false if tasks are not equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if ( !time.equals(task.time) )   return false;
        //if ( !start.equals(task.start)) return false;
        //if (end != task.end)     return false;
        //if (interval != task.interval) return false;
        if (active != task.active) return false;
        if (repeat != task.repeat) return false;

        if (!title.equals(task.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;

        result = title != null ? title.hashCode() : 0;

         result = result + (int)time.getTime();
  //     result = 31 * result + start;
    //   result = 31 * result + end;
      // result = 31 * result + interval;

        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (repeat ? 1 : 0);

        return result;
    }

    @Override
    public String toString() {
        StringBuilder t = new StringBuilder();

        t.append("Task{ ").append("title='").append(title).append("', time=").append(time).append(", start=")
                .append(start).append(", end=").append(end).append(", interval=").append(interval).append(", active=")
                .append(active).append(", repeat=").append(repeat).append('}');

        return t.toString();
    }

    @Override
    public Task clone() {
        Task result = null;

        try {
            result = (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Task in clone not create");
        }

        try {
            result.setTitle(this.getTitle());

            result.time = new Date(this.getTime().getTime());
            result.setTime(new Date(this.getStartTime().getTime()), new Date(this.getEndTime().getTime()), this.getRepeatInterval());

            result.setActive(this.isActive());
            result.repeat = this.isRepeated();
        } catch (Exception e) {
            System.out.println("Task parametrs not set");
            //e.printStackTrace();
        }

        return result;
    }
}
