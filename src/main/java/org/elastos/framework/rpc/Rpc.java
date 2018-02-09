package org.elastos.framework.rpc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.HttpRequestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mdj17 on 2018/1/23.
 */
public class Rpc {
     public static final String ASSETID = "a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0";

     //GenerateBlock 命令方式生成区块
     public static String generateBlock(int blockNum , String url){
         Object[] params = new Object[1];
         params[0] = blockNum;
         return call("discretemining",params,url);
     }

     //交易
     public static String sendTransaction(String address,String value,String fee ,String url){
         return sendTxWithLock(address,value,fee,url,"0");
     }

     //锁仓交易
     public static String sendTxWithLock(String address,String value,String fee ,String url, String lock){
        Object[] params = new Object[5];
        params[0] = ASSETID;
        params[1] = address;
        params[2] = value;
        params[3] = fee;
        params[4] = lock;
        return call("sendtransaction",params,url);
     }

     //批量交易
     public static String sendBatchOutTransaction(Map[] batchOut , String fee , String url){
         return sendBatchOutTxWithLock(batchOut,fee,url,"0");
     }

     //批量锁仓
    public static String sendBatchOutTxWithLock(Map[] batchOut ,String fee, String url, String lock){
        Object[] params = new Object[4];
        params[0] = ASSETID;
        params[1] = batchOut;
        params[2] = fee;
        params[3] = lock;
         return call("sendbatchouttransaction",params,url);
    }

     public static String call (String method ,Object[] params,String url){
         //构造json格式
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("method",method);
         map.put("id",21338);
         map.put("params",params) ;

         //发送请求
         JSONObject jsonParam = new JSONObject();
         jsonParam.accumulateAll(map);
         JSONObject jsonObject = HttpRequestUtil.httpPost(url, jsonParam);
         String result =  jsonObject.getString("result");
         return result;
     }

}
