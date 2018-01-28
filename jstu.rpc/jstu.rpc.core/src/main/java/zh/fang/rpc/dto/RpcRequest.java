package zh.fang.rpc.dto;

import java.io.Serializable;

public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -1818686256934451267L;

    private String requestId;

    private String service;

    private String method;

    private Class<?>[] parameterArr;

    private Object[] parameterVals;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterArr() {
        return parameterArr;
    }

    public void setParameterArr(Class<?>[] parameterArr) {
        this.parameterArr = parameterArr;
    }

    public Object[] getParameterVals() {
        return parameterVals;
    }

    public void setParameterVals(Object[] parameterVals) {
        this.parameterVals = parameterVals;
    }
}
