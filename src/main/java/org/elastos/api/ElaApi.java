package org.elastos.api;


import net.sf.json.JSONObject;
import org.elastos.common.Config;
import org.elastos.framework.rpc.Rpc;



public class ElaApi {

    public static String estimatesmartfee(JSONObject params){
        return Rpc.call("estimatesmartfee",params, Config.getRpcUrl());
    }

    public static String getblockbyheight(JSONObject params){
        return Rpc.call("getblockbyheight",params,Config.getRpcUrl());
    }

    public static String listunspent(JSONObject params){
        return Rpc.call("listunspent",params,Config.getRpcUrl());
    }

    public static String getblock(JSONObject params){
        return Rpc.call("getblock",params,Config.getRpcUrl());
    }

    public static String getrawtransaction(JSONObject params){
        return Rpc.call("getrawtransaction",params,Config.getRpcUrl());
    }

    public static String sendrawtransaction(JSONObject params){
        return Rpc.call("sendrawtransaction",params,Config.getRpcUrl());
    }

    public static String getblockhash(JSONObject params){
        return Rpc.call("getblockhash",params,Config.getRpcUrl());
    }

    public static String discretemining(JSONObject params){
        return Rpc.call("discretemining",params,Config.getRpcUrl());
    }

    public static String getreceivedbyaddress(JSONObject params){
        return Rpc.call("getreceivedbyaddress",params,Config.getRpcUrl());
    }
    public static String getblockcount(){
        return Rpc.call("getblockcount",null,Config.getRpcUrl());
    }
}