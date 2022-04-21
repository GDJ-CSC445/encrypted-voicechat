package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.ErrorOpcode;
import edu.oswego.cs.network.opcodes.PacketOpcode;

public class ErrorPacket extends Packet {

    private final ErrorOpcode errorOpcode;

    public ErrorPacket(ErrorOpcode errorOpcode) {
        super(PacketOpcode.ERR);
        this.errorOpcode = errorOpcode;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
