package edu.oswego.cs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

;


public class EncryptedVoiceChat extends Application {
    Stage window;
    Parent root;
    Scene scene;
    Socket socket;
    static Socket socket_t;
    static int port;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        window = stage;
        stage.setTitle("Main Menu");

        ServerConnection connServ = new ServerConnection();

        connServ.connetProperty().addListener((v, oldValue, newValue) -> {
            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu2.fxml")));
                scene = new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application-styles.css")).toExternalForm());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.setScene(scene);
                        stage.show();
                    }
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

        socket_t = new Socket("moxie.cs.oswego.edu", 15551);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket_t.getInputStream()));
        int port = Integer.parseInt(input.readLine());
        Thread.sleep(1000);
        socket_t.close();
        socket_t = new Socket("moxie.cs.oswego.edu", port);

        //Thread th = new Thread(connServ.task1);
        //th.setDaemon(true);
        //th.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
