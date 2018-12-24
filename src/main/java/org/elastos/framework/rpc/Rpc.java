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

    public static String getblockcount(String url){
        return call("getblockcount",null,url);
    }

    public static String getblockbyheight(int height,String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("height",height);
        return call("getblockbyheight",params,url);
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

    public static String sendrawtransaction(String data, String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("data",data);
        return call("sendrawtransaction",params,url);
    }

    public static String getblockhash(int height, String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("height",height);
        return call("getblockhash",params,url);
    }

    public static String discretemining(int count, String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("count",count);
        return call("discretemining",params,url);
    }

    public static String getreceivedbyaddress(String address, String url){
        HashMap<String, Object> params = new HashMap<>();
        params.put("address",address);
        return call("getreceivedbyaddress",params,url);
    }
}
