package tasks;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * linked list
 * @author Sasha Kostyan
 * @version %I%, %G%
 * @updated 17-дек-2015 22:26:45
 */

public class LinkedTaskList extends TaskList implements Cloneable, Serializable {
    // variable for size of array
    private int numberOfSizeArrayTask;
    //
    private TaskNode point;
    private TaskNode first;
    /**
     * method that add to the list of task
     *
     * @param task that need add
     */
    public void add(Task task) {
        TaskNode node = new TaskNode();
        node.setTask(task);

        if (first == null) {
            first = node;
            point = node;
            this.numberOfSizeArrayTask++;
        } else {
            point.setNext(node);
            point = node;
            this.numberOfSizeArrayTask++;
        }
    }


    /**
     * method that delete task from list
     *
     * @param task that need delete
     * @return <code>true</code> if the task delete
     *               <code>false</code> if the task does not delete
     * @throws Exception
     */
    public boolean remove(Task task) throws Exception {
        if (task == null)
            throw new Exception("incoming task is null");

        TaskNode pointPredecessor;
        TaskNode point = this.first;

        if (point == null)
            return false;

        // if one task in a list
        if ((point.getNext() == null) && point.getTask().equals(task)) {
            this.first = null;
            this.point = null;
            this.numberOfSizeArrayTask--;
            return true;
        }

        // if the first in a list
        if (point.getTask().equals(task)) {
            this.first = point.getNext();
            this.numberOfSizeArrayTask--;
            return true;
        }

        //if the task is in the middle of the list
        pointPredecessor = point;
        point = point.getNext();

        for (int i = 0; i < size() - 2; i++) { //without first and last
            if (point.getTask().equals(task)) {
                pointPredecessor.setNext(point.getNext());
                this.numberOfSizeArrayTask--;
                return true;
            }
            pointPredecessor = point;
            point = point.getNext();
        }

        // if task is last in list
        if (point.getNext() == null && point.getTask().equals(task)) {
            pointPredecessor.setNext(null);
            this.point = pointPredecessor;
            this.numberOfSizeArrayTask--;
            return true;
        }
        return false;
    }

    /**
     * Number of jobs in the list
     *
     * @return number Of Size ArrayTask
     */
    public int size() {
        return numberOfSizeArrayTask;
    }

    /**
     * method that return the number of task that specified in the list
     *
     * @param index of task in list
     * @return task
     */
    public Task getTask(int index) {
        rangeCheck(index);

        int count = 0;
        TaskNode point = first;

        if (point == null)
            return null;

        while (index >= count) {
            if (count == index)
                break;

            point = point.getNext();
            count += 1;
        }
        return point.getTask();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    //@Override
    public Iterator<Task> iterator() {

        return new MyIterator();
    }

    private class MyIterator implements Iterator<Task> {
        private TaskNode lastReturned;
        private TaskNode next;
        private int nextIndex;

        public MyIterator() {
            next = first;

        }

        public boolean hasNext() {
            //return nextIndex < numberOfSizeArrayTask; //LinkedTaskList.this.numberOfSizeArrayTask
            if (next == null) return false;
            // if (next.getNext() == null) return false;
            return true;
        }

        public Task next() {
            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.getNext();
            nextIndex+=1;
            return lastReturned.getTask();
        }

        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();

            try {
                LinkedTaskList.this.remove(getTask(nextIndex-1));

                lastReturned = null;
                nextIndex--;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public LinkedTaskList clone() throws CloneNotSupportedException{
        LinkedTaskList result = (LinkedTaskList) super.clone();
        result.numberOfSizeArrayTask = 0;
        result.first = null;
        result.point = null;

        for (Task it : this)
            result.add(it.clone());

        return result;
    }

}