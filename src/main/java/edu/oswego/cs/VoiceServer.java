package edu.oswego.cs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class VoiceServer {

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket clientSocket;
        BufferedOutputStream out;
        BufferedInputStream in;
        int port = 26900;
        File audio = new File("test.wav");

        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            System.out.println("Connected to client " + clientSocket.getInetAddress());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
            in = new BufferedInputStream(clientSocket.getInputStream());
            while(true) {
                in.read();
                System.out.println("Reading packet...");
                FileInputStream fin = new FileInputStream(audio);
                fin.readAllBytes();
            }


        } catch (IOException e) {
            System.out.println("Cannot create server socket connection.");
            e.printStackTrace();
        }
    }
}
