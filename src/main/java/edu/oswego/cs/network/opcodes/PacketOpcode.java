package edu.oswego.cs.network.opcodes;

public enum PacketOpcode {

    UNDEF(-1),
    SOUND(0),
    PARTICIPANT(1),
    END(2),
    ERR(3),
    PARTICIPANT_ACK(4);

    private final int opcodeInt;

    PacketOpcode(int opcodeInt) {
        this.opcodeInt = opcodeInt;
    }

    public int getInt() {
        return this.opcodeInt;
    }

    public static PacketOpcode getOpcode(int opcodeInt) {
        switch (opcodeInt) {
            case -1: return ERR;
            case  0: return SOUND;
            case  1: return PARTICIPANT;
            case  2: return END;
            case  3: return ERR;
            case  4: return PARTICIPANT_ACK;
            default: return UNDEF;
        }
    }

}
