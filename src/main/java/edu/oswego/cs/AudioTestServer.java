package edu.oswego.cs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class AudioTestServer {

    private static InputStream inStream;
    private static FileOutputStream fileOutStream;

    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(2700)) {
            System.out.println("Listening on port: " + socket.getLocalPort());
            File file = new File("new.wav");
            Socket clientSocket = socket.accept();

            if (file.createNewFile()) {
                inStream = clientSocket.getInputStream();
                ByteBuffer bbuf = ByteBuffer.allocate(2048);
                fileOutStream = new FileOutputStream(file);

                while (true) {
                    System.out.println("Writing..." + inStream.read());
                    fileOutStream.write(bbuf.array());

                }
            } else {
                file.delete();
                System.out.println("File deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
