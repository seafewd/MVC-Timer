package Counter;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.util.Observable;
import java.util.Observer;

//Observer
public class CounterView extends Stage implements Observer{
    private CounterModel model;


    public Insets defaultPadding = new Insets(15, 15, 15, 15);

    //String property to bind the running state label to different values
    private StringProperty runningStateLabelProperty = new SimpleStringProperty("Timer is not running.");

    //String property to bind the timer labels to each other
    public static StringProperty timerTextProperty = new SimpleStringProperty();

    //CounterView constructor
    public CounterView(CounterModel model, CounterController controller) {
        this.model = model;

        //generate GUI
        drawControllerWindow("Controller 1", 1300, controller);
        drawControllerWindow("Controller 2", 900, controller);
        drawTimerWindow("Timer 1", 1300);
        drawTimerWindow("Timer 2", 900);

        //add observer to model object
        model.addObserver(this);
    }

    //update observers with new timer value
    public void update(Observable obs, Object args) {
        setTimerText();
    }

    private void drawControllerWindow(String title, int xPos, CounterController controller) {

        //layout elements
        Stage stage = new Stage();
        FlowPane root = new FlowPane();
        VBox columnVBOX = new VBox(5);
        HBox firstLineBox = new HBox(10);
        HBox secondLineBox = new HBox(10);

        //buttons
        Button startButton = new Button("Start timer");
        Button stopButton = new Button("Stop timer");
        Button resetButton = new Button("Reset timer");

        //label for timer state. value changed based on runningProperty's changeListener
        String timersRunning = "Watches are running";
        String timersStopped = "Watches are not running";
        Label runningStateLabel = new Label(controller.isRunning() ? timersRunning : timersStopped);
        controller.runningProperty().addListener((observable, oldValue, newValue) ->
                runningStateLabel.setText(newValue ? timersRunning : timersStopped)
        );

        //spacing
        root.setPadding(defaultPadding);

        //stage positioning
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - xPos));
        stage.setY((primScreenBounds.getHeight() - 400));

        //add nodes to elements
        firstLineBox.getChildren().addAll(runningStateLabel);
        secondLineBox.getChildren().addAll(startButton, stopButton, resetButton);
        columnVBOX.getChildren().addAll(firstLineBox, secondLineBox);

        root.getChildren().addAll(columnVBOX);

        //user closes the window
        stage.setOnCloseRequest(e -> close());

        //create scene & display it
        stage.setTitle(title);
        stage.setScene(new Scene(root, 270, 75));
        stage.show();

        //bind disableProperty of buttons to runningProperty
        startButton.disableProperty().bind(controller.runningProperty());
        stopButton.disableProperty().bind(controller.runningProperty().not());

        startButton.setOnAction(e -> controller.start());
        stopButton.setOnAction(e -> controller.stop());
        resetButton.setOnAction(e -> controller.reset());
    }

    private void drawTimerWindow(String title, int xPos) {
        Label headerLabel = createLabel("Timer");
        Label timerLabel = new Label();

        //binds the timerLabel value to timerTextProperty
        timerLabel.textProperty().bind(timerTextProperty);

        //layout elements
        Stage stage = new Stage();
        FlowPane root = new FlowPane();
        VBox columnBox = new VBox();
        HBox firstLineBox = new HBox();
        HBox secondLineBox = new HBox();

        //spacing & padding
        root.setPadding(defaultPadding);
        firstLineBox.setSpacing(10.0);
        secondLineBox.setSpacing(10.0);

        //stage positioning
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - xPos));
        stage.setY((primScreenBounds.getHeight() - 600));

        //add nodes to elements
        firstLineBox.getChildren().addAll(headerLabel);
        secondLineBox.getChildren().addAll(timerLabel);
        columnBox.getChildren().addAll(firstLineBox, secondLineBox);
        root.getChildren().addAll(columnBox);

        setTimerText();

        //user closes window
        stage.setOnCloseRequest(e -> close());

        //create scene & display it
        stage.setTitle(title);
        stage.setScene(new Scene(root, 220, 80));
        stage.show();
    }

    Label createLabel(String text) {
        Label label = new Label(text);
        return label;
    }

    @Override
    public void hide() {
        Platform.exit();
    }

    //set the text value of the timer label
    private void setTimerText() {
        long time = (model.getValue()*1000L)-3600000;
        DateTime dt = new DateTime(time);
        timerTextProperty.setValue(dt.toString("HH:mm:ss"));
    }
}
