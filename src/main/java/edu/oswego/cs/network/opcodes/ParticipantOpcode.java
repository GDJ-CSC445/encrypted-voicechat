package edu.oswego.cs.network.opcodes;

public enum ParticipantOpcode {

    UNDEF(-1),
    JOIN(0),
    OTHER_JOIN(1),
    CREATE_SERVER(2),
    LIST_SERVERS(3),
    LEAVE(4);
    private final int opcodeInt;

    ParticipantOpcode(int opcodeInt) {
        this.opcodeInt = opcodeInt;
    }

    public int getInt() {
        return this.opcodeInt;
    }

    public static ParticipantOpcode getOpcode(int opcodeInt) {
        switch (opcodeInt) {
            case  0: return JOIN;
            case  1: return OTHER_JOIN;
            case  2: return CREATE_SERVER;
            case  3: return LIST_SERVERS;
            case  4: return LEAVE;
            default: return UNDEF;
        }
    }


}
