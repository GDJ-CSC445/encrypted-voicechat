package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.DebugPacket;
import edu.oswego.cs.network.packets.Packet;
import edu.oswego.cs.network.packets.ParticipantACK;
import edu.oswego.cs.network.packets.ParticipantData;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ServerConnection implements Runnable {
    private static Socket socket;
    Socket finalSocket;
    private static int port;
    private static PrintWriter out;
    private static InputStream in;
    public static boolean connected = false;
    public BooleanProperty connet = new SimpleBooleanProperty(this, "connected", false);

    public boolean isConnet() {
        return connet.get();
    }

    public BooleanProperty connetProperty() {
        return connet;
    }

    public static int getPort() {
        return port;
    }

    public static Socket getSocket() {
        return socket;
    }

    public void setConnet(boolean connet) {
        this.connet.set(connet);
    }

    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_RESET = "\u001B[0m";

    public static void displayInfo(String msg) {
        System.out.println(TEXT_GREEN + "[INFO]" + TEXT_RESET + " " + msg);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("moxie.cs.oswego.edu", 15551);
        BufferedReader inport = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int port = Integer.parseInt(inport.readLine());
        Thread.sleep(1000);
        socket.close();
        socket = new Socket("moxie.cs.oswego.edu", port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        InputStream in = socket.getInputStream();

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
                    if (packet instanceof DebugPacket) {
                        DebugPacket debugPacket = (DebugPacket) packet;
                        displayInfo("Debug Message From PORT " + debugPacket.getPort() + "\t" + debugPacket.getMsg());
                    }
                    buffer = new byte[]{};
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        for (;;) {
            String userIn = scanner.nextLine();
            if (userIn.contains("DEBUG")) {
                String msg = userIn.substring(6);
                DebugPacket debugPacket = new DebugPacket(port, msg);
                socket.getOutputStream().write(debugPacket.getBytes());
                socket.getOutputStream().flush();
            }
            if (userIn.contains("CREATE")) {
                String[] params = userIn.split(" ");
                ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, port, new String[]{params[1]});
                socket.getOutputStream().write(participantData.getBytes());
                socket.getOutputStream().flush();
            } else if (userIn.contains("LIST")) {
                ParticipantData participantData = new ParticipantData(ParticipantOpcode.LIST_SERVERS, port);
                socket.getOutputStream().write(participantData.getBytes());
                socket.getOutputStream().flush();
            }
            else if (userIn.contains("JOIN")) {
                String serverName = userIn.split(" ")[1];
                ParticipantData participantData = new ParticipantData(ParticipantOpcode.JOIN, port, new String[]{serverName});
                socket.getOutputStream().write(participantData.getBytes());
                socket.getOutputStream().flush();
            }

        }
    }

    @Override
    public void run() {
        while (!connetProperty().get()) {
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
        /// this has to be first and has to be on menu... ping into maxie or what not then allow button press.

        Socket finalSocket = socket;
        new Thread(() -> {

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
        }).start();

        Scanner scanner = new Scanner(System.in);

        for (; ; ) {
            try {
                String userIn = scanner.nextLine();                 // this is to correlate with the Create server button
                if (userIn.contains("CREATE")) {                    //
                    String[] params = userIn.split(" ");
                    ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, port, new String[]{params[1]});
                    socket.getOutputStream().write(participantData.getBytes());
                    socket.getOutputStream().flush();
                } else if (userIn.contains("LIST")) {                     // this is to correlate with the List server button
                    ParticipantData participantData = new ParticipantData(ParticipantOpcode.LIST_SERVERS, port);
                    socket.getOutputStream().write(participantData.getBytes());
                    socket.getOutputStream().flush();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    Task<Socket> task1 = new Task<Socket>() {
        @Override
        protected Socket call() throws Exception {
            while (!connetProperty().get()) {
                try {
                    socket = new Socket("pi.cs.oswego.edu", 26990);
                    BufferedReader inport = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    port = Integer.parseInt(inport.readLine());
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

    Task<Void> task2 = new Task<Void>() { //this is for receving the list
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


