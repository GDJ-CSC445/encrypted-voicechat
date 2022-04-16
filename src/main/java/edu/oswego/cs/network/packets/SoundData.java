package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;

public class SoundData extends DataPacket {


    public SoundData() {
        super(PacketOpcode.SOUND);
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
