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
        return messageOpcode+opcode+msg.length()+"$"+msg;
    }


    public int getOpcode() {
        return opcode;
    }

    public String getMessageOpcode() {
        return messageOpcode;
    }
}
