package tasks;
import java.util.SortedMap;

/**
 * Created by Саша on 20.12.2015.
 */
public class Controller {
    Model model = new Model();
    public void Close(){
        model.Close();
    }
    public SortedMap Period(int i){
        return (model.Period(i));
    }
    public void AllTask(){
        model.AllTask();
    }
    public void Remove(int i){
        model.Remove(i);
    }
    public void View(int a){
        model.View(a);
    }
    public void AddTask(){
        model.AddTask();
    }
    public void Edit(int i){
        model.Edit(i);
    }
}
