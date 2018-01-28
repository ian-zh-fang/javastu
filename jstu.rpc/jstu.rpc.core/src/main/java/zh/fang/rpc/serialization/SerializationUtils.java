package zh.fang.rpc.serialization;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializationUtils {

    private static Map<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<Class<?>, Schema<?>>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private SerializationUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Schema<T> getSchema(Class<T> clazz){
        Schema<T> schema = (Schema<T>) schemaMap.get(clazz);
        if(null == schema){
            schema = RuntimeSchema.createFrom(clazz);
            if(null != schema){
                schemaMap.put(clazz, schema);
            }
        }
        return schema;
    }

    public static <T> byte[] serialize(T obj){
        Class<T> clazz = (Class<T>)obj.getClass();
        LinkedBuffer buf = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try{
            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buf);
        }
        catch (Exception err){
            throw new IllegalStateException(err.getMessage(), err);
        }
        finally {
            buf.clear();
        }
    }

    public static <T> T deserialize(byte[] buf, Class<T> clazz){
        try{
            T msg = (T)objenesis.newInstance(clazz);
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(buf, msg, schema);
            return msg;
        }
        catch (Exception err){
            throw new IllegalStateException(err.getMessage(), err);
        }
    }
}
