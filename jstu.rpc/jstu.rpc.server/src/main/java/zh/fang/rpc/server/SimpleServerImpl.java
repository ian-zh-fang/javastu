package zh.fang.rpc.server;

import zh.fang.rpc.server.interfaces.ISimpleService;

public class SimpleServerImpl implements ISimpleService {
    @Override
    public String hello() {
        return "hi";
    }
}
