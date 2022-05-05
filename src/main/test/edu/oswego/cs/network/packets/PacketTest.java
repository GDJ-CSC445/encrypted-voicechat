package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PacketTest {

    @Test
    public void participantDataParserTest() {
        ParticipantOpcode participantOpcode = ParticipantOpcode.JOIN;
        int port = 1551;
        ParticipantData expectedParticipantData = new ParticipantData(participantOpcode, port);

        Packet packet = Packet.parse(expectedParticipantData.getBytes());
        assertNotNull(packet, "Packet parser returned a NULL packet");

        assertTrue(packet instanceof ParticipantData);

        ParticipantData actualParticipantData = (ParticipantData) packet;
        assertArrayEquals(expectedParticipantData.getBytes(), actualParticipantData.getBytes(), "Packet bytes was not parsed correctly.");

        String[] parameters = new String[]{"encrypted", "voicechat", "server"};
        expectedParticipantData = new ParticipantData(participantOpcode, port, parameters);

        packet = Packet.parse(expectedParticipantData.getBytes());
        assertNotNull(packet, "Packet parser returned a NULL packet");

        actualParticipantData = (ParticipantData) packet ;
        assertArrayEquals(expectedParticipantData.getBytes(), actualParticipantData.getBytes(), "Packet bytes was not parsed correctly.");

    }

    @Test
    public void participantACKParserTest() {
        ParticipantOpcode participantOpcode = ParticipantOpcode.JOIN;
        int port = 1551;
        ParticipantACK expectedParticipantACK = new ParticipantACK(participantOpcode, port);

        Packet packet = Packet.parse(expectedParticipantACK.getBytes());
        assertNotNull(packet, "Packet parser returned a NULL packet");

        assertTrue(packet instanceof ParticipantACK);

        ParticipantACK actualParticipantData = (ParticipantACK) packet;
        assertArrayEquals(expectedParticipantACK.getBytes(), actualParticipantData.getBytes(), "Packet bytes was not parsed correctly.");

        String[] parameters = new String[]{"encrypted", "voicechat", "server"};
        expectedParticipantACK = new ParticipantACK(participantOpcode, port, parameters);

        packet = Packet.parse(expectedParticipantACK.getBytes());
        assertNotNull(packet, "Packet parser returned a NULL packet");

        actualParticipantData = (ParticipantACK) packet ;
        assertArrayEquals(expectedParticipantACK.getBytes(), actualParticipantData.getBytes(), "Packet bytes was not parsed correctly.");

    }

    @Test
    public void soundDataParserTest() {
        int port = 1551;
        byte[] soundData = new byte[]{1, 2, 3};
        SoundData expectedSoundData = new SoundData(port, soundData, 1);

        Packet packet = Packet.parse(expectedSoundData.getBytes());
        assertNotNull(packet, "Sound data parser returns null");

        assertTrue(packet instanceof SoundData);

        SoundData actualSoundData = (SoundData) packet;

        assertArrayEquals(expectedSoundData.getBytes(), actualSoundData.getBytes(), "Byte parsing is not correct.");
        assertArrayEquals(expectedSoundData.getData(), actualSoundData.getData(), "Sound data not parsed correctly.");

    }

}