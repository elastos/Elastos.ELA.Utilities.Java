package org.elastos.framework.rpc;

import net.sf.json.JSONArray;
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

    public static String call(String method ,Map params,String url){
        //构造json格式
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("method",method);
        map.put("params",params) ;

        //发送请求
        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        JSONObject jsonObject = HttpRequestUtil.httpPost(url, jsonParam);

        return jsonObject.toString();
    }

    public static String listunspent(String assetid, JSONArray addressList, String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("addresses",addressList);
        params.put("assetid",assetid);
        return call("listunspent",params,url);
    }

    public static String getblock(String blockHash, int verbosity,String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("blockhash",blockHash);
        params.put("verbosity",verbosity);
        return call("getblock",params,url);
    }

    public static String getrawtransaction(String txid,boolean verbose, String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("txid",txid);
        params.put("verbose",verbose);
        return call("getrawtransaction",params,url);
    }

    public static String getreceivedbyaddress(String address, String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("address",address);
        return call("getreceivedbyaddress",params,url);
    }


}
