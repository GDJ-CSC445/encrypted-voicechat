package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParticipantDataTest {

    @Test
    public void getByteTest() {
        ParticipantOpcode participantOpcode = ParticipantOpcode.JOIN;
        int srcPort = 255;

        ParticipantData participantData = new ParticipantData(participantOpcode, srcPort);
        byte[] expectedBytes = new byte[]{0, 1, 0, -1, 0, 0};

        Assertions.assertArrayEquals(expectedBytes, participantData.getBytes(), "Packet data for participant data is wrong.");

        participantData = new ParticipantData(participantOpcode, srcPort, new String[]{"param"});
        expectedBytes = new byte[]{0, 1, 0, -1, 0, 0, 112, 97, 114, 97, 109, 0};

        Assertions.assertArrayEquals(expectedBytes, participantData.getBytes(), "Packet data is not parsing parameters correctly.");
    }

}
