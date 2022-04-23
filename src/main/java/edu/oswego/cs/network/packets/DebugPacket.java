package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*

  +-----------------------------------+----------------+
  |                 |                 |                |
  |      Opcode     |     src Port    |       msg      |
  |                 |                 |                |
  +-----------------------------------+----------------+
        2 Bytes           2 Bytes           N Bytes

 */
public class DebugPacket extends Packet {
    private final int port;
    private final String msg;

    public DebugPacket(int port, String msg) {
        super(PacketOpcode.DEBUG);
        this.port = port;
        this.msg = msg;
    }

    public int getPort() {
        return port;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public byte[] getBytes() {
        List<Byte> byteList = new ArrayList<>();
        byte[] portBytes = new BigInteger(String.valueOf(port)).toByteArray();

        Collections.addAll(byteList , (byte) 0, (byte) opcode.getInt());
        if (port > 255) byteList.addAll(Packet.arrayToList(portBytes));
        else            Collections.addAll(byteList, (byte) 0, (byte) port);
        byteList.addAll(Packet.arrayToList(msg.getBytes()));

        return Packet.listToArray(byteList);
    }
}
