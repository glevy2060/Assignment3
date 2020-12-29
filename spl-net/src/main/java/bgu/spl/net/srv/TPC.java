package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;

import java.util.function.Supplier;

public class TPC extends BaseServer<String>{
    public TPC(int port, Supplier<MessagingProtocol<String>> protocolFactory, Supplier<MessageEncoderDecoder<String>> encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }

    @Override
    protected void execute(BlockingConnectionHandler<String> handler) {
        new Thread(handler).start();
    }
}
