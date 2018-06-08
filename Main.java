package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * główna  klasa aplikacji
 * tutaj  jest pierwsze okno aplikacji, po naciśnięciu "Play", jest odtwarzana metoda z klasy Game
 */
public class Main extends Application implements EventHandler<ActionEvent> {

    Game game = new Game();
    Stage stage = new Stage();

    public Button buttonPlay;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Options");

        buttonPlay = new Button("Play");

        buttonPlay.setOnAction(this::handle);

        StackPane layout =  new StackPane();
        layout.getChildren().add(buttonPlay);

        Scene scene = new Scene(layout, 400,  200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void handle(ActionEvent event) {
        game.start(stage);
    }

}

