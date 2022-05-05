package edu.oswego.cs;

import javafx.collections.ModifiableObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SceneController1 {

    private Parent root;
    private Stage stage;
    private Scene scene;
    public Label connectedToLabel = null;
    public ModifiableObservableListBase<String> listProperty = null;
    public ArrayList<String> listOfChatRooms = new ArrayList<>();

    @FXML
    public void switchToMainMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu2.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchCreateServer(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CreateServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToServerList(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ListServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToActiveChat(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ActiveChat.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /*    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listProperty = new ;
    }*/

 /*


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       while(!connetProperty().get()){
            try {
                socket = new Socket("pi.cs.oswego.edu", 26990);
                BufferedReader inport = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                port = Integer.parseInt(inport.readLine());
                Thread.sleep(1000);
                socket.close();
                socket = new Socket("pi.cs.oswego.edu", port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = socket.getInputStream();
                setConnet(true);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        Socket finalSocket = socket;
        new Thread( ()-> {

            while (finalSocket.isConnected()) {
                try {

                    byte[] buffer = new byte[1024];
                    in.read(buffer);
                    Packet packet = Packet.parse(buffer);
                    if (packet instanceof ParticipantACK) {
                        ParticipantACK participantACK = (ParticipantACK) packet;
                        for (String param : participantACK.getParams()) {
                            // this is where we find the names of the chat rooms
                            System.out.println(param);
                        }
                    }
                    buffer = new byte[]{};
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}