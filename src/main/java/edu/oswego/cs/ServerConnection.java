//package edu.oswego.cs;
//
//import edu.oswego.cs.network.opcodes.ParticipantOpcode;
//import edu.oswego.cs.network.packets.Packet;
//import edu.oswego.cs.network.packets.ParticipantACK;
//import edu.oswego.cs.network.packets.ParticipantData;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class ServerConnection {
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        Socket socket = new Socket("localhost", 26900);
//        BufferedReader inport = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        int port = Integer.parseInt(inport.readLine());
//        Thread.sleep(1000);
//        socket.close();
//        socket = new Socket("localhost", port);
//        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//        InputStream in = socket.getInputStream();
//
//        Socket finalSocket = socket;
//        new Thread( ()-> {
//
//            while (finalSocket.isConnected()) {
//                try {
//
//                    byte[] buffer = new byte[8192];
//                    int count;
//                    in.read(buffer);
//                    while((count = in.read(buffer)) > 0) {
//                        Packet packet = Packet.parse(buffer);
//                        if (packet instanceof ParticipantACK) {
//                            ParticipantACK participantACK = (ParticipantACK) packet;
//                            for (String param : participantACK.getParams()) {
//                                System.out.println(param);
//                            }
//                        }
//                        out.write(buffer, 0, count);
//                    }
//
//                    buffer = new byte[]{};
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        Scanner scanner = new Scanner(System.in);
//        for (; ; ) {
//            String userIn = scanner.nextLine();
//            if (userIn.contains("CREATE")) {
//                String[] params = userIn.split(" ");
//                ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, port, new String[]{params[1]});
//                socket.getOutputStream().write(participantData.getBytes());
//                socket.getOutputStream().flush();
//            }
//            else if (userIn.contains("LIST")) {
//                ParticipantData participantData = new ParticipantData(ParticipantOpcode.LIST_SERVERS, port);
//                socket.getOutputStream().write(participantData.getBytes());
//                socket.getOutputStream().flush();
//            }
//        }
//    }
//
//}
