package edu.oswego.cs.network.opcodes;

public enum ErrorOpcode {

    UNDEF(-1),
    CHATROOM_DNE(0),
    CHATROOM_FULL(1),
    CHATROOM_PASS_FALSE(2),
    CHATROOM_EXISTS(3);;

    private final int opcodeInt;

    ErrorOpcode(int opcodeInt) {
        this.opcodeInt = opcodeInt;
    }

    public int getInt() {
        return this.opcodeInt;
    }

    public static ErrorOpcode getOpcode(int opcodeInt) {
        switch (opcodeInt) {
            case  0: return CHATROOM_DNE;
            case  1: return CHATROOM_FULL;
            case  2: return CHATROOM_PASS_FALSE;
            case  3: return CHATROOM_EXISTS;

            default: return UNDEF;
        }
    }


}
