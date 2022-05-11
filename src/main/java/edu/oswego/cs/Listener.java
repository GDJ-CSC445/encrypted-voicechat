package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.PacketOpcode;
import edu.oswego.cs.network.packets.Packet;
import edu.oswego.cs.network.packets.SoundPacket;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Listener extends Thread {

    private Socket s;
    private static volatile boolean socketBroken;

    @Override
    public void run() {
        socketBroken = false;
        while (EncryptedVoiceChat.connectedToRoom && ! socketBroken) {
            byte[] b = new byte[1024];
            try {
                EncryptedVoiceChat.socket.getInputStream().read(b);
            } catch (IOException e) {
                e.printStackTrace();
                socketBroken = true;
            }

            Packet p = Packet.parse(b);

            if (p instanceof SoundPacket) {
                if (p.getOpcode() == PacketOpcode.SACK) {
                    try {

                        ObjectInputStream objectInputStream = new ObjectInputStream(EncryptedVoiceChat.socket.getInputStream());
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

                    } catch (Exception e) {
                        e.printStackTrace();
                        socketBroken = true;
                    }

                }
            }
        }
    }

    public Listener(Socket s) {
        this.s = s;
    }

    public void setSocket(Socket s) {
        this.s = s;
    }

    public void setSocketBroken(boolean b) {
        socketBroken = b;
    }

}
