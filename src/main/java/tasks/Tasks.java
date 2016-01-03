package tasks;

import java.util.*;

/**
 * class to work with collections of tasks
 * @author Sasha Kostyan
 * @version %I%, %G%
 */
public class Tasks {
    /**
     * method that returns a subset of the tasks that
     * are scheduled to perform in the interval.
     *Do not depend from array or list class
     *
     * @param tasks iterable of list
     * @param start date from which you want to search
     * @param end the date by which you want to search(inclusive)
     * @return array of task that are scheduled to perform in the interval
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) {
        TaskList inlist = new ArrayTaskList();

        for (Task arr : tasks ) {
            Date aftStart = arr.nextTimeAfter(start);                       //nextTimeAfter can return null
            if (aftStart != null && (aftStart.before(end) || aftStart.equals(end)))
                inlist.add(arr);
        }
        return inlist;
    }

    /**
     * build a calendar of tasks for a given period
     *
     * @param tasks iterable of list
     * @param start date from which you want to search
     * @param end the date by which you want to search(inclusive)
     * @return map of task, where key is day
     */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) {
        Date day;
        SortedMap<Date, Set<Task>> smap = new TreeMap<>();

        for (Task arr : tasks) {
            day = arr.nextTimeAfter(start);

            while(day != null && (day.before(end) || day.equals(end)) ) {
                if (!smap.containsKey(day)) {
                    smap.put(day, new HashSet<Task>());
                }

                smap.get(day).add(arr);
                day = arr.nextTimeAfter(day);
            }
        }
        return smap;
    }
}
