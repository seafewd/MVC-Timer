package Counter;

import java.io.Serializable;
import java.util.Observable;

public class CounterModel extends Observable implements Serializable{

    private int value;

    //increment the value of the animation timer, flag as changed and notify all observers (CounterView in this case)
    //notifyObservers: calls all update() methods for all Observers observing the object...
    public void inc() {
        value++;
        setChanged();
        notifyObservers();
    }

    //set the value (of the animation timer) to 0, flag as changed, notify
    public void reset() {
        value = 0;
        setChanged();
        notifyObservers();
    }

    public int getValue() {
        return value;
    }
}
