package zh.fang.rpc.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import zh.fang.rpc.serialization.SerializationUtils;

public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClazz;

    public RpcEncoder(Class<?> genericClazz){
        this.genericClazz = genericClazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if(this.genericClazz.isInstance(in)){
            byte[] buf = SerializationUtils.serialize(in);
            out.writeInt(buf.length);
            out.writeBytes(buf);
        }
    }
}
