package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.passiveObjects.ServerMessage;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

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

        if(nextByte == '\0' & len > 2) {
            zeroCounter--;
            if(zeroCounter > 0)
                pushByte((byte)(32));
            if(zeroCounter == 0) {
                return popString();
            }
        }else
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
        String result = opcode;
        result += new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private void popOpCode(){
        short op1 = (short) (bytes[0] & 0xff);
        short op2 = (short)(bytes[1] & 0xff);
        opcode = "" +op1 + op2;
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
