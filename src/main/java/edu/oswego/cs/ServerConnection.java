package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.DebugPacket;
import edu.oswego.cs.network.packets.Packet;
import edu.oswego.cs.network.packets.ParticipantACK;
import edu.oswego.cs.network.packets.ParticipantData;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ServerConnection {

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
        new Thread(() -> {

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
        for (; ; ) {
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
            } else if (userIn.contains("JOIN")) {
                String serverName = userIn.split(" ")[1];
                ParticipantData participantData = new ParticipantData(ParticipantOpcode.JOIN, port, new String[]{serverName});
                socket.getOutputStream().write(participantData.getBytes());
                socket.getOutputStream().flush();
            }

        }
    }
}

