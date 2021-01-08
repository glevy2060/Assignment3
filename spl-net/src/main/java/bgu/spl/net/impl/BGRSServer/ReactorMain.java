package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.Database;
import bgu.spl.net.impl.echo.EncoderDecoder;
import bgu.spl.net.impl.echo.MessageProtocol;
import bgu.spl.net.srv.ActorThreadPool;
import bgu.spl.net.srv.Reactor;

import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class ReactorMain {

    public static void main(String[] args) {
        final int port = Integer.parseInt(args[0]);
        final Supplier<MessagingProtocol> protocolFactory=new Supplier<MessagingProtocol>() {
            @Override
            public MessagingProtocol<String> get(){return new MessageProtocol();}
        };
        final Supplier<MessageEncoderDecoder<String>> readerFactory=new Supplier<MessageEncoderDecoder<String>>() {
            @Override
            public MessageEncoderDecoder<String> get() {
                return new EncoderDecoder();
            }
        };

        Reactor reactor = new Reactor(10, port, protocolFactory, readerFactory);
        reactor.serve();


    }
}