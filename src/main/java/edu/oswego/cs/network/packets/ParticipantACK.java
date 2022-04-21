package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;
import edu.oswego.cs.network.opcodes.ParticipantOpcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*

  +-----------------------------------+--------------------------------------+----------+
  |                 |                 |                    |                |           |
  |      Opcode     |     Dest Port   | Participant Opcode |     Param      |     0     |
  |                 |                 |                    |                |           |
  +-----------------------------------+--------------------------------------+----------+
        2 Bytes           2 Bytes            2 Bytes            N Bytes        1 Byte

 */

public class ParticipantACK extends Packet {

    private final ParticipantOpcode participantOpcode;
    private final int destPort;
    private String[] params = new String[]{};

    public ParticipantACK(ParticipantOpcode participantOpcode, int destPort) {
        super(PacketOpcode.PARTICIPANT_ACK);
        this.participantOpcode = participantOpcode;
        this.destPort = destPort;
    }

    public ParticipantACK(ParticipantOpcode participantOpcode, int destPort, String[] params) {
        super(PacketOpcode.PARTICIPANT_ACK);
        this.participantOpcode = participantOpcode;
        this.destPort = destPort;
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }

    @Override
    public byte[] getBytes() {
        List<Byte> byteList = new ArrayList<>();
        byte[] destPortBytes = new BigInteger(String.valueOf(destPort)).toByteArray();

        Collections.addAll( byteList, (byte) 0, (byte) opcode.getInt() );
        if (destPort > 255) byteList.addAll(Packet.arrayToList(destPortBytes));
        else               Collections.addAll(byteList, (byte) 0, (byte) destPort);

        Collections.addAll( byteList, (byte) 0, (byte) participantOpcode.getInt() );

        for (String param : params) {
            byteList.addAll(Packet.arrayToList(param.getBytes()));
            byteList.add( (byte) 0);
        }

        return Packet.listToArray(byteList);
    }
}
