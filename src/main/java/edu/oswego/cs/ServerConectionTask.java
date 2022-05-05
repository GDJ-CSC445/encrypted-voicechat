/*
package edu.oswego.cs;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConectionTask {


    */
/*public boolean isConnet() {
        return connet.get();
    }

    public BooleanProperty connetProperty() {
        return connet;
    }

    public void setConnet(boolean connet) {
        this.connet.set(connet);
    }*//*


    public Task<Socket> getTask() {
        return task;
    }

    public void setTask(Task<Socket> task1) {
        this.task = task;
    }

    Task<Socket> task = new Task<Socket>() {
        @Override
        protected Socket call() throws Exception {
            while(!connetProperty().get()){
                try {
                    socket = new Socket("pi.cs.oswego.edu", 26990);
                    BufferedReader inport = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    int port = Integer.parseInt(inport.readLine());
                    Thread.sleep(1000);
                    socket.close();
                    socket = new Socket("pi.cs.oswego.edu", port);
                    setConnet(true);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return socket;
        }
    };
}
*/
