package tasks;

import java.util.Iterator;

/**
 * abstract class for release list of task
 *
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
public abstract class TaskList implements Iterable<Task> {
    public abstract void add(Task task);

    public abstract boolean remove(Task task) throws Exception;

    public abstract int size();

    public abstract Task getTask(int index);

    public abstract Iterator<Task> iterator();

    /**
     * for check index in list
     * @param index the estimated number in list
     */
    public void rangeCheck(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    /**
     * special metods for check that lists are equals
     *
     * @param o list for check
     * @return true if list equals or false if not equals
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || !(o instanceof TaskList)) {
            return false;
        }

        if ( ((TaskList) o).size() != this.size() ) {
            return false;
        }

        Iterator<Task> itCurrent = iterator();
        Iterator<Task> itObj = ((TaskList) o).iterator();

        while (itCurrent.hasNext() && itObj.hasNext()) {
            Task tItCur = itCurrent.next();
            Task tItObj = itObj.next();

            if ( !(tItCur == null ? tItObj == null : tItCur.equals(tItObj) )) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = size();

        result = 31 * result + getTask(0).hashCode()+getTask(result-1).hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("TaskList{   ").append("numberOfSizeArrayTask = ").append(size()).append(".").append( '\n' );

        Iterator<Task> it = iterator();
        int i = 0;
        while (it.hasNext()) {
            Task taskIt = it.next();
            b.append("   task[").append(i).append("]: ").append( taskIt.toString()).append('\n');
            i++;
        }

        b.append("}");
        return b.toString();
    }

}