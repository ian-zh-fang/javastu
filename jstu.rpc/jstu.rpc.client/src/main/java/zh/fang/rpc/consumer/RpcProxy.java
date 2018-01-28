package zh.fang.rpc.consumer;

import zh.fang.rpc.dto.RpcRequest;
import zh.fang.rpc.dto.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcProxy {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 20838;

    public <T> T create(Class<?> clazz){
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setService(method.getDeclaringClass().getName());
                        request.setMethod(method.getName());
                        request.setParameterArr(method.getParameterTypes());
                        request.setParameterVals(args);

                        RpcHandler handler = new RpcHandler(HOST, PORT);
                        RpcResponse response = handler.send(request);

                        if(null != response.getError()){
                            throw response.getError();
                        }

                        return response.getResult();
                    }
                }
        );
    }
}
