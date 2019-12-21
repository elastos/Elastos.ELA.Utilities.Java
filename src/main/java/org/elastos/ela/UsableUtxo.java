package org.elastos.ela;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.api.Basic;
import org.elastos.common.*;
import org.elastos.ela.bitcoinj.Utils;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.payload.PayloadRegisterAsset;
import org.elastos.ela.payload.PayloadTransferCrossChainAsset;
import org.elastos.framework.rpc.Rpc;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.elastos.ela.payload.PayloadRegisterAsset.ElaPrecision;
import static org.elastos.ela.payload.PayloadRegisterAsset.MaxPrecision;


/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/6/20
 */
public class UsableUtxo {

    private static String utxoAmount;
    private static List<UTXOTxInput> inputList ;

    public static String txHash;

    private static List<String> addrList;


    // ELA
    public static String makeAndSignTx(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates, outputs, changeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx(inputList.toArray(new UTXOTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }
    // ELA
    public static String makeAndSignTx(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress,PayloadRecord payloadRecord) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates,outputs,changeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx(inputList.toArray(new UTXOTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates,payloadRecord);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }
    // ELA
    public static String makeAndSignTx(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress,String memo) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates,outputs,changeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx(inputList.toArray(new UTXOTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates,memo);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }

    // crossChain
    public static RawTx makeAndSignTxByCrossChain(List<String> privates , LinkedList<TxOutput> txOutputs , PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets, String changeAddress) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates,txOutputs,changeAddress);
        return Ela.crossChainSignTx(inputList.toArray(new UTXOTxInput[inputList.size()]),txOutputs.toArray(new TxOutput[txOutputs.size()]),payloadTransferCrossChainAssets, availablePrivates);
    }

