package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.framework.rpc.Rpc;

import static org.elastos.ela.UsableUtxo.RPCURL;

public class ElaApi {


    public static String getblockcount(){
        return Rpc.getblockcount(RPCURL);
    }

    public static String getblockbyheight (JSONObject params){
        int height = params.getInt("height");
        return Rpc.getblockbyheight(height,RPCURL);
    }

    public static String listunspent (JSONObject params){
        String assetid = params.getString("assetid");
        JSONArray addresses = params.getJSONArray("addresses");
        return Rpc.listunspent(assetid,addresses,RPCURL);
    }

    public static String getblock (JSONObject params){
        String blockhash = params.getString("blockhash");
        int verbosity = params.getInt("verbosity");
        return Rpc.getblock(blockhash,verbosity,RPCURL);
    }

    public static String getrawtransaction (JSONObject params){
        String txid = params.getString("txid");
        boolean verbose = params.getBoolean("verbose");
        return Rpc.getrawtransaction(txid,verbose,RPCURL);
    }

    public static String sendrawtransaction (JSONObject params){
        String txid = params.getString("data");
        return Rpc.sendrawtransaction(txid,RPCURL);
    }

    public static String getblockhash (JSONObject params){
        int height = params.getInt("height");
        return Rpc.getblockhash(height,RPCURL);
    }

    public static String discretemining (JSONObject params){
        int count = params.getInt("count");
        return Rpc.discretemining(count,RPCURL);
    }

    public static String getreceivedbyaddress (JSONObject params){
        String address = params.getString("address");
        return Rpc.getreceivedbyaddress(address,RPCURL);
    }
}