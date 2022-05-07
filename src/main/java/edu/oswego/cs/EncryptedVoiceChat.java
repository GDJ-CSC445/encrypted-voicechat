package edu.oswego.cs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

;


public class EncryptedVoiceChat extends Application {
    Stage window;
    Parent root;
    Scene scene;

    static Socket socket;
    static int port;
    static Boolean connectedToRoom;


    @Override
    public void start(Stage stage) throws IOException, InterruptedException, ExecutionException {

        window = stage;
        stage.setTitle("Main Menu");

        ServerConectionTask connServ = new ServerConectionTask();

        connServ.connectProperty().addListener((v, oldValue, newValue) -> {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu2.fxml")));
                scene = new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application-styles.css")).toExternalForm());
                Platform.runLater(() -> {
                    stage.setScene(scene);
                    stage.show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu1.fxml")));
            scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application-styles.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread th = new Thread(connServ.task);
        th.setDaemon(true);
        th.start();

        connServ.task.setOnSucceeded(workerStateEvent -> socket = connServ.task.getValue());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
