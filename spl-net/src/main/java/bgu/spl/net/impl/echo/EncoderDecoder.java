package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.passiveObjects.ServerMessage;

import java.nio.charset.StandardCharsets;

public class EncoderDecoder implements MessageEncoderDecoder<ServerMessage> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public ServerMessage decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(ServerMessage message) {
        return message.toString().getBytes(StandardCharsets.UTF_8);
        //first 2 bytes represent the opcode of ERROR/ACK
        //second two bytes represent the commant opcode (1-11)
        //message length until $
        //message
    }
}
