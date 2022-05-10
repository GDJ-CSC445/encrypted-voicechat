package edu.oswego.cs;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class VoiceClient {

    public static void main(String[] args) {      // this looks to be the client? like when ur actually in the server?
        Socket socket;
        String host = "localhost";
        int port = 15555;
        BufferedOutputStream outStream;

        try {
            AudioCapture audioCapture = new AudioCapture();
            socket = new Socket(host, port);
            outStream = new BufferedOutputStream(socket.getOutputStream());

            //begin recording
//            audioCapture.start();

            byte[] buf = new byte[1024];
            int inBytes = audioCapture.dataLine.read(buf, 0, buf.length);
            outStream.write(buf, 0, inBytes);

            //commence recording
//            audioCapture.finish();


        } catch (UnknownHostException e) {
            System.out.println("Unknown host."); //this happens where?
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
