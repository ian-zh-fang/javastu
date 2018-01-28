package zh.fang.rpc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zh.fang.rpc.server.IRPCServer;
import zh.fang.rpc.server.RPCServerImpl;

@SpringBootApplication
@EnableAutoConfiguration
public class RpcServerApplication  {

    public static void main(String[] args){
        SpringApplication.run(RpcServerApplication.class, args);
        try{
            IRPCServer svr = new RPCServerImpl();
            svr.start();
        }
        catch (Exception err){
            System.out.print(err.getMessage());
        }
    }

}
