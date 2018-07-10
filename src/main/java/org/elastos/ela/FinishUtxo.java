package org.elastos.ela;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.elastos.framework.rpc.Rpc;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FinishUtxo {

    private static String RPCURL ;
    private static int FEE ;

    private static long utxoAmount;
    private static List<UTXOTxInput> inputList;

    public static String txHash;

    /**
     * 整合utxo
     * @param privates
     * @param outputs
     * @param zeroAddress
     * @return
     */
    public static String finishUtxo(List<String> privates , LinkedList<TxOutput> outputs , String zeroAddress) throws IOException {

        //去重
        ArrayList<String> privateList = new ArrayList<String>(new HashSet<String>(privates));

        //获取utxo
        Map<String,String[]> params = new HashMap<String,String[]>();
        String[] addressList = new String[privateList.size()];
        for (int i = 0 ; i < privateList.size() ; i++){
            String address = Ela.getAddressFromPrivate(privateList.get(i));
            addressList[i] = address;
        }
        params.put("addresses",addressList);
        getConfig_url();

//        System.out.println("==================== 通过地址查询uxto  ====================");
        String utxo = Rpc.call_("listunspent",params,RPCURL);
        String flag = getUtxo(utxo , outputs , zeroAddress);
        if (flag.equals("ok")){
            RawTx rawTx = SignTxAbnormal.singleSignTx(inputList.toArray(new UTXOTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), privates);
            txHash = rawTx.getTxHash();
            return rawTx.getRawTxString();
        }else {
            return flag;
        }
    }

    public static String getUtxo(String utxo , LinkedList<TxOutput> outputs , String zeroAddress){

        String flag = "ok";

        JSONObject jsonObject = JSONObject.fromObject(utxo);
        String error = jsonObject.getString("error");
        if (error != "null"){
            JSONObject jsonError = JSONObject.fromObject(error);
            String message = jsonError.getString("message");
            System.out.println("获取utxo失败 ：" + message);
            return flag = "Getting utxo failure , " +message;
        }

        JSONArray jsonArray = jsonObject.getJSONArray("result");
        List<UTXOInputSort> UTXOInputList = new ArrayList<UTXOInputSort>();
        for (int i=0 ; i < jsonArray.size() ; i++){
            JSONObject result = (JSONObject) jsonArray.get(i);
            String txid = result.getString("txid");
            String address = result.getString("address");
            int vout = result.getInt("vout");

            String blockHash = getBlockHash(txid);

            boolean boo = availableUtxo(blockHash, txid , vout);
            if (boo){
                UTXOInputList.add(new UTXOInputSort(txid,address,vout,utxoAmount));
            }
        }

        Collections.sort(UTXOInputList);

        //outputs.amount > 可用utxo.amout
        inputList = new LinkedList<UTXOTxInput>();

        long outputValue = 0;
        for (int k = 0 ; k < outputs.size() ; k++){
            TxOutput output = outputs.get(k);
            long value = output.getValue();
            outputValue += value;
        }

        long inputValue = 0;
        for (int j = 0 ; j < UTXOInputList.size() ; j++){
            UTXOInputSort input = UTXOInputList.get(j);
            String inputTxid = input.getTxid();
            String inputAddress = input.getAddress();
            int inputVont = input.getVont();

            inputValue += input.getAmount();
            inputList.add(new UTXOTxInput(inputTxid,inputVont,"",inputAddress));
            if (inputValue > outputValue + FEE){
                break;
            }
        }

        if (inputValue > outputValue + FEE){

            long zeroValue = inputValue - outputValue - FEE;
            outputs.add(new TxOutput(zeroAddress, zeroValue));
            return "ok";
        }else {
            System.out.println("utxo不足，value = " +inputValue);
            flag = "Utxo deficiency , inputValue : " + inputValue + " , outputValue :" + outputValue;
        }
        return flag;
    }

    /**
     * 解析区块信息判断可用utxo
     * @param txid
     * @param vout
     * @return
     */
    public static Boolean availableUtxo(String blockHash , String txid , int vout){
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        paramsMap.put("blockhash",blockHash);
        paramsMap.put("verbosity",2);

//        System.out.println("==================== 通过区块Hash查询区块信息  ====================");
        String block = Rpc.call_("getblock" ,paramsMap, RPCURL);

        JSONObject jsonObject = JSONObject.fromObject(block);
        String error = jsonObject.getString("error");
        if (error != "null"){
            JSONObject jsonError = JSONObject.fromObject(error);
            String message = jsonError.getString("message");
            System.out.println("获取区块信息失败 ：" + message);
            return false;
        }
        JSONObject results = jsonObject.getJSONObject("result");
        JSONArray  txArray = results.getJSONArray("tx");

        for (int i =0 ; i < txArray.size() ; i++){
            JSONObject tx =(JSONObject) txArray.get(i);
            String txHash = tx.getString("txid");
            if (txHash.equals(txid)){
                long locktime = tx.getLong("locktime");
//                System.out.println("locktime = " + locktime);


                JSONArray voutJson = tx.getJSONArray("vout");
                JSONObject output = (JSONObject) voutJson.get(vout);
                long outputlock = output.getLong("outputlock");
//                System.out.println("outputlock = " + outputlock);

                String value = output.getString("value");
                long valueLong = Util.IntByString(value);
                utxoAmount = valueLong;
                if (outputlock == 0){
                    return true;
                }
                System.out.println("锁仓 txid : " + txid );
//                JSONArray vinJson = tx.getJSONArray("vin");
//
//                for(int j = 0 ; j < vinJson.size() ; i++){
//                    JSONObject vin =(JSONObject) vinJson.get(j);
//                    long sequence = vin.getLong("sequence");
//                    System.out.println("sequence = " + sequence);
//                    if (sequence == 0xfffffffe || sequence == 0){
//                        return true;
//                    }
//                }
//                System.out.println("uxto locked , txid :" + txid);
//                if (locktime > outputlock){
//                    return true;
//                }
            }
        }
        return false;
    }


    public static String getBlockHash(String txid){
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<String, Object>();
        paramsMap.put("txid",txid);
        paramsMap.put("verbose",true);

//        System.out.println("==================== 通过txid查询区块Hash  ====================");
        String Transcation = Rpc.call_("getrawtransaction",paramsMap , RPCURL);
        JSONObject jsonObject = JSONObject.fromObject(Transcation);
        String error = jsonObject.getString("error");
        if (error != "null"){
            JSONObject jsonError = JSONObject.fromObject(error);
            String message = jsonError.getString("message");
            System.out.println("获取区块信息失败 ：" + message);
            return "";
        }
//        JSONArray jsonArray = jsonObject.getJSONArray("result");
        JSONObject result = jsonObject.getJSONObject("result");
        return result.getString("blockhash");
    }

    public static void getConfig_url()throws IOException{
        File directory = new File ("");
        String courseFile = directory.getCanonicalPath();
//        File file = new File(courseFile + "/src/main/resources/java-config.json");
        File file = new File(courseFile + "/java-config.json");
        String content = FileUtils.readFileToString(file,"UTF-8");
        JSONObject jsonObject = JSONObject.fromObject(content);

        String host = jsonObject.getString("Host");
        FEE = jsonObject.getInt("Fee");
        RPCURL = "http://" + host;
    }
}


