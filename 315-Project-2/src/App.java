import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application {
    
    public static void main(String[] args) throws Exception {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // primaryStage.setTitle("HBox Experiment 1");

        // Button button1 = new Button("Analyst");
        // Button button2 = new Button("Viewer");


        // HBox hbox = new HBox(button1, button2);
        // hbox.setAlignment(Pos.TOP_RIGHT);

        // Scene scene = new Scene(hbox, 200, 100);
        // primaryStage.setScene(scene);
        // primaryStage.show();
        Parent root = FXMLLoader.load(getClass().getResource("analyst.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
