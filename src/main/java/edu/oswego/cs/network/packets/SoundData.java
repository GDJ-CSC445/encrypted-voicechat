package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*

  +-----------------------------------+-----------+----------------+
  |                 |                 |           |                |
  |      Opcode     |     Src Port    |  Seq Num  |      Data      |
  |                 |                 |           |                |
  +-----------------------------------+-----------+----------------+
        2 Bytes           2 Bytes        2 Bytes         N Bytes

 */
public class SoundData extends DataPacket {
    private final int port;
    private final byte[] data;
    private final int sequenceNumber;

    public SoundData(int port, byte[] data, int sequenceNumber) {
        super(PacketOpcode.SOUND);
        this.port = port;
        this.data = data;
        this.sequenceNumber = sequenceNumber;
    }

    public byte[] getData() {
        return data;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getPort() {
        return port;
    }

    @Override
    public byte[] getBytes() {
        List<Byte> byteList = new ArrayList<>();
        byte[] portBytes = new BigInteger(String.valueOf(port)).toByteArray();
        byte[] sequenceBytes = new BigInteger(String.valueOf(sequenceNumber)).toByteArray();

        Collections.addAll(byteList , (byte) 0, (byte) opcode.getInt());
        if (port > 255) byteList.addAll(Packet.arrayToList(portBytes));
        else            Collections.addAll(byteList, (byte) 0, (byte) port);
        if (sequenceNumber > 255) byteList.addAll(Packet.arrayToList(sequenceBytes));
        else                      Collections.addAll(byteList, (byte) 0, (byte) sequenceNumber);
        byteList.addAll(Packet.arrayToList(data));

        return Packet.listToArray(byteList);
    }
}
