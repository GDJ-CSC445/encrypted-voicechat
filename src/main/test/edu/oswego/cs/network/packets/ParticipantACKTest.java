package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantACKTest {

    @Test
    public void getBytesTest() {
        ParticipantOpcode participantOpcode = ParticipantOpcode.JOIN;
        int destPort = 255;

        ParticipantACK participantAck = new ParticipantACK(participantOpcode, destPort);
        byte[] expectedBytes = new byte[]{0, 4, 0, -1, 0, 0};

        Assertions.assertArrayEquals(expectedBytes, participantAck.getBytes(), "Packet ack for participant data is wrong.");

        participantAck = new ParticipantACK(participantOpcode, destPort, new String[]{"param"});
        expectedBytes = new byte[]{0, 4, 0, -1, 0, 0, 112, 97, 114, 97, 109, 0};

        Assertions.assertArrayEquals(expectedBytes, participantAck.getBytes(), "Packet ack is not parsing parameters correctly.");
    }

}