package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.ParticipantData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;


public class EncryptedVoiceChat extends Application {
    Stage window;
    Parent root;
    Scene scene;
    static Socket socket;
    static int port;
    static boolean connectedToRoom;
    static String selectedRoom = "";

    Label connectedToLabel;

    static String connectionHost = "pi.cs.oswego.edu";
    static int connectionPort = 26990;

    static ArrayList<String> chatrooms = new ArrayList<>();

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
                        connectedToLabel = (Label) root.lookup("#connectedToLabel");
                        connectedToLabel.setText("Connected to " + connectionHost);
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

        Thread th = new Thread(connServ.task1);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        //Close the connection between server and client.
        try {
            if (connectedToRoom) {
                ParticipantData participantData1 = new ParticipantData(ParticipantOpcode.LEAVE, EncryptedVoiceChat.port);
                EncryptedVoiceChat.socket.getOutputStream().write(participantData1.getBytes());
                EncryptedVoiceChat.socket.getOutputStream().flush();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
