package Counter;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.*;

public class MVCCounter extends Application {
    //main in bottom

    final static String FILE_NAME = "user_config.ser";
    private CounterController controller;

    public void start(Stage stage) {
        CounterModel model;

        //try to de-serialize the controller object if the .ser file exists
        //otherwise create new instances of the controller and the model
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
            controller = (CounterController)ois.readObject();
            model = controller.getModel();
        } catch (IOException | ClassNotFoundException e) {
            model = new CounterModel();
            controller = new CounterController(model);
        }

        new CounterView(model, controller);
    }

    @Override
    public void stop() {
        //try to serialize the controller object
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            oos.writeObject(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
