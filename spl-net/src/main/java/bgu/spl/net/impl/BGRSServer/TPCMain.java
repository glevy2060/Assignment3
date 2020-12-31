package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.echo.EncoderDecoder;
import bgu.spl.net.impl.echo.MessageProtocol;
import bgu.spl.net.srv.TPC;

import java.util.function.Supplier;

public class TPCMain {

    public static void main(String[] args) {
        final int port = 7777;
        final Supplier<MessagingProtocol<String>> protocolFactory= new Supplier<MessagingProtocol<String>>() {
            @Override
            public MessagingProtocol<String> get(){return new MessageProtocol();}
        };
        final Supplier<MessageEncoderDecoder<String>> readerFactory=new Supplier<MessageEncoderDecoder<String>>() {
            @Override
            public MessageEncoderDecoder<String> get() {
                return new EncoderDecoder();
            }
        };

        TPC tpc = new TPC(port, protocolFactory, readerFactory);
        tpc.serve();
    }
}
