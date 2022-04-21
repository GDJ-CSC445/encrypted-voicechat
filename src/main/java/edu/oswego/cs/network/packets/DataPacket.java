package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;

public abstract class DataPacket extends Packet {
    
    public DataPacket(PacketOpcode opcode) {
        super(opcode);
    }
    
}
