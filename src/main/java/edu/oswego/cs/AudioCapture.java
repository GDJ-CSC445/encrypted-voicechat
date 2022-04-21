package edu.oswego.cs;

import edu.oswego.cs.network.packets.EndPacket;
import edu.oswego.cs.network.packets.SoundData;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class AudioCapture{
    static final long FILE_LENGTH = 5000; //60000; //1 minute
    String fileName = "audio_test.wav";
    File wavFile = new File(fileName);
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine dataLine;

    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSize = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        //return new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);
    }

    //capture sound from microphone and record into WAV file
    public void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            //checks if system supports data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported.");
                System.exit(0);
            }
            dataLine = (TargetDataLine) AudioSystem.getLine(info);
            dataLine.open(format);
            dataLine.start();
            System.out.println("Begin audio capture.");

            AudioInputStream ain = new AudioInputStream(dataLine);
            System.out.println("Begin recording.");
            AudioSystem.write(ain, fileType,wavFile);

        } catch (LineUnavailableException e) {
            System.out.println("Line not available.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error recording file.");
            e.printStackTrace();
        }
    }

    public void sendWavOverTCP(Socket socket) throws IOException, InterruptedException {
        byte[] wavBytes = Files.readAllBytes(Path.of(fileName));
        final int dataLimit = 506;
        int seqNumber = 0;
        while (wavBytes.length > 0) {
            if (wavBytes.length >= dataLimit) {
                byte[] tempBytes = Arrays.copyOfRange(wavBytes, 0, dataLimit + 1);
                wavBytes = Arrays.copyOfRange(wavBytes, dataLimit + 1, wavBytes.length);
                SoundData soundData = new SoundData(
                        socket.getPort(),
                        tempBytes,
                        seqNumber);
                System.out.println(Arrays.toString(soundData.getBytes()));
                socket.getOutputStream().write(soundData.getBytes());
//                Thread.sleep(500);
            } else {
                SoundData soundData = new SoundData(
                        socket.getPort(),
                        wavBytes,
                        seqNumber);
                System.out.println(Arrays.toString(soundData.getBytes()));
                socket.getOutputStream().write(soundData.getBytes());
//                Thread.sleep(750);
                wavBytes = new byte[]{};
            }
            seqNumber++;
        }
        socket.getOutputStream().write(new EndPacket(socket.getPort()).getBytes());
        System.out.println("sent end packet");
    }

    //close line, complete capture
    public void finish() {
        dataLine.stop();
        dataLine.close();
        System.out.println("Record complete.");
    }

    public static void main (String[] args) {
        final AudioCapture ac = new AudioCapture();

        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(FILE_LENGTH);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
                e.printStackTrace();
            }
            ac.finish();
            try {
                Socket socket = new Socket("localhost", 1500);
                ac.sendWavOverTCP(socket);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        stopper.start();
        //start recording
        ac.start();
    }

}

