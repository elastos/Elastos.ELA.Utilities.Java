package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.framework.rpc.Rpc;

import java.util.HashMap;

import static org.elastos.ela.UsableUtxo.RPCURL;

public class ElaApi {

    public static String estimatesmartfee(JSONObject params){
        return Rpc.call("estimatesmartfee",params,RPCURL);
    }

    public static String getblockbyheight(JSONObject params){
        return Rpc.call("getblockbyheight",params,RPCURL);
    }

    public static String listunspent(JSONObject params){
        return Rpc.call("listunspent",params,RPCURL);
    }

    public static String getblock(JSONObject params){
        return Rpc.call("getblock",params,RPCURL);
    }

    public static String getrawtransaction(JSONObject params){
        return Rpc.call("getrawtransaction",params,RPCURL);
    }

    public static String sendrawtransaction(JSONObject params){
        return Rpc.call("sendrawtransaction",params,RPCURL);
    }

    public static String getblockhash(JSONObject params){
        return Rpc.call("getblockhash",params,RPCURL);
    }

    public static String discretemining(JSONObject params){
        return Rpc.call("discretemining",params,RPCURL);
    }

    public static String getreceivedbyaddress(JSONObject params){
        return Rpc.call("getreceivedbyaddress",params,RPCURL);
    }
    public static String getblockcount(){
        return Rpc.call("getblockcount",null,RPCURL);
    }
}