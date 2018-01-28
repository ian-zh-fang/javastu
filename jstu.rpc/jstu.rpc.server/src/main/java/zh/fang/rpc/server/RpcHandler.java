package zh.fang.rpc.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import zh.fang.rpc.dto.RpcRequest;
import zh.fang.rpc.dto.RpcResponse;

import java.util.Map;

public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> handlers;

    public RpcHandler(Map<String, Object> handlers){
        this.handlers = handlers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try{
            Object result = handle(request);
            response.setResult(result);
        }
        catch (Throwable err){
            response.setError(err);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest request) throws Throwable{
        String name = request.getService();
        Object service = handlers.get(name);

        Class<?> clazz = service.getClass();
        String method = request.getMethod();
        Class<?>[] parameterArr = request.getParameterArr();
        Object[] parameterVals = request.getParameterVals();

        FastClass fastClazz = FastClass.create(clazz);
        FastMethod fastMethod = fastClazz.getMethod(method, parameterArr);
        return fastMethod.invoke(service, parameterVals);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.print(cause.getMessage());
        ctx.close();
    }
}
