package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.passiveObjects.ServerMessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncoderDecoder implements MessageEncoderDecoder<String> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private String opcode = "";
    private int zeroCounter = 10;
    private int numOfBytesRemaining = 0;

    @Override
    public String decodeNextByte(byte nextByte) {
        // opcode check and set all relevant parameters
        if(len == 2){
            popOpCode();
        }

        if(numOfBytesRemaining==0 && zeroCounter==0)
            return popString();

        if(numOfBytesRemaining > 0 && zeroCounter ==0)
            numOfBytesRemaining--;

        if(nextByte == '\0')
            zeroCounter --;

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(String message) {
        return message.getBytes(StandardCharsets.UTF_8);
        //first 2 bytes represent the opcode of ERROR/ACK
        //second two bytes represent the commant opcode (1-11)
        //message length until $
        //message
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private String popString() {
        String result = new String(bytes, 2, len, StandardCharsets.UTF_8);

        len = 0;
        return result;
    }

    private void popOpCode(){
        opcode = new String(bytes, 0, 2, StandardCharsets.UTF_8);
        setZeroCounter();
    }

    private void setZeroCounter(){
        if(opcode.equals("01") || opcode.equals("02") ||opcode.equals("03"))
            zeroCounter = 2;
        else if(opcode.equals("04") ||opcode.equals("11"))
            zeroCounter = 0;
        else if(opcode.equals("05") || opcode.equals("06") || opcode.equals("07")
                || opcode.equals("09") || opcode.equals("10")) {
            zeroCounter = 0;
            numOfBytesRemaining = 2;
        }else if(opcode.equals("08"))
            zeroCounter = 1;
    }

}
