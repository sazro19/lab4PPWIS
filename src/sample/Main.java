package sample;

import javafx.scene.control.Button;
import main.controller.SimpleFileChooser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        SimpleFileChooser simpleFileChooser = new SimpleFileChooser();

        HBox hBox = new HBox();
        Button loadButton = new Button("load");
        Button saveButton = new Button("save");
        hBox.getChildren().addAll(loadButton,saveButton);

        loadButton.setOnAction(e -> {
            System.out.println(simpleFileChooser.load().get());
        });
        saveButton.setOnAction(e ->{
            try {
                simpleFileChooser.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(hBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("explorerFX");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
