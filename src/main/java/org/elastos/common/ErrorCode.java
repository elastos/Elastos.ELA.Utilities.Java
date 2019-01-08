package org.elastos.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;

public class ErrorCode {
    public static String getError(int code, String msg) {

        HashMap m = new HashMap();
        m.put("code",code);
        m.put("id",null);
        m.put("message",msg);

        HashMap map = new HashMap();
        map.put("error", m);
        map.put("jsonrpc", "2.0");
        map.put("result", null);
        return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    public static String paramNotNull(String msg){
     return getError(-32601, msg + " not exist");
    }

    public static String invalidParam(String msg){
        return getError(-32602, "Invalid params : " + msg);
    }

    public static String exceptionError(String msg){
        return getError(-32604, "Exception Error : " + msg);
    }

    public static String ParamErr(String msg) {
        return getError(-32605, msg);
    }
}
