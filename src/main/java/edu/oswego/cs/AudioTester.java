package edu.oswego.cs;

public class AudioTester {

    public static void main (String[] args) {
        final AudioCapture ac = new AudioCapture();

        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted Exception");
                    e.printStackTrace();
                }
                ac.stopCapture();
            }
        });
        stopper.start();

        //start recording
        ac.startCapture();
        ac.sendWavOverTCP();
    }
}
