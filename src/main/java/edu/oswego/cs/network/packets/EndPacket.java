package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;

public class EndPacket extends Packet {

    public EndPacket() {
        super(PacketOpcode.END);
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
