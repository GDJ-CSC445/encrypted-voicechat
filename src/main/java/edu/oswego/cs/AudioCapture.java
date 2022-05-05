package edu.oswego.cs;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioCapture {
    static final long FILE_LENGTH = 60000; //1 minute
    File wavFile = new File("audio_test.wav");
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine dataLine;

    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSize = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSize, channels, signed, bigEndian);
    }

    //capture sound from microphone and record into WAV file
    public void start() { ///dose this happens when the hit the mic button?
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            //checks if system supports data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported, setting Audio Format...");
                new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);
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

    //close line, complete capture
    public void finish() {
        dataLine.stop();
        dataLine.close();
        System.out.println("Record complete.");
    }

    public static void main (String[] args) {
        final AudioCapture ac = new AudioCapture();

        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(FILE_LENGTH);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Exception");
                    e.printStackTrace();
                }
                ac.finish();
            }
        });
        stopper.start();
        //start recording
        ac.start();
    }
}

