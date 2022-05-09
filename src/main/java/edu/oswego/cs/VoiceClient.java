package edu.oswego.cs;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class VoiceClient {

    AudioCapture audioCapture;
    Socket socket;
    String host = "127.0.0.1";
    int port = 26900;
    BufferedOutputStream outStream;
    BufferedInputStream inStream;

    public VoiceClient() {
        try {
            socket = new Socket(host, port);
            outStream = new BufferedOutputStream(socket.getOutputStream());
            inStream = new BufferedInputStream(socket.getInputStream());
            if (socket.isConnected()) {
//                inStream = new BufferedInputStream(new FileInputStream(soundFile));
//                play(inStream);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private synchronized void play(final InputStream in) throws Exception {
        AudioInputStream ain = AudioSystem.getAudioInputStream(in);
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(ain);
            clip.start();
            Thread.sleep(100);
            clip.drain();
        }

    }
}
