package zh.fang.rpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import zh.fang.rpc.dto.RpcRequest;
import zh.fang.rpc.dto.RpcResponse;
import zh.fang.rpc.nio.RpcDecoder;
import zh.fang.rpc.nio.RpcEncoder;
import zh.fang.rpc.server.interfaces.ISimpleService;

import java.util.HashMap;
import java.util.Map;

public class RpcServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Map<String, Object> handlers = new HashMap<String, Object>();

    public RpcServerChannelInitializer() {
        handlers.put(ISimpleService.class.getName(), new SimpleServerImpl());
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        channel.pipeline()
                .addLast(new RpcDecoder(RpcRequest.class))
                .addLast(new RpcEncoder(RpcResponse.class))
                .addLast(new RpcHandler(handlers));
    }
}
