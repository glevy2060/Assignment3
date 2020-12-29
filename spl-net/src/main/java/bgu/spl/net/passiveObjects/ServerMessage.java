package bgu.spl.net.passiveObjects;

public class ServerMessage {
    protected int opcode;
    protected String messageOpcode;
    protected String msg;

    public ServerMessage(int opcode, int messageOpcode, String msg){
        this.messageOpcode="";
        if(messageOpcode<10)
            this.messageOpcode="0";
        this.messageOpcode+=messageOpcode;
        this.opcode = opcode;
        this.msg = msg;
    }

    public String toString(){
        if(messageOpcode.equals("12"))
            msg = msg + "/0";
        return messageOpcode+opcode+msg;
    }


    public int getOpcode() {
        return opcode;
    }

    public String getMessageOpcode() {
        return messageOpcode;
    }
}
