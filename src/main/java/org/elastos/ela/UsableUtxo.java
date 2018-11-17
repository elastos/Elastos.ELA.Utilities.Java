package org.elastos.ela;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.elastos.api.Basic;
import org.elastos.api.Verify;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;
import org.elastos.ela.bitcoinj.Utils;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.payload.PayloadRegisterAsset;
import org.elastos.ela.payload.PayloadTransferCrossChainAsset;
import org.elastos.framework.rpc.Rpc;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
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

    private static String RPCURL ;
    private static long FEE ;
    private static long REGISTERASSETFEE;
    private static int CONFIRMATION ;

    private static String utxoAmount;
    private static List<utxoTxInput> inputList ;

    public static String txHash;

    public static List<String> addrList;


    // ELA
    public static String makeAndSignTx(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates, outputs, changeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx(inputList.toArray(new utxoTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }
    // ELA
    public static String makeAndSignTx(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress,PayloadRecord payloadRecord) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates,outputs,changeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx(inputList.toArray(new utxoTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates,payloadRecord);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }
    // ELA
    public static String makeAndSignTx(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress,String memo) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates,outputs,changeAddress);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTx(inputList.toArray(new utxoTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates,memo);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }

    // crossChain
    public static RawTx makeAndSignTxByCrossChain(List<String> privates , LinkedList<TxOutput> txOutputs , PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets, String changeAddress) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates,txOutputs,changeAddress);
        RawTx rawTx = Ela.crossChainSignTx(inputList.toArray(new utxoTxInput[inputList.size()]),txOutputs.toArray(new TxOutput[txOutputs.size()]),payloadTransferCrossChainAssets, availablePrivates);
        return rawTx;
    }

    //token
    public static String makeAndSignTxByToken(List<String> privates , LinkedList<TxOutput> outputs , String changeAddress,PayloadRegisterAsset payload) throws SDKException {
        List<String> availablePrivates = getChangeAmountAndUsableUtxo(privates, outputs, changeAddress);

        // register asset
        // add payload to output
        Basic.registerToOutput(payload, outputs);
        RawTx rawTx = SignTxAbnormal.makeSingleSignTxByToken(inputList.toArray(new utxoTxInput[inputList.size()]), outputs.toArray(new TxOutput[outputs.size()]), availablePrivates ,payload);
        txHash = rawTx.getTxHash();
        return rawTx.getRawTxString();
    }


    public static List<String> getChangeAmountAndUsableUtxo(List<String> privates , LinkedList<TxOutput> outputs , String ChangeAddress) throws SDKException {
        //add changeAddress
        getChangeAddressAmount(privates,outputs , ChangeAddress);
        // Address to heavy ， The private key corresponding to utxo is used for signature
        ArrayList<String> addrArray = new ArrayList<String>(new HashSet<String>(addrList));
        return availablePrivate(privates, addrArray);
    }

    public static String getUtxoAndConfig(List<String> privates, String assetid) throws SDKException {
        //privatekey to heavy
        ArrayList<String> privateList = new ArrayList<String>(new HashSet<String>(privates));
        //get utxo
        Map<String,Object> params = new HashMap<String,Object>();
        String[] addressList = new String[privateList.size()];
        for (int i = 0 ; i < privateList.size() ; i++){
            String address = Ela.getAddressFromPrivate(privateList.get(i));
            addressList[i] = address;
        }
        params.put("addresses",addressList);
        params.put("assetid",assetid);
        getConfig_url();
        //        System.out.println("==================== 通过地址查询uxto  ====================");
        String utxo = Rpc.call_("listunspent",params,RPCURL);
        return utxo;
    }


    public static void getChangeAddressAmount(List<String> privates, LinkedList<TxOutput> outputs, String changeAddress) throws SDKException {

        if (outputs.size() != 0){
            // Token Transfer
            if (outputs.get(0).getTokenAssetID() != null){
                BigInteger tokenOutputValue = getTokenOutputValue(outputs);
                inputList = new LinkedList<utxoTxInput>();
                addTokenOutputs(privates,outputs,changeAddress,tokenOutputValue);
                // transaction fee
                addElaOutputs(privates,outputs,changeAddress,0);
            }else {
                //ELA Transfer
                long outputValue = getOutputValue(outputs);
                inputList = new LinkedList<utxoTxInput>();
                addElaOutputs(privates,outputs,changeAddress,outputValue);
            }
        }else {
            // register asset
            inputList = new LinkedList<utxoTxInput>();
            addElaOutputs(privates,outputs,changeAddress,0);
        }
    }


    public static void addElaOutputs(List<String> privates,LinkedList<TxOutput> outputs, String changeAddress,  long outputValue) throws SDKException {
        String utxo = getUtxoAndConfig(privates,Common.SYSTEM_ASSET_ID);

        List<UTXOInputSort> UTXOInputList = getUtxo(utxo);

        if (outputs.size() != 0){
            long inputValue = getInputsValue(UTXOInputList, outputValue);

            //changeAddress to output
            long ChangeValue = inputValue - outputValue - FEE;
            String valueStr = Util.divideAmountELA(new BigDecimal(ChangeValue), ElaPrecision).toString();
            outputs.add(new TxOutput(changeAddress, valueStr,Common.SYSTEM_ASSET_ID,ElaPrecision));
        }else {
            // register asset
            long inputValue = registerAssetFee(UTXOInputList);
            long changeAddressValue = inputValue - REGISTERASSETFEE;
            String valueStr = Util.divideAmountELA(new BigDecimal(changeAddressValue), ElaPrecision).toString();
            outputs.add(new TxOutput(changeAddress, valueStr,Common.SYSTEM_ASSET_ID,ElaPrecision));
        }
    }

    public static void addTokenOutputs(List<String> privates,LinkedList<TxOutput> outputs, String changeAddress,  BigInteger tokenOutputValue) throws SDKException {

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

    public static long registerAssetFee(List<UTXOInputSort> UTXOInputList) throws SDKException {
        //usable intput value
        long inputValue = 0;
        addrList = new ArrayList<String>();
        for (int j = 0 ; j < UTXOInputList.size() ; j++){
            UTXOInputSort input = UTXOInputList.get(j);
            String inputTxid = input.getTxid();
            String inputAddress = input.getAddress();
            int inputVont = input.getVont();

            inputValue += Util.multiplyAmountELA(new BigDecimal(input.getAmount()),ElaPrecision).longValue();
            inputList.add(new utxoTxInput(inputTxid,inputVont,"",inputAddress));
            addrList.add(inputAddress);
            if (inputValue >= REGISTERASSETFEE){
                break;
            }
        }

        if (inputValue >= REGISTERASSETFEE){
            return inputValue;
        }else throw new SDKException(ErrorCode.ParamErr("Utxo deficiency , inputValue : " + Util.divideAmountELA(new BigDecimal(inputValue), ElaPrecision).toString() + " , registerAssetFee :" + Util.divideAmountELA(new BigDecimal(REGISTERASSETFEE), ElaPrecision).toString()));
    }

    public static  long getInputsValue(List<UTXOInputSort> UTXOInputList,long outputValue) throws SDKException {
        //usable intput value
        long inputValue = 0;
        addrList = new ArrayList<String>();
        for (int j = 0 ; j < UTXOInputList.size() ; j++){
            UTXOInputSort input = UTXOInputList.get(j);
            String inputTxid = input.getTxid();
            String inputAddress = input.getAddress();
            int inputVont = input.getVont();

            inputValue += Util.multiplyAmountELA(new BigDecimal(input.getAmount()),ElaPrecision).longValue();
            inputList.add(new utxoTxInput(inputTxid,inputVont,"",inputAddress));
            addrList.add(inputAddress);

            if (inputValue >= outputValue + FEE){
                break;
            }
        }

        if (inputValue >= outputValue + FEE){
            return inputValue;
        }else throw new SDKException(ErrorCode.ParamErr("Utxo deficiency , inputValue : " + Util.divideAmountELA(new BigDecimal(inputValue), ElaPrecision).toString() + " , outputValue :" + Util.divideAmountELA(new BigDecimal(outputValue), ElaPrecision).toString() + " , fee :" + Util.divideAmountELA(new BigDecimal(FEE), ElaPrecision).toString()));
    }

    public static  BigInteger getTokenInputsValue(List<UTXOInputSort> UTXOInputList,BigInteger tokenOutputValue) throws SDKException {
        //usable intput token value
        int i = 0;
        BigInteger inputValue = new BigInteger("0");
        addrList = new ArrayList<String>();
        for (int j = 0 ; j < UTXOInputList.size() ; j++){
            UTXOInputSort input = UTXOInputList.get(j);
            String inputTxid = input.getTxid();
            String inputAddress = input.getAddress();
            int inputVont = input.getVont();

            inputValue = Util.multiplyAmountELA(new BigDecimal(input.getAmount()),MaxPrecision).toBigIntegerExact().add(inputValue);
            inputList.add(new utxoTxInput(inputTxid,inputVont,"",inputAddress));
            addrList.add(inputAddress);

            i = inputValue.compareTo(tokenOutputValue);
            if (i >= 0){
                break;
            }
        }

        if (i >= 0){
            return inputValue;
        }else throw new SDKException(ErrorCode.ParamErr("Utxo deficiency , inputValue : " + Util.multiplyAmountELA(new BigDecimal(inputValue), MaxPrecision).toString() + " , tokenOutputValue :" + Util.divideAmountETH(new BigDecimal(tokenOutputValue), MaxPrecision).toString() + " , fee :" + Util.divideAmountELA(new BigDecimal(FEE), ElaPrecision).toString()));


    }

    public static long getOutputValue(LinkedList<TxOutput> outputs){
        long outputValue = 0;
        for (int k = 0 ; k < outputs.size() ; k++){
            TxOutput output = outputs.get(k);
            long value = output.getValue();
            outputValue += value;
        }
        return outputValue;
    }

    public static BigInteger getTokenOutputValue(LinkedList<TxOutput> outputs){
        BigInteger tokenValue = new BigInteger("0");
        for (int k = 0 ; k < outputs.size() ; k++){
            TxOutput output = outputs.get(k);
            BigInteger value = output.getTokenValue();
            tokenValue = tokenValue.add(value);
        }
        return tokenValue;
    }

    public static List<UTXOInputSort> getUtxo(String utxo) throws SDKException {
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
        for (int i=0 ; i < jsonArray.size() ; i++){
            JSONObject result = (JSONObject) jsonArray.get(i);
            String txid = result.getString("txid");
            String address = result.getString("address");
            int vout = result.getInt("vout");

            String blockHash = getBlockHash(txid);

            if (blockHash != null){
                boolean boo = unlockeUtxo(blockHash, txid , vout);
                if (boo){
                    UTXOInputList.add(new UTXOInputSort(txid,address,vout,utxoAmount));
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
    public static Boolean unlockeUtxo(String blockHash , String txid , int vout){
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

                // 步骤 1
                JSONArray voutJson = tx.getJSONArray("vout");
                JSONObject output = (JSONObject) voutJson.get(vout);
                long outputlock = output.getLong("outputlock");
//                System.out.println("outputlock = " + outputlock);

                String value = output.getString("value");
                utxoAmount = value;
                if (outputlock == 0){
                    return true;
                }
                System.out.println("锁仓 txid : " + txid );
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
        int confirmations = result.getInt("confirmations");
        if (confirmations >= CONFIRMATION){
            return result.getString("blockhash");
        }
        return null;
    }

    /**
     * 获取java-config.json配置文件信息
     * @throws SDKException
     */
    public static void getConfig_url() throws SDKException {
        String content;
        try {
            File directory = new File ("");
            String courseFile = directory.getCanonicalPath();
//        File file = new File(courseFile + "/src/main/resources/java-config.json");
            File file = new File(courseFile + "/java-config.json");
            content = FileUtils.readFileToString(file,"UTF-8");
            JSONObject jsonObject = JSONObject.fromObject(content);

            Verify.verifyParameter(Verify.Type.Host,jsonObject);
            Verify.verifyParameter(Verify.Type.Confirmation,jsonObject);
            Verify.verifyParameter(Verify.Type.Fee,jsonObject);

            String host = jsonObject.getString("Host");

            String fee = jsonObject.getString("Fee");
            FEE = Util.multiplyAmountELA(new BigDecimal(fee), ElaPrecision).longValue();

            RPCURL = "http://" + host;
            CONFIRMATION = jsonObject.getInt("Confirmation");
            String registerAssetFee = jsonObject.getString("RegisterAssetFee");
            REGISTERASSETFEE = Util.multiplyAmountELA(new BigDecimal(registerAssetFee), ElaPrecision).longValue();
            if (CONFIRMATION == 0){
                CONFIRMATION = 16;
            }
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("reade java-config.json error : " + e.toString()));
        }
    }

    /**
     * 拿到需要花费utxo的地址对应的私钥
     * @param privateList
     * @param addressList
     * @return
     */
    public static List<String> availablePrivate(List<String> privateList, List<String> addressList){

        if (privateList.size() != addressList.size()){
            List<String> availablePrivate = new ArrayList<String>();

            HashMap privateMap = new HashMap<String,String>();
            for (int i = 0; i < privateList.size();i++){
                privateMap.put(Ela.getAddressFromPrivate(privateList.get(i)),privateList.get(i));
            }
            Iterator keys = privateMap.keySet().iterator();
            while (keys.hasNext()){
                String key =(String)keys.next();
                for (int j = 0; j<addressList.size();j++){
                    if (key.equals(addressList.get(j))){
                        availablePrivate.add((String)privateMap.get(key));
                    }
                }
            }
            return availablePrivate;
        }else return privateList;
    }
}


