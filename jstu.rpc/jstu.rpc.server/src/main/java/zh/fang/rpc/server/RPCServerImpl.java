package zh.fang.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RPCServerImpl implements IRPCServer, Runnable {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 20838;

    private boolean running;

    private ExecutorService executorService;

    public RPCServerImpl() {
        this.running = false;
    }

    @Override
    public final void stop() {
        if(isRunning() && !executorService.isShutdown()){
            executorService.shutdown();
        }
    }

    @Override
    public final void start() throws Exception {
        if(isRunning()){
            return;
        }

        if(null == executorService){
            executorService = Executors.newSingleThreadExecutor();
        }

        executorService.execute(this);
    }

    @Override
    public final boolean isRunning() {
        return this.running;
    }

    @Override
    public final int getPort() {
        return PORT;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(HOST, PORT).sync();
            future.channel().closeFuture().sync();
        }
        catch (Exception er){}
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
