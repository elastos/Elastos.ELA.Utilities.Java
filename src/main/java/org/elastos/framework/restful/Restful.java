package org.elastos.framework.restful;

import net.sf.json.JSONObject;
import org.elastos.ela.HttpRequestUtil;
import org.elastos.framework.node.Runner;

/**
 * Created by mdj17 on 2018/1/25.
 */
public class Restful {

    public interface api{
        String API_SENDRAWTRANSACTION   = "/api/v1/transaction";
        String API_GETBLOCKBYHEIGHT     = "/api/v1/block/details/height/";
        String API_GETBLOCKHEIGHT       = "/api/v1/block/height";
    }

    //获取区块高度
    public static Integer getBestHeight(String restfulUrl){
        String path = restfulUrl + "/api/v1/block/height";
        //发送请求
        JSONObject jsonObject = HttpRequestUtil.httpGet(path);
        if("Success".equals(jsonObject.get("Desc"))) {
            Integer height = (Integer) jsonObject.get("Result");
            return height;
        }
        return  0;
    }

    //根据地址获取余额
    public static Double getBalance(int nodeNum,String address){

        String restfulUrl = (String) Runner.nodeList.get(0).get("restful" + nodeNum);

        String path = restfulUrl + "/api/v1/asset/balances/" + address;
        //发送请求
        JSONObject jsonObject = HttpRequestUtil.httpGet(path);
        if("Success".equals(jsonObject.get("Desc"))) {
            String balance = (String) jsonObject.get("Result");
            return Double.valueOf(balance);
        }
        return  0.0;
    }

//    public static String sendRawTransaction(String restfulUrl,String signHash){
//        String path = restfulUrl + "/api/v1/transaction" + "/" + signHash;
//        //发送请求
//        JSONObject jsonObject = HttpRequestUtil.httpGet(path);
//        if("Success".equals(jsonObject.get("Desc"))) {
//            Integer height = (Integer) jsonObject.get("Result");
//            return height;
//        }
//        return  "";
//    }

    public static String getRestfulAddress(String path , String port) {
        return path + ":" + port;
    }
}

