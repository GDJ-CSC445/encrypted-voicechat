package edu.oswego.cs.network.packets;

import edu.oswego.cs.network.opcodes.PacketOpcode;
import edu.oswego.cs.network.opcodes.ParticipantOpcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Packet {

    protected final PacketOpcode opcode;

    public Packet(PacketOpcode opcode) {
        this.opcode = opcode;
    }

    abstract public byte[] getBytes();

    public static Packet parse(byte[] bytes) {
        PacketOpcode opcode = PacketOpcode.getOpcode( bytes[1] );

        switch (opcode) {
            case SOUND: ;
            case PARTICIPANT: return PacketFactory.parseParticipantDataPacket(bytes);
            case END: ;
            case ERR: ;
            case PARTICIPANT_ACK: return PacketFactory.parseParticipantACKPacket(bytes);
            default: ;
        }
        return null;
    }

    public PacketOpcode getOpcode() {
        return opcode;
    }

    protected static byte[] listToArray(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        for (int b = 0; b < byteList.size(); b++)
            bytes[b] = byteList.get(b);

        return bytes;
    }

    protected static List<Byte> arrayToList(byte[] bytes) {
        List<Byte> byteList = new ArrayList<>();
        for (byte b : bytes)
            byteList.add(b);

        return byteList;
    }

    private static class PacketFactory {

        public static ParticipantACK parseParticipantACKPacket(byte[] bytes) {
            int port = new BigInteger( new byte[]{bytes[2], bytes[3]} ).intValue();
            ParticipantOpcode participantOpcode = ParticipantOpcode.getOpcode( new BigInteger(new byte[]{bytes[4], bytes[5]}).intValue() );
            String [] parameters = null;
            if (bytes.length > 6) {
                byte[] subArray = Arrays.copyOfRange(bytes, 6, bytes.length);
                parameters = parseParam(subArray);
            }
            if (parameters != null) return new ParticipantACK(participantOpcode, port, parameters);
            return new ParticipantACK(participantOpcode, port);
        }

        public static ParticipantData parseParticipantDataPacket(byte[] bytes) {
            int port = new BigInteger( new byte[]{bytes[2], bytes[3]} ).intValue();
            ParticipantOpcode participantOpcode = ParticipantOpcode.getOpcode( new BigInteger(new byte[]{bytes[4], bytes[5]}).intValue() );
            String [] parameters = null;
            if (bytes.length > 6) {
                byte[] subArray = Arrays.copyOfRange(bytes, 6, bytes.length);
                parameters = parseParam(subArray);
            }
            if (parameters != null) return new ParticipantData(participantOpcode, port, parameters);
            return new ParticipantData(participantOpcode, port);
        }

        private static String[] parseParam(byte[] subArray) {
            ArrayList<String> parameters = new ArrayList<>();
            ArrayList<Byte> bytes = new ArrayList<>();
            for (byte b : subArray) {
                if (b == (byte) 0) {
                    if (bytes.size() == 0) break;
                    parameters.add(new String(listToArray(bytes)));
                    bytes = new ArrayList<>();
                    continue;
                }
                bytes.add(b);
            }
            return parameters.toArray(new String[0]);
        }

    }

}

