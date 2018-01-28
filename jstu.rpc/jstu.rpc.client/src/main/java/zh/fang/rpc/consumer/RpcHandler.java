package zh.fang.rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import zh.fang.rpc.dto.RpcRequest;
import zh.fang.rpc.dto.RpcResponse;

public class RpcHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private final Object locker = new Object();

    private String host;

    private int port;

    private RpcResponse response;

    public RpcHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response = rpcResponse;
        synchronized (locker){
            locker.notifyAll();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public RpcResponse send(RpcRequest request) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcClientChannelInitializer(this))
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();

            synchronized (locker){
                locker.wait();
            }

            if(null != response){
                future.channel().closeFuture().sync();
            }

            return response;
        }
        catch (Exception err){
            System.out.print(err.getMessage());
            throw  err;
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
