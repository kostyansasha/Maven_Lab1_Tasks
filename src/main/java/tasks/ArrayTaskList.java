package tasks;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * array list
 * @author Sasha Kostyan
 * @version %I%, %G%
 * @updated 17-DEC-2015 22:26:44
 */
public class ArrayTaskList extends TaskList implements Cloneable, Serializable {
    private int numberOfSizeArrayTask;  // variable for size of array
    private Task arrayTask[];           // create array of tasks

    /**
     * method that add to the list of task
     *
     * @param task that need add
      */
    public void add(Task task)  {
        Task tempArrayTask[] = new Task[numberOfSizeArrayTask + 1]; // copy old array in new

        for (int i=0; i < numberOfSizeArrayTask; i++)               // copy old array in new
                tempArrayTask[i] = arrayTask[i];

        tempArrayTask[numberOfSizeArrayTask] = task;
        arrayTask = tempArrayTask;
        numberOfSizeArrayTask++;

    }

    /**
     * method that delete task from list
     *
     * @param task that need delete
     * @return <code>true</code> if the task delete
     *               <code>false</code> if the task does not delete
     */
    public boolean remove(Task task) throws Exception {
        if (task == null)
            throw new Exception("incoming task is null");

        int i;                                          // to find a match for the entire array
        int j;                                          // to move an item after the first found
        boolean itemFound;                              //status delete
        itemFound = false;

        for (i = 0; i < numberOfSizeArrayTask; i++) {   //Not ArrayTask.length-1!
            if (task.equals(arrayTask[i])) {

                // delete the last element
                if (i == numberOfSizeArrayTask-1) {
                    arrayTask[i] = null;
                    numberOfSizeArrayTask--;

                    // copy old array in new
                    Task tempArrayTask[] = new Task[numberOfSizeArrayTask];
                    for (int k=0; k < numberOfSizeArrayTask; k++)
                        tempArrayTask[k] = arrayTask[k];
                    arrayTask = tempArrayTask;

                    itemFound = true;
                    break;
                }

                // not last element, fist found
                for (j = i; j < numberOfSizeArrayTask-1; j++) {
                    arrayTask[j] = arrayTask[j + 1];
                }
                arrayTask[numberOfSizeArrayTask-1] = null;
                numberOfSizeArrayTask--;

                // copy old array in new
                Task tempArrayTask[] = new Task[numberOfSizeArrayTask];
                for (int k=0; k < numberOfSizeArrayTask; k++)
                    tempArrayTask[k] = arrayTask[k];
                arrayTask = tempArrayTask;

                itemFound = true;
                break;
            }
        }
        // task is not found in the array
        if (!itemFound) {
            return false;
        }
        // task delete
        return true;
    }

    /**
     *  Number of jobs in the list
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
        return arrayTask[index];
    }


    public Iterator<Task> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Task> {
        int cursor;                                 // index of next element to return
        int lastRet = -1;                           // index of last element returned; -1 if no such


        public boolean hasNext() {
            return cursor < numberOfSizeArrayTask;  //ArrayTaskList.this.numberOfSizeArrayTask
        }

        public Task next() {
            int i = cursor;

            if (i >= numberOfSizeArrayTask)
                throw new NoSuchElementException();

            TaskList elementData = ArrayTaskList.this;

            if (i >= elementData.size())
                throw new ConcurrentModificationException();

            cursor = i + 1;
            return  arrayTask[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                ArrayTaskList.this.remove(getTask(lastRet));
                cursor = lastRet;
                lastRet = -1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList result = (ArrayTaskList) super.clone();
        result.numberOfSizeArrayTask = 0;

        for (Task ff : this) {
            result.add(ff.clone());
        }

        return result;
    }

}