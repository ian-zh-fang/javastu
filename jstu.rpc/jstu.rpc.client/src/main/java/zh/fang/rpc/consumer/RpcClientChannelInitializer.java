package zh.fang.rpc.consumer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import zh.fang.rpc.dto.RpcRequest;
import zh.fang.rpc.dto.RpcResponse;
import zh.fang.rpc.nio.RpcDecoder;
import zh.fang.rpc.nio.RpcEncoder;

public class RpcClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcHandler handler;

    public RpcClientChannelInitializer(RpcHandler handler){
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline()
                .addLast(new RpcDecoder(RpcResponse.class))
                .addLast(new RpcEncoder(RpcRequest.class))
                .addLast(handler);
    }
}
