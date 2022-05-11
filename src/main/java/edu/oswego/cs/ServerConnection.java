package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.PacketOpcode;
import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;


public class ServerConnection {
    private static Socket socket;
    Socket finalSocket;
    private static int port;
    private static PrintWriter out;
    private static InputStream in;
    public static boolean connected = false;
    public BooleanProperty connet = new SimpleBooleanProperty(this, "connected", false);

    static String connectionHost = "moxie.cs.oswego.edu";
    static int connectionPort = 15550;

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
    public static final String TEXT_RED = "\u001B[31m";
    private static final String TEXT_RESET = "\u001B[0m";

    public static void displayInfo(String msg) {
        System.out.println(TEXT_GREEN + "[INFO]" + TEXT_RESET + " " + msg);
    }

    public static void displayError(String error) {
        System.out.println(TEXT_RED + "[ERROR]" + TEXT_RESET + " " + error);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket(connectionHost, connectionPort);
        BufferedReader inport = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        int port = Integer.parseInt(inport.readLine());
        Thread.sleep(1000);
        socket.close();
        socket = new Socket(connectionHost, port);
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
                    else if (packet instanceof SoundPacket) {
                        if (packet.getOpcode() == PacketOpcode.SACK) {
                            try {
                                ObjectInputStream objectInputStream = new ObjectInputStream(finalSocket.getInputStream());
                                byte[] soundData = (byte[]) objectInputStream.readObject();
                                FileOutputStream fileOut = new FileOutputStream("received_audio.wav");
                                fileOut.write(soundData);
//
                                fileOut.close();

                                AudioInputStream stream = AudioSystem.getAudioInputStream(new File("received_audio.wav"));
                                AudioFormat format = stream.getFormat();
                                DataLine.Info info = new DataLine.Info(Clip.class, format);
                                Clip clip = (Clip) AudioSystem.getLine(info);
                                clip.open(stream);
                                clip.start();

                            } catch (Exception e) {e.printStackTrace();}

                        }
                    }
                    else if (packet instanceof DebugPacket) {
                        DebugPacket debugPacket = (DebugPacket) packet;
                        displayInfo("Debug Message From PORT " + debugPacket.getPort() + "\t" + debugPacket.getMsg());
                    }
                    else if (packet instanceof ErrorPacket) {
                        ErrorPacket errorPacket = (ErrorPacket) packet;
                        displayError(errorPacket.getErrorOpcode() + "; " + errorPacket.getErrorMsg());
                    }

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
                ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, port, Arrays.copyOfRange(params, 1, params.length));
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
            else if (userIn.contains("LEAVE")) {
                ParticipantData participantData = new ParticipantData(ParticipantOpcode.LEAVE, port);
                socket.getOutputStream().write(participantData.getBytes());
                socket.getOutputStream().flush();
            }
            else if (userIn.contains("TALK")) {
                AudioCapture audioCapture = new AudioCapture();
                new Thread(audioCapture::startCapture).start();
                Thread.sleep(5000);
                audioCapture.stopCapture();

                FileInputStream fileIn = new FileInputStream("audio.wav");
                SoundPacket soundPacket = new SoundPacket(PacketOpcode.SRQ, port);
                socket.getOutputStream().write(soundPacket.getBytes());

                Thread.sleep(1000);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(fileIn.readAllBytes());
            }

        }
    }
}

