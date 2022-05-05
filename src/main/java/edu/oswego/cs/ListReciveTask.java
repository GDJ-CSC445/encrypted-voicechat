package edu.oswego.cs;

import edu.oswego.cs.network.packets.Packet;
import edu.oswego.cs.network.packets.ParticipantACK;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ListReciveTask {
    Parent root;
    final Socket finalSocket;
    private static PrintWriter out;
    private static InputStream in;

    public ListReciveTask(Parent root, Socket finalSocket) throws IOException {
        this.root = root;
        this.finalSocket = finalSocket;
        out = new PrintWriter(finalSocket.getOutputStream(), true);
        in = finalSocket.getInputStream();
    }

    Task<Void> task = new Task<Void>() { //this is for receving the list
        @Override
        protected Void call() throws Exception {
            while (finalSocket.isConnected()) {
                try {

                    byte[] buffer = new byte[1024];
                    in.read(buffer);
                    Packet packet = Packet.parse(buffer);
                    if (packet instanceof ParticipantACK) {
                        ParticipantACK participantACK = (ParticipantACK) packet;
                        for (String param : participantACK.getParams()) {
                            System.out.println(param);
                        }
                    }
                    buffer = new byte[]{};
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                }
            });
            return null;
        }
    };
}
