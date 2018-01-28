package zh.fang.rpc.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import zh.fang.rpc.serialization.SerializationUtils;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClazz;

    public RpcDecoder(Class<?> genericClazz) {
        this.genericClazz = genericClazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readables = in.readableBytes();
        if(4 > readables){
            return;
        }

        in.markReaderIndex();
        int length = in.readInt();
        if(0 > length){
            ctx.close();
        }

        if(length > readables){
            in.resetReaderIndex();
            return;
        }

        byte[] buf = new byte[length];
        in.readBytes(buf);

        Object obj = SerializationUtils.deserialize(buf, genericClazz);
        out.add(obj);
    }
}
