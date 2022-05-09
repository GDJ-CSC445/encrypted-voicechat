package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.ErrorOpcode;
import edu.oswego.cs.network.opcodes.PacketOpcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorPacket extends Packet {

    private final ErrorOpcode errorOpcode;
    private String errorMsg = null;

    public ErrorPacket(ErrorOpcode errorOpcode) {
        super(PacketOpcode.ERR);
        this.errorOpcode = errorOpcode;
    }

    public ErrorPacket(ErrorOpcode errorOpcode, String errorMsg) {
        super(PacketOpcode.ERR);
        this.errorOpcode = errorOpcode;
        this.errorMsg = errorMsg;
    }

    public ErrorOpcode getErrorOpcode() {
        return errorOpcode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public byte[] getBytes() {
        List<Byte> byteList = new ArrayList<>();

        Collections.addAll(byteList , (byte) 0, (byte) opcode.getInt());
        Collections.addAll(byteList , (byte) 0, (byte) errorOpcode.getInt());
        if (errorMsg != null) byteList.addAll(Packet.arrayToList(errorMsg.getBytes()));

        return Packet.listToArray(byteList);
    }
}
