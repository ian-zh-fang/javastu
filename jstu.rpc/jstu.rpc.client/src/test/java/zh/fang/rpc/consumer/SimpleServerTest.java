package zh.fang.rpc.consumer;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zh.fang.rpc.server.interfaces.ISimpleService;

@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleServerTest {

    @Test
    public void rpcTest(){
        final RpcProxy proxy = new RpcProxy();
        final ISimpleService svr = proxy.create(ISimpleService.class);
        String result = svr.hello();
        assert null != result;
    }

    @Test
    public void helloTest(){
        System.out.print("hello world !");
        assert  true;
    }
}
