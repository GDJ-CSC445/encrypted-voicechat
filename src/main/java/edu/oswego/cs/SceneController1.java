package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.ErrorOpcode;
import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.ErrorPacket;
import edu.oswego.cs.network.packets.Packet;
import edu.oswego.cs.network.packets.ParticipantACK;
import edu.oswego.cs.network.packets.ParticipantData;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class SceneController1 {

    public Button listServer;
    public Button backButton;
    public Button RefreshButton;
    public Button JoinServerButton;
    private Parent root;
    private Stage stage;
    private Scene scene;
    public Label connectedToLabel = null;

    public TextField ServerNameTextField;
    public TextField PasswordTextField;
    public Spinner<Integer> NumberOfParticipants;

    @FXML
    public void switchToMainMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu2.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void LeaveRoom(ActionEvent event) throws IOException {
        if (EncryptedVoiceChat.connectedToRoom) {
            ParticipantData participantData1 = new ParticipantData(ParticipantOpcode.LEAVE, EncryptedVoiceChat.port);
            EncryptedVoiceChat.socket.getOutputStream().write(participantData1.getBytes());
            EncryptedVoiceChat.socket.getOutputStream().flush();
        }

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

        final int serverNameLimit = 12;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ListServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ListView lv = (ListView) root.getChildrenUnmodifiable().get(0);

        ParticipantData participantData = new ParticipantData(ParticipantOpcode.LIST_SERVERS, EncryptedVoiceChat.port);
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());

        byte[] buffer = new byte[1024];

        EncryptedVoiceChat.socket.getInputStream().read(buffer);
        Packet packet = Packet.parse(buffer);
        if (packet instanceof ParticipantACK) {
            ParticipantACK participantACK = (ParticipantACK) packet;
            for (String param : participantACK.getParams()) {
                String name = param.split(";")[0];
                for (int i = serverNameLimit - name.length(); i > 0; i--)
                    name = name.concat("  ");
                if (name.length() % 2 != 0) name = name.concat("  ");
                String currentParticipants = param.split(";")[1].split("/")[0];
                String maxParticipants = param.split(";")[1].split("/")[1];
                HBox hbox = new HBox(new Label(name), new Label("(" + currentParticipants + "/" + maxParticipants + ")"));
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(260);
                lv.getItems().add(hbox);
                EncryptedVoiceChat.chatrooms.add(param);
            }
        } else if (packet instanceof ErrorPacket) {
            ErrorPacket errorPacket = (ErrorPacket) packet;
            ServerConnection.displayError(errorPacket.getErrorOpcode() + "; " + errorPacket.getErrorMsg());
        }


        HBox hBox = new HBox(root);
        scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void listViewOnChange(Event event) {
       ListView<HBox> lv = (ListView<HBox>) event.getSource();
       HBox hbox = lv.getSelectionModel().getSelectedItem();
       EncryptedVoiceChat.selectedRoom = ((Label) hbox.getChildrenUnmodifiable().get(0)).getText();
        System.out.println(EncryptedVoiceChat.selectedRoom);
    }

    @FXML
    public void switchToActiveChat(ActionEvent event) throws IOException {

        if (EncryptedVoiceChat.selectedRoom == null || EncryptedVoiceChat.selectedRoom.equals("")) return;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ListServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //root.getChildrenUnmodifiable().get(0).addEventHandler();
        // figure out how to see what is selected in HBox soo that you can pick that item to be sent in packet.
        ParticipantData participantData = new ParticipantData(ParticipantOpcode.JOIN, EncryptedVoiceChat.port, new String[]{EncryptedVoiceChat.selectedRoom});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());

        byte[] buffer = new byte[1024];

        new Thread( () -> {
            try {
                EncryptedVoiceChat.socket.getInputStream().read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Packet packet = Packet.parse(buffer);
            System.out.println("OPCODE: " + packet.getOpcode() + "; ");
            if (packet instanceof ErrorPacket) {
                System.out.println("Error: " + ((ErrorPacket) packet).getErrorOpcode());
                if (((ErrorPacket) packet).getErrorOpcode() == ErrorOpcode.CHATROOM_FULL) {
                    Label errorLabel = (Label) root.lookup("#errorDisplayLabel");
                    errorLabel.setText("CHATROOM FULL");
                }

            } else if (packet instanceof ParticipantACK) {
                System.out.println("Params: " + Arrays.toString(((ParticipantACK) packet).getParams()));
            }
        }).start();


    }

//        if (packet instanceof ErrorPacket) {
//            ErrorPacket errorPacket = (ErrorPacket) packet;
//            ServerConnection.displayError(errorPacket.getErrorOpcode() + "; " + errorPacket.getErrorMsg());
//            VBox dialogVbox = new VBox(20);
//            dialogVbox.getChildren().add(new Text("This is a Dialog"));
//            Scene dialogScene = new Scene(dialogVbox, 300, 200);
//
//            stage.setScene(dialogScene);
//            stage.show();
//            return;
//        }


    /*    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ActiveChat.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }*/

    @FXML
    public void createServer(ActionEvent event) throws IOException {
        String serverName = ServerNameTextField.getText();
        String numberOfParticipants = String.valueOf(NumberOfParticipants.getValue());
        ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, EncryptedVoiceChat.port, new String[]{serverName, numberOfParticipants});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());

        participantData = new ParticipantData(ParticipantOpcode.JOIN, EncryptedVoiceChat.port, new String[]{serverName});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());
        EncryptedVoiceChat.connectedToRoom = true;

        // error check

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ActiveChat.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //update stage when we get new person to join room


    }
}