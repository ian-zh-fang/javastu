package zh.fang.rpc.server;

public interface IRPCServer {

    void stop();

    void start() throws Exception;

    boolean isRunning();

    int getPort();
}
