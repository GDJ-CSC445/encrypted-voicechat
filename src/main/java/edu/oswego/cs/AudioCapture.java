package edu.oswego.cs;

import edu.oswego.cs.network.packets.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Audio Capture class to record audio input from user and save audio to .WAV file. This audio
 * will be sent over a TCP connection to members of a voice chat room.
 */
public class AudioCapture {

    private AudioInputStream audioIn;
    private AudioFormat audioFormat;
    private double audioDuration;
    public Thread thread;
    public TargetDataLine dataLine;
    String fileName = "audio.wav";
    File wavFile = new File(fileName);


    /**
     * Audio Format must be set to tell system how to interpret and handle incoming bits
     */
    public AudioFormat setAudioFormat() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 1;
        int sampleSize = 16;
        boolean bigEndian = true;
        return new AudioFormat(encoding, rate, sampleSize, channels,
                ((sampleSize/8) * channels), rate, bigEndian);
    }

    public void getTDL() {
        DataLine.Info dlInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(dlInfo)) {
            System.out.println("Data line not supported.");
            return;
        }
        try {
            dataLine = (TargetDataLine) AudioSystem.getLine(dlInfo);
            dataLine.open(audioFormat, dataLine.getBufferSize());
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Begin audio capture and record
     */
    public void startCapture() {
        try {
            audioFormat = setAudioFormat();
            getTDL();
            dataLine.start();
            System.out.println("Starting audio capture.");
            audioIn = new AudioInputStream(dataLine);
            System.out.println("Starting recording audio input.");
            AudioSystem.write(audioIn, AudioFileFormat.Type.WAVE, wavFile);
        } catch (IOException e) {
            System.out.println("Unable to write to file.");
            e.printStackTrace();
        }
    }

    /**
     * Terminate capture and prepare to send audio packets
     */
    public void stopCapture() {
        dataLine.stop();
        dataLine.close();
        System.out.println("Record complete");
    }

//    public void sendWavOverTCP(Socket socket) throws IOException {
//        byte[] wavBytes = Files.readAllBytes(Path.of(fileName));
//        final int dataLimit = (wavBytes.length/8) + 1;
//        int seqNumber = 0;
//        while (wavBytes.length > 0) {
//            if (wavBytes.length >= dataLimit) {
//                System.out.println(wavBytes.length);
//                byte[] tempBytes = Arrays.copyOfRange(wavBytes, 0, dataLimit + 1);
//                wavBytes = Arrays.copyOfRange(wavBytes, dataLimit + 1, wavBytes.length);
//                SoundData soundData = new SoundData(socket.getPort(), tempBytes, seqNumber);
////                System.out.println(Arrays.toString(soundData.getBytes()));
//                socket.getOutputStream().write(soundData.getBytes());
////                Thread.sleep(500);
//            } else {
//                SoundData soundData = new SoundData(socket.getPort(), wavBytes, seqNumber);
////                System.out.println(Arrays.toString(soundData.getBytes()));
//                socket.getOutputStream().write(soundData.getBytes());
////                Thread.sleep(750);
//                wavBytes = new byte[]{};
//            }
//            seqNumber++;
//        }
//        socket.getOutputStream().write(new EndPacket(socket.getPort()).getBytes());
//        System.out.println("sent end packet");
//    }
}
