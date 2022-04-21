package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*

  +-----------------------------------+
  |                 |                 |
  |      Opcode     |     Src Port    |
  |                 |                 |
  +-----------------------------------+
        2 Bytes           2 Bytes

 */
public class EndPacket extends Packet {
    private final int port;
    public EndPacket(int port) {
        super(PacketOpcode.END);
        this.port = port;
    }

    @Override
    public byte[] getBytes() {
        List<Byte> byteList = new ArrayList<>();
        byte[] portBytes = new BigInteger(String.valueOf(port)).toByteArray();

        Collections.addAll(byteList , (byte) 0, (byte) opcode.getInt());
        if (port > 255) byteList.addAll(Packet.arrayToList(portBytes));
        else            Collections.addAll(byteList, (byte) 0, (byte) port);

        return Packet.listToArray(byteList);
    }
}
