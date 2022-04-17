package edu.oswego.cs.network.opcodes;

public enum ErrorOpcode {

    UNDEF(-1),
    CHATROOM_DNE(0),
    CHATROOM_FULL(1),
    CHATROOM_PASS_FALSE(2);

    private final int opcodeInt;

    ErrorOpcode(int opcodeInt) {
        this.opcodeInt = opcodeInt;
    }

    public int getInt() {
        return this.opcodeInt;
    }

    public static ErrorOpcode getOpcode(int opcodeInt) {
        switch (opcodeInt) {
            default: return UNDEF;
        }
    }


}
