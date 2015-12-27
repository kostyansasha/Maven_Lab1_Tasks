

/**
 * Class  that defines the list node
 *
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
//package ua.sumdu.j2se.kostyan.tasks;
package tasks;
import java.io.Serializable;

public class TaskNode implements Serializable {

    private Task task;
    private TaskNode next;

    /**
     *
     * @return task
     */
    public Task getTask () {
        return this.task;
    }

    /**
     *
     * @param task that need to set
     */
    public void setTask (Task task) {
        this.task = task;
    }

    /**
     *
     * @param next is link to the next task
     */
    public void setNext(TaskNode next) {
        this.next = next;
    }

    /**
     *
     * @return link on current task
     */
    public TaskNode getNext() {
        return this.next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TaskNode taskNode = (TaskNode) o;

        return !(task != null ? !task.equals(taskNode.task) : taskNode.task != null);

    }

    @Override
    public int hashCode() {
        return task != null ? task.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TaskNode{" +
                "task=" + task +
                ", next=" + next +
                '}';
    }
}