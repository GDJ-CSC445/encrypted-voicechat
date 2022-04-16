package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;
import edu.oswego.cs.network.opcodes.ParticipantOpcode;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/*

  +-----------------------------------+--------------------------------------+----------+
  |                 |                 |                    |                |           |
  |      Opcode     |     Src Port    | Participant Opcode |     Param      |     0     |
  |                 |                 |                    |                |           |
  +-----------------------------------+--------------------------------------+----------+
        2 Bytes           2 Bytes            2 Bytes            N Bytes        1 Byte

 */


public class ParticipantData extends DataPacket {

    private final ParticipantOpcode participantOpcode;
    private final int port;
    private String[] params = new String[]{};

    public ParticipantData(ParticipantOpcode participantOpcode, int srcPort) {
        super(PacketOpcode.PARTICIPANT);
        this.participantOpcode = participantOpcode;
        this.port = srcPort;
    }

    public ParticipantData(ParticipantOpcode participantOpcode, int port, String[] params) {
        super(PacketOpcode.PARTICIPANT);
        this.participantOpcode = participantOpcode;
        this.port = port;
        this.params = params;
    }

    public ParticipantOpcode getParticipantOpcode() {
        return participantOpcode;
    }

    public String[] getParams() {
        return params;
    }

    @Override
    public byte[] getBytes() {
        List<Byte> byteList = new ArrayList<>();
        byte[] portBytes = new BigInteger(String.valueOf(port)).toByteArray();

        Collections.addAll( byteList, (byte) 0, (byte) opcode.getInt() );
        if (port > 255) byteList.addAll(Packet.arrayToList(portBytes));
        else               Collections.addAll(byteList, (byte) 0, (byte) port);
        Collections.addAll( byteList, (byte) 0, (byte) participantOpcode.getInt() );

        for (String param : params) {
            byteList.addAll(Packet.arrayToList(param.getBytes()));
            byteList.add( (byte) 0);
        }

        return Packet.listToArray(byteList);
    }
}
