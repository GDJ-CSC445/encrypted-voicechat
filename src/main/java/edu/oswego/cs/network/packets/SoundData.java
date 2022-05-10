package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SoundData  implements Serializable {

    private final byte[] data;

    public SoundData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