    //token
    public static String makeAndSignTxByToken(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress,PayloadRegisterAsset payload) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates, outputs, changeAddress);

        // register asset
        // add payload to output
        Basic.registerToOutput(payload, outputs);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTxByToken(inputList.toArray(new UTXOTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates ,payload);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }

    public static String makeAndSignTxByDid(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress,String payload) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates, outputs, changeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTxByDid(inputList.toArray(new UTXOTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates, payload);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }

    private static List<String> getChangeAmountAndUsableUtxo(List<String> privates, LinkedList<TxOutput> outputs, String ChangeAddress) throws SDKException {
        //add changeAddress
        getChangeAddressAmount(privates,outputs , ChangeAddress);
        // Address to heavy ， The private key corresponding to utxo is used for signature
        ArrayList<String> addrArray = new ArrayList<String>(new HashSet<String>(addrList));
        return availablePrivate(privates, addrArray);
    }

    private static String getUtxoAndConfig(List<String> privates, String assetid) throws SDKException {
        //privatekey to heavy
        ArrayList<String> privateList = new ArrayList<String>(new HashSet<String>(privates));
        //get utxo
//        Map<String,Object> params = new HashMap<String,Object>();
        String[] addressList = new String[privateList.size()];
        for (int i = 0 ; i < privateList.size() ; i++){
            String address = Ela.getAddressFromPrivate(privateList.get(i));
            addressList[i] = address;
        }

        //        System.out.println("==================== 通过地址查询uxto  ====================");

        String utxo = Rpc.listunspent(assetid,JSONArray.fromObject(addressList) , Config.getRpcUrl());
        return utxo;
    }


    private static void getChangeAddressAmount(List<String> privates, LinkedList<TxOutput> outputs, String changeAddress) throws SDKException {

        if (outputs.size() != 0){
            // Token Transfer
            if (outputs.get(0).getTokenAssetID() != null){
                BigInteger tokenOutputValue = getTokenOutputValue(outputs);
                inputList = new LinkedList<UTXOTxInput>();
                addTokenOutputs(privates,outputs,changeAddress,tokenOutputValue);
                // transaction fee
                addElaOutputs(privates,outputs,changeAddress,0);
            }else {
                //ELA Transfer
                long outputValue = getOutputValue(outputs);
                inputList = new LinkedList<UTXOTxInput>();
                addElaOutputs(privates,outputs,changeAddress,outputValue);
            }
        }else {
            // register asset
            inputList = new LinkedList<UTXOTxInput>();
            addElaOutputs(privates,outputs,changeAddress,0);
        }
    }


    private static void addElaOutputs(List<String> privates, LinkedList<TxOutput> outputs, String changeAddress, long outputValue) throws SDKException {
        String utxo = getUtxoAndConfig(privates,Common.SYSTEM_ASSET_ID);

        List<UTXOInputSort> UTXOInputList = getUtxo(utxo);

        if (outputs.size() != 0){
            long inputValue = getInputsValue(UTXOInputList, outputValue);

            //changeAddress to output
            long ChangeValue = inputValue - outputValue - Config.getFee();
            String valueStr = Util.divideAmountELA(new BigDecimal(ChangeValue), ElaPrecision).toString();
            outputs.add(new TxOutput(changeAddress, valueStr,Common.SYSTEM_ASSET_ID,ElaPrecision));
        }else {
            // register asset
            long inputValue = registerAssetFee(UTXOInputList);
            long changeAddressValue = inputValue - Config.getRegisterAssetFee();
            String valueStr = Util.divideAmountELA(new BigDecimal(changeAddressValue), ElaPrecision).toString();
            outputs.add(new TxOutput(changeAddress, valueStr,Common.SYSTEM_ASSET_ID,ElaPrecision));
        }
    }

    private static void addTokenOutputs(List<String> privates, LinkedList<TxOutput> outputs, String changeAddress, BigInteger tokenOutputValue) throws SDKException {

        byte[] tokenAssetID = outputs.get(0).getTokenAssetID();
        String tokenAssetIDStr = DatatypeConverter.printHexBinary(Utils.reverseBytes(tokenAssetID)).toLowerCase();

        String utxo = getUtxoAndConfig(privates, tokenAssetIDStr);

        List<UTXOInputSort> UTXOInputList = getUtxo(utxo);

        BigInteger tokenInputsValue = getTokenInputsValue(UTXOInputList, tokenOutputValue);

        int i = tokenInputsValue.compareTo(tokenOutputValue);
        if (i > 0){
            BigInteger subtractValue = tokenInputsValue.subtract(tokenOutputValue);
            String valueStr = Util.divideAmountELA(new BigDecimal(subtractValue), MaxPrecision).toString();
            outputs.add(new TxOutput(changeAddress,valueStr,tokenAssetIDStr,MaxPrecision));
        }
    }

    private static long registerAssetFee(List<UTXOInputSort> UTXOInputList) throws SDKException {
        //usable intput value
        long inputValue = 0;
        addrList = new ArrayList<String>();
        for (UTXOInputSort input : UTXOInputList) {
            String inputTxid = input.getTxid();
            String inputAddress = input.getAddress();
            int inputVont = input.getVont();

            inputValue += Util.multiplyAmountELA(new BigDecimal(input.getAmount()), ElaPrecision).longValue();
            inputList.add(new UTXOTxInput(inputTxid, inputVont, "", inputAddress));
            addrList.add(inputAddress);
            if (inputValue >= Config.getRegisterAssetFee()) {
                break;
            }
        }

        if (inputValue >= Config.getRegisterAssetFee()){
            return inputValue;
        }else throw new SDKException(ErrorCode.ParamErr("Utxo deficiency , inputValue : " + Util.divideAmountELA(new BigDecimal(inputValue), ElaPrecision).toString() + " , registerAssetFee :" + Util.divideAmountELA(new BigDecimal(Config.getRegisterAssetFee()), ElaPrecision).toString()));
    }

    private static  long getInputsValue(List<UTXOInputSort> UTXOInputList, long outputValue) throws SDKException {
        //usable intput value
        long inputValue = 0;
        addrList = new ArrayList<String>();
        for (UTXOInputSort input : UTXOInputList) {
            String inputTxid = input.getTxid();
            String inputAddress = input.getAddress();
            int inputVont = input.getVont();

            inputValue += Util.multiplyAmountELA(new BigDecimal(input.getAmount()), ElaPrecision).longValue();
            inputList.add(new UTXOTxInput(inputTxid, inputVont, "", inputAddress));
            addrList.add(inputAddress);

            if (inputValue >= outputValue + Config.getFee()) {
                break;
            }
        }

        if (inputValue >= outputValue + Config.getFee()){
            return inputValue;
        }else throw new SDKException(ErrorCode.ParamErr("Utxo deficiency , inputValue : " + Util.divideAmountELA(new BigDecimal(inputValue), ElaPrecision).toString() + " , outputValue :" + Util.divideAmountELA(new BigDecimal(outputValue), ElaPrecision).toString() + " , fee :" + Util.divideAmountELA(new BigDecimal(Config.getFee()), ElaPrecision).toString()));
    }

    private static  BigInteger getTokenInputsValue(List<UTXOInputSort> UTXOInputList, BigInteger tokenOutputValue) throws SDKException {
        //usable intput token value
        int i = 0;
        BigInteger inputValue = new BigInteger("0");
        addrList = new ArrayList<String>();
        for (UTXOInputSort input : UTXOInputList) {
            String inputTxid = input.getTxid();
            String inputAddress = input.getAddress();
            int inputVont = input.getVont();

            inputValue = Util.multiplyAmountELA(new BigDecimal(input.getAmount()), MaxPrecision).toBigIntegerExact().add(inputValue);
            inputList.add(new UTXOTxInput(inputTxid, inputVont, "", inputAddress));
            addrList.add(inputAddress);

            i = inputValue.compareTo(tokenOutputValue);
            if (i >= 0) {
                break;
            }
        }

        if (i >= 0){
            return inputValue;
        }else throw new SDKException(ErrorCode.ParamErr("Utxo deficiency , inputValue : " + Util.multiplyAmountELA(new BigDecimal(inputValue), MaxPrecision).toString() + " , tokenOutputValue :" + Util.divideAmountETH(new BigDecimal(tokenOutputValue), MaxPrecision).toString() + " , fee :" + Util.divideAmountELA(new BigDecimal(Config.getFee()), ElaPrecision).toString()));


    }

    private static long getOutputValue(LinkedList<TxOutput> outputs){
        long outputValue = 0;
        for (TxOutput output : outputs) {
            long value = output.getValue();
            outputValue += value;
        }
        return outputValue;
    }

    private static BigInteger getTokenOutputValue(LinkedList<TxOutput> outputs){
        BigInteger tokenValue = new BigInteger("0");
        for (TxOutput output : outputs) {
            BigInteger value = output.getTokenValue();
            tokenValue = tokenValue.add(value);
        }
        return tokenValue;
    }

    private static List<UTXOInputSort> getUtxo(String utxo) throws SDKException {
        JSONObject jsonObject = JSONObject.fromObject(utxo);
        String error = jsonObject.getString("error");
        if (error != "null"){
            JSONObject jsonError = JSONObject.fromObject(error);
            String message = jsonError.getString("message");
//            System.out.println("获取utxo失败 ：" + message);
            throw new SDKException(ErrorCode.ParamErr("Getting utxo failure , " +message));
//            return "Getting utxo failure , " +message;
        }

        Object resultObject = jsonObject.get("result");
        if (resultObject == null) {
//            return "The address is not utxo , Please check the address";
            throw new SDKException(ErrorCode.ParamErr("The address is not utxo , Please check the address"));
        }

        JSONArray jsonArray = jsonObject.getJSONArray("result");
        List<UTXOInputSort> UTXOInputList = new ArrayList<UTXOInputSort>();
        for (Object o : jsonArray) {
            JSONObject result = (JSONObject) o;
            String txid = result.getString("txid");
            String address = result.getString("address");
            int vout = result.getInt("vout");

            String blockHash = getBlockHash(txid);

            if (blockHash != null) {
                boolean boo = unlockeUtxo(blockHash, txid, vout);
                if (boo) {
                    UTXOInputList.add(new UTXOInputSort(txid, address, vout, utxoAmount));
                }
            }
        }
        Collections.sort(UTXOInputList);
        return UTXOInputList;
    }



    /**
     * 解析区块信息判断可用utxo
     * UTXO锁验证步骤：
     * 1.判断所有的input 是否引用包含 UTXO 锁 （OutputLock > 0）的 UTXO，如果没有引用，则返回 true;
     * 2.判断引用了 UTXO 锁的 Input 的 Sequence 是否等于 0xfffffffe,如果不相等，返回 false；
     * 3.判断交易的 TimeLock 是否大于所有 UTXO 的 OutputLock 的值，如果不大于，返回 false;
     * 4.验证通过，返回 ture.
     * @param txid
     * @param vout
     * @return
     */
    private static Boolean unlockeUtxo(String blockHash, String txid, int vout){

//        System.out.println("==================== 通过区块Hash查询区块信息  ====================");
        String block = Rpc.getblock(blockHash, 2, Config.getRpcUrl());

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

        for (Object o : txArray) {
            JSONObject tx = (JSONObject) o;
            String txHash = tx.getString("txid");
            if (txHash.equals(txid)) {
                long locktime = tx.getLong("locktime");
//                System.out.println("locktime = " + locktime);

                // 步骤 1
                JSONArray voutJson = tx.getJSONArray("vout");
                JSONObject output = (JSONObject) voutJson.get(vout);
                long outputlock = output.getLong("outputlock");
//                System.out.println("outputlock = " + outputlock);

                String value = output.getString("value");
                utxoAmount = value;
                if (outputlock == 0) {
                    return true;
                }
                System.out.println("锁仓 txid : " + txid);
//                JSONArray vinJson = tx.getJSONArray("vin");
//
                // 步骤 2
//                for(int j = 0 ; j < vinJson.size() ; i++){
//                    JSONObject vin =(JSONObject) vinJson.get(j);
//                    long sequence = vin.getLong("sequence");
//                    System.out.println("sequence = " + sequence);
//                    if (sequence == 0xfffffffe || sequence == 0){
//                        return true;
//                    }
//                }
                // 步骤 3
//                System.out.println("uxto locked , txid :" + txid);
//                if (locktime > outputlock){
//                    return true;
//                }
            }
        }
        return false;
    }

    /**
     * 通过交易ID获取区块Hash
     * @param txid
     * @return
     */
    private static String getBlockHash(String txid){

        //        System.out.println("==================== 通过txid查询区块Hash  ====================");
        String Transcation =Rpc.getrawtransaction(txid,true,Config.getRpcUrl());
        JSONObject jsonObject = JSONObject.fromObject(Transcation);
        String error = jsonObject.getString("error");
        if (!error.equals("null")){
            JSONObject jsonError = JSONObject.fromObject(error);
            String message = jsonError.getString("message");
            System.out.println("获取区块信息失败 ：" + message);
            return "";
        }
//        JSONArray jsonArray = jsonObject.getJSONArray("result");
        JSONObject result = jsonObject.getJSONObject("result");
        int confirmations = result.getInt("confirmations");
        if (confirmations >= Config.getConfirmation()){
            return result.getString("blockhash");
        }
        return null;
    }


    /**
     * 拿到需要花费utxo的地址对应的私钥
     * @param privateList
     * @param addressList
     * @return
     */
    private static List<String> availablePrivate(List<String> privateList, List<String> addressList){

        if (privateList.size() != addressList.size()){
            List<String> availablePrivate = new ArrayList<String>();

            HashMap<String,String> privateMap = new HashMap<String,String>();
            for (String s : privateList) {
                privateMap.put(Ela.getAddressFromPrivate(s), s);
            }
            for (Object o : privateMap.keySet()) {
                String key = (String) o;
                for (String s : addressList) {
                    if (key.equals(s)) {
                        availablePrivate.add((String) privateMap.get(key));
                    }
                }
            }
            return availablePrivate;
        }else return privateList;
    }
}


