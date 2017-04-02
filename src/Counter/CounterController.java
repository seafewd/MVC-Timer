package Counter;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Counter.CounterView.timerTextProperty;

public class CounterController implements Serializable {
    static transient BooleanProperty running;
    private transient AnimationTimer timer;
    private CounterModel model;

    public CounterController(CounterModel model) {
        this.model = model;
        running = new SimpleBooleanProperty(false); //boolean property to keep track of the timer state
        createTimer();
    }

    //animation timer
    private void createTimer() {
        timer = new AnimationTimer() {
            private long currentTime;

            public void handle(long now) {
                if (now > currentTime + 1_000_000_000) {
                    currentTime = now;
                    model.inc();
                    System.out.println("Timer's current value: " + timerTextProperty.getValue());
                }
            }
        };
    }

    //start timer and update the running property
    public void start() {
        running.setValue(true);
        timer.start();
        System.out.println("Timer running: " + isRunning());
    }

    //stop timer and update the running property
    public void stop() {
        running.setValue(false);
        timer.stop();

        System.out.println("Timer running: " + isRunning());
    }

    //reset timer
    public void reset() {
        model.reset();
        System.out.println("Timer reset.");

    }

    //get the time in HH:mm:ss format
    static String getTime() {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        return timeStamp;
    }

    //returns running state of the timer (bool)
    public static boolean isRunning() {
        return running.get();
    }

    //return timer property
    public BooleanProperty runningProperty() {
        return running;
    }

    //returns the model
    public CounterModel getModel() {
        return model;
    }

    //serialize object. this method is needed because not all members in the class are serializable, e.g. the runningProperty
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        // write BooleanProperty (not serializable)
        oos.writeBoolean(isRunning());
    }
    //de-serialize object. this method is needed because not all members in the class are serializable, e.g. the runningProperty
    private void readObject(ObjectInputStream ois) throws IOException {
        try {
            ois.defaultReadObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // read BooleanProperty
        running = new SimpleBooleanProperty(ois.readBoolean());
        // create timer
        createTimer();
        if(running.getValue()) start();
    }

}
