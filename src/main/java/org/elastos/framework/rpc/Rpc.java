package org.elastos.framework.rpc;

import net.sf.json.JSONObject;
import org.elastos.ela.HttpRequestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei@elastos.org
 * @time: 2018/1/22
 */
public class Rpc {
     //生成区块
     public static String generateBlock(int blockNum , String url){
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("count",String.valueOf(blockNum));
         return call("discretemining",map,url);
     }

     public static String call (String method ,Map params,String url){
         //构造json格式
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("method",method);
         map.put("params",params) ;

         //发送请求
         JSONObject jsonParam = new JSONObject();
         jsonParam.accumulateAll(map);
         System.out.println("jsonParam = "+ jsonParam);
         JSONObject jsonObject = HttpRequestUtil.httpPost(url, jsonParam);
         System.out.println("jsonObject : "+ jsonObject);
         String result =  jsonObject.getString("result");
         return result;
     }

    public static String getRpcAddress(String path , String port) {
        return path + ":" + port;
    }

    public static String call_ (String method ,Map params,String url){
        //构造json格式
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("method",method);
        map.put("params",params) ;

        //发送请求
        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
//        System.out.println("jsonParam = "+ jsonParam);
        JSONObject jsonObject = HttpRequestUtil.httpPost(url, jsonParam);
//        System.out.println("jsonObject : "+ jsonObject);

        return jsonObject.toString();
    }

}
