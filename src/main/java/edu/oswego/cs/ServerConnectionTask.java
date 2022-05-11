package edu.oswego.cs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnectionTask {

    private static Socket socket;
    public static boolean connected = false;
    public BooleanProperty connect = new SimpleBooleanProperty(this, "connected", false);

    public boolean getConnect() {
        return connect.get();
    }

    public BooleanProperty connectProperty() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect.set(connect);
    }


    public Task<Socket> getTask() {
        return task;
    }

    Task<Socket> task = new Task<Socket>() {
        @Override
        protected Socket call() throws Exception {
            while (!connectProperty().get()) {
                try {
                    socket = new Socket(EncryptedVoiceChat.connectionHost, EncryptedVoiceChat.connectionPort);
                    BufferedReader inport = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int port = Integer.parseInt(inport.readLine());
                    Thread.sleep(1000);
                    socket.close();
                    socket = new Socket(EncryptedVoiceChat.connectionHost, port);
                    setConnect(true);
                } catch (IOException | InterruptedException e) {
                    ServerConnection.displayError("Could not connect to " + EncryptedVoiceChat.connectionHost + " on port: " + EncryptedVoiceChat.connectionPort);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
            return socket;
        }
    };
}
