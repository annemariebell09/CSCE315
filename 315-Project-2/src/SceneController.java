import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.ToolBar;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Test Users: 1488844, 1181550, 1227322

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML Text movie1;
    @FXML Text movie2;
    @FXML Text movie3;
    @FXML Text movie4;
    @FXML Text movie5;
    @FXML TextField dateStartInput;
    @FXML TextField dateEndInput;
    @FXML TextField userid;
    @FXML Text userout;

    private jdbcpostgreSQL sqlController = new jdbcpostgreSQL();


    public void switchToAnalyst(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("analyst.fxml"));
            stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    @FXML protected void generateData(ActionEvent event) {
        String user = userid.getText();
        List<String> recentTitles = sqlController.getRecentTitles(userid.getText(), dateStartInput.getText(), dateEndInput.getText());
        userout.setText(user);
        movie1.setText(recentTitles.get(0));
        movie2.setText(recentTitles.get(1));
        movie3.setText(recentTitles.get(2));
        movie4.setText(recentTitles.get(3));
        movie5.setText(recentTitles.get(4));
    }

    public void switchToViewer(ActionEvent event) {
        try { 
            /*
            * Buttons for different sections and Toolbar for the buttons
            *
            */
            Button btn1 = new Button("Genre");
            Button btn2 = new Button("Director");
            Button btn3 = new Button("User");
            Button btn4 = new Button("Runtime");
            Button btn5 = new Button("Reset");
            Button btn6 = new Button("Viewer");
            Button btn7 = new Button("Submit");
            Button btn8 = new Button("Back");
            TextField textField1 = new TextField ();
            TextField textField2 = new TextField ();

            ToolBar toolBar = new ToolBar(btn1, btn2, btn3, btn4, btn5, btn6);
            
            VBox vBox = new VBox(toolBar);
            vBox.setAlignment(Pos.TOP_RIGHT);

            /*
            * Graph Genre 
            *
            */

            NumberAxis xAxis = new NumberAxis(); // create xAxis
            xAxis.setLabel("label1"); // label xAxis

            NumberAxis yAxis = new NumberAxis(); // create yAxis
            yAxis.setLabel("label2"); // label yAxis

            //create line chart using xAxis and yAxis
            LineChart lineChart = new LineChart(xAxis, yAxis);

            //create data series
            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("Genre");


            //add data series to line chart
            lineChart.getData().add(dataSeries1);



            /*
            * Graph Director
            *
            */

            NumberAxis xAxis2 = new NumberAxis(); // create xAxis
            xAxis2.setLabel("label1"); // label xAxis

            NumberAxis yAxis2 = new NumberAxis(); // create yAxis
            yAxis2.setLabel("label2"); // label yAxis

            //create line chart using xAxis and yAxis
            LineChart lineChart2 = new LineChart(xAxis2, yAxis2);

            //create data series
            XYChart.Series dataSeries2 = new XYChart.Series();
            dataSeries2.setName("Director");


            //add data series to line chart
            lineChart2.getData().add(dataSeries2);


            /*
            * Graph user
            *
            */
            NumberAxis xAxis3 = new NumberAxis(); // create xAxis
            xAxis3.setLabel("label1"); // label xAxis

            NumberAxis yAxis3 = new NumberAxis(); // create yAxis
            yAxis3.setLabel("label2"); // label yAxis

            //create line chart using xAxis and yAxis
            LineChart lineChart3 = new LineChart(xAxis3, yAxis3);

            //create data series
            XYChart.Series dataSeries3 = new XYChart.Series();
            dataSeries3.setName("User");


            //add data series to line chart
            lineChart3.getData().add(dataSeries3);


            /*
            * Graph Runtime
            *
            */
            NumberAxis xAxis4 = new NumberAxis(); // create xAxis
            xAxis4.setLabel("label1"); // label xAxis

            NumberAxis yAxis4 = new NumberAxis(); // create yAxis
            yAxis4.setLabel("label2"); // label yAxis

            //create line chart using xAxis and yAxis
            LineChart lineChart4 = new LineChart(xAxis4, yAxis4);

            //create data series
            XYChart.Series dataSeries4 = new XYChart.Series();
            dataSeries4.setName("Runtime");


            //add data series to line chart
            lineChart4.getData().add(dataSeries4);



            btn1.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    System.out.println("Genre Analytics");
                    vBox.getChildren().add(lineChart);
                    //btn1.setDisable(true); //can disable button after click
                }
            });
            
            btn2.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    System.out.println("Director Analytics");
                    vBox.getChildren().add(lineChart2);
                    

                }
            });
            
            btn3.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    Label label1 = new Label("Start Date:");
                    Label label2 = new Label("End Date:");
                
                    HBox hb = new HBox();
                    hb.getChildren().addAll(label1, textField1, label2, textField2, btn7);
                    hb.setSpacing(10);
                    vBox.getChildren().add(hb);
                }
            });
            
            btn4.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    System.out.println("Runtime Analytics");
                    vBox.getChildren().add(lineChart4);
                

                }
            });

            btn5.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    vBox.getChildren().remove(lineChart);
                    vBox.getChildren().remove(lineChart2);
                    vBox.getChildren().remove(lineChart3);
                    vBox.getChildren().remove(lineChart4);
                }
            });

            btn6.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    try {
                        root = FXMLLoader.load(getClass().getResource("analyst.fxml"));
                        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception ie) {
                        System.out.println(ie);
                    }
                }
            });

            btn7.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    String startDate = textField1.getText();
                    String endDate = textField2.getText();

                    System.out.println("Llama's Recomendations");   
                        
                    //Creating a scene object 
                    //Scene scene = new Scene(root, 600, 300);  
                    
                    //Setting title to the Stage 
                    stage.setTitle("Recomendations"); 
                    Text text = new Text();
                    text.setText("Llama's Recomendations");
                    
                    int maxTitles = 10;

                    List<String> titles = sqlController.getTopTenTitles(startDate, endDate);
                    


                    ListView<String> listView = new ListView<String>();
                    for (int i = 0; i < maxTitles; i++) {
                        listView.getItems().add(titles.get(i));
                    }
                    
                    //create an hbox to hold the list
                    HBox hbox = new HBox(listView, btn8);

                    Scene scene = new Scene(hbox, 960, 600);
                    stage.setScene(scene);
                    stage.show();
                }
            });

            btn8.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    try {
                        root = FXMLLoader.load(getClass().getResource("analyst.fxml"));
                        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception ie) {
                        System.out.println(ie);
                    }
                }
            });
            //toolBar.setOrientation(Orientation.HORIZONTAL);
            
            Scene scene = new Scene(vBox, 960, 600);
            stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
            stage.setScene(scene);
            stage.setTitle("Analytics");
            stage.show();
        } catch (Exception ie) {
            System.out.println(ie);
        }
    }

}



