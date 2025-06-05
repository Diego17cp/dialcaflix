package com.dialca.recommender;
// Author: DialcaDev / Diego Castro
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hola mundo");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 200); 
        
        primaryStage.setTitle("Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
