package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.ParticipantData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController2 {

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

    @FXML
    public void createServer(ActionEvent event) throws IOException {
        String serverName = ServerNameTextField.getText();

        ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, EncryptedVoiceChat.port, new String[]{serverName});
        EncryptedVoiceChat.socket_t.getOutputStream().write(participantData.getBytes());
    }

}