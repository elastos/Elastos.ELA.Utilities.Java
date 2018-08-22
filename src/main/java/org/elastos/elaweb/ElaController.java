package org.elastos.elaweb;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.*;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.Error;
import java.util.*;

/**
 * Created by mdj17 on 2018/1/28.
 */
public class ElaController {
    private static String  INVALIDTXID          = "Invalid Txid";
    private static String  INVALIDINDEX         = "Invalid Index";
    private static String  INVALIDPRPVATEKEY    = "Invalid PrivateKey";
    private static String  INVALIDADDRESS       = "Invalid Address";
    private static String  INVALIDCHANGEADDRESS = "Invalid ChangeAddress";
    private static String  INVALIDAMOUNT        = "Invalid Amount";
    public static String  INVALFEE              = "Invalid FEE";
    public static String  INVALCONFIRMATION    = "Invalid CONFIRMATION";

    private static String  TXIDNOTNULL          = "No Txid Field";
    private static String  INDEXNOTNULL         = "No Index Field";
    private static String  PRPVATEKEYNOTNULL    = "No PrivateKey Field";;
    private static String  ADDRESSNOTNULL       = "No Address Field";
    private static String  CHANGEADDRESSNOTNULL = "No ChangeAddress Field";
    private static String  AMOUNTNOTNULL        = "No Amount Field";
    public static String  FEENOTNULL            = "No FEE Field";
    public static String  HOSTNOTNULL           = "No Host Field";
    public static String  CONFIRMATIONNOTNULL  = "No CONFIRMATION Field";



    /**
     * 处理请求
     * @param params
     * @return
     * @throws Exception
     */
    public static String processMethod (String params) throws Exception {

        JSONObject jsonObject = JSONObject.fromObject(params);
        JSONArray jsonArray = jsonObject.getJSONArray("params");
        String method = jsonObject.getString("method");
        if (jsonArray.size() == 0) {
            if (method.equals("genPrivateKey") ) {
                return  genPrivateKey();
            }
            if (method.equals("gen_priv_pub_addr")) {
                return gen_priv_pub_addr();
            }
        }
        if (jsonArray.size() != 0){
            JSONObject param = (JSONObject)jsonArray.get(0);
            if (method.equals("genPublicKey")){
                String state = checkPrivateKey("genPublicKey",param,"PrivateKey");
                if (state != null) return state;
                String privateKey = param.getString("PrivateKey");
                return genPublicKey(privateKey);
            }
            if (method.equals("genAddress")){
                String state = checkPrivateKey("genAddress",param,"PrivateKey");
                if (state != null) return state;
                String privateKey = param.getString("PrivateKey");
                return genAddress(privateKey);
            }
            if (method.equals("genRawTransaction")){
                return genRawTransaction(param);
            }
            if (method.equals("decodeRawTransaction")){
                String rawTransaction = param.getString("RawTransaction");
                return decodeRawTransaction(rawTransaction);
            }
            if (method.equals("genRawTransactionByPrivateKey")) {
                return genRawTransactionByPrivateKey(param);
            }
            if (method.equals("checkAddress")) {
                return checkAddress(param);
            }
        }
        return null ;
    }

    /**
     * 校验地址是否为ela合法地址
     * @param addresses 字典格式或者数组格式的地址
     * @return
     */
    public static String checkAddress(JSONObject addresses){
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        JSONArray addressesJSONArray = addresses.getJSONArray("Addresses");
        Object addressesObject = addressesJSONArray.get(0);
        if (addressesObject instanceof String){
            for (int i = 0 ; i < addressesJSONArray.size() ; i ++){
                boolean boo =  Util.checkAddress((String)addressesJSONArray.get(i));
                resultMap.put((String)addressesJSONArray.get(i),boo);
            }
        }else {
            for (int i = 0 ; i < addressesJSONArray.size() ; i ++){
                JSONObject o = (JSONObject) addressesJSONArray.get(i);
                String address = o.getString("address");
                boolean boo =  Util.checkAddress(address);
                resultMap.put(address,boo);
            }
        }

        return formatJson("checkAddress",resultMap) ;
    }

    /**
     * 生成RawTrnsaction
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return  返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransaction(JSONObject inputsAddOutpus) throws Exception{

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

        //解析inputs
        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0 ; i < utxoInputs.size() ; i++){
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);
            String state = checkInputs("genRawTransaction",utxoInput);
            if (state != null) return state;

            String txid = utxoInput.getString("txid");
            String index = utxoInput.getString("index");
            String privateKey = utxoInput.getString("privateKey");
            String address = Ela.getAddressFromPrivate(privateKey);

            inputList.add(new UTXOTxInput(txid,Integer.parseInt(index),privateKey,address));
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        List<TxOutput>  outputList = new LinkedList<TxOutput>();
        for (int j = 0 ; j < outputs.size() ; j ++){
            JSONObject output = (JSONObject)outputs.get(j);
            String state = checkOutputs("genRawTransaction",output);
            if (state != null) return state;

            String address = output.getString("address");
            long amount = output.getLong("amount");
            outputList.add(new TxOutput(address,amount));
        }

        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        RawTx rawTx = Ela.makeAndSignTx(inputList.toArray(new UTXOTxInput[utxoInputs.size()]),outputList.toArray(new TxOutput[outputs.size()]));
        resultMap.put("rawTx",rawTx.getRawTxString());
        resultMap.put("txHash",rawTx.getTxHash());

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action","genRawTransaction");
        map.put("Desc","SUCCESS");
        map.put("Result",resultMap);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        return jsonParam.toString();
    }

    /**
     * 生成私钥
     * @return 返回json字符串
     */
    public static String genPrivateKey(){
        String privateKey = Ela.getPrivateKey();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action","genPrivateKey");
        map.put("Desc","SUCCESS");
        map.put("Result",privateKey);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);

        return jsonParam.toString();
    }

    /**
     * 生成公钥
     * @param privateKey  私钥
     * @return 返回json字符串
     */
    public static String genPublicKey(String privateKey){
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action","genPublicKey");
        map.put("Desc","SUCCESS");
        map.put("Result",publicKey);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);

        return jsonParam.toString();
    }

    /**
     * 生成地址
     * @param privateKye  私钥
     * @return  返回Json字符串
     */
    public static String genAddress(String privateKye){
        String address    = Ela.getAddressFromPrivate(privateKye);
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action","genAddress");
        map.put("Desc","SUCCESS");
        map.put("Result",address);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);

        return jsonParam.toString();
    }

    /**
     * 生成私钥、公钥、地址
     * @return  返回json字符串
     */
    public static String gen_priv_pub_addr(){
        String privateKey = Ela.getPrivateKey();
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        String address    = Ela.getAddressFromPrivate(privateKey);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("PrivateKey",privateKey);
        resultMap.put("PublicKey",publicKey);
        resultMap.put("Address",address);

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action","genAddress");
        map.put("Desc","SUCCESS");
        map.put("Result",resultMap);



        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);

        return jsonParam.toString();
    }

    /**
     * 发送Rawtransaction
     * @param rawTx
     */
    public static String sendRawTransaction(String rawTx ,String txUrl) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data" , rawTx);

        //构造json格式
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("method", "sendrawtransaction");
        map.put("params", params);

        //发送RowTransaction
        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        System.out.println("url = " + txUrl);
        System.out.println("json = " + jsonParam);
        JSONObject responseJSONObject = HttpRequestUtil.httpPost(txUrl, jsonParam);
        return responseJSONObject.toString();
    }

    /**
     * 反解析rawTransaction得到TXid,address,value
     * @param rawTransaction
     * @return
     * @throws IOException
     */
    public static String decodeRawTransaction(String rawTransaction) throws IOException {

        byte[] rawTxByte = DatatypeConverter.parseHexBinary(rawTransaction);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawTxByte);
        DataInputStream dos = new DataInputStream(byteArrayInputStream);
        Map resultMap = Tx.DeSerialize(dos);

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action","decodeRawTransaction");
        map.put("Desc","SUCCESS");
        map.put("Result",resultMap);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);

        return jsonParam.toString();
    }

    /**
     * 根据私钥获取utxo生成RawTrnsaction
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransactionByPrivateKey(JSONObject inputsAddOutpus) throws Exception {

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");

        //解析inputs
        List<String> privateList = new LinkedList<String>();
        for (int i = 0; i < PrivateKeys.size(); i++) {
            JSONObject utxoInput = (JSONObject) PrivateKeys.get(i);
            String state = checkPrivateKey("genRawTransactionByPrivateKey",utxoInput,"privateKey");
            if (state != null) return state;

            privateList.add(utxoInput.getString("privateKey"));
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);
            String state = checkOutputs("genRawTransactionByPrivateKey",output);
            if (state != null) return state;

            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }

        String state = checkChangeAddress("genRawTransactionByPrivateKey", json_transaction);
        if (state != null) return state;

        String changeAddress = json_transaction.getString("ChangeAddress");

        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String rawTx = FinishUtxo.finishUtxo(privateList, outputList, changeAddress);
        if (FinishUtxo.STATE.equals(false)){
            return rawTx;
        }

        if (rawTx.length() > 80){
            resultMap.put("rawTx", rawTx);
            resultMap.put("txHash", FinishUtxo.txHash);
            return formatJson("genRawTransactionByPrivateKey" ,resultMap);
        }else {
            return error("genRawTransactionByPrivateKey" ,rawTx);
        }
    }

    public static String  formatJson(String action , Object resultMap) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action", action);
        map.put("Desc", "SUCCESS");
        map.put("Result", resultMap);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        return jsonParam.toString();
    }

    public static String  error(String action , String result) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action", action);
        map.put("Desc", "ERROR");
        map.put("Result", result);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        return jsonParam.toString();
    }

    // =================================== check ===========================================

    public static String checkInputs(String action,JSONObject utxoInput){
        Object txid = utxoInput.get("txid");
        if (txid != null) {
            if (!Util.checkPrivateKey((String) txid)) {
                return error(action, INVALIDTXID);
            }
        }else return error(action, TXIDNOTNULL);

        Object index = utxoInput.get("index");
        if (index != null) {
            if (!Util.checkAmount(index)) {
                return error(action, INVALIDINDEX);
            }
        }else return error(action, INDEXNOTNULL);

        return checkPrivateKey(action, utxoInput,"privateKey");
    }

    /**
     * 验证privateKey
     * @param action
     * @param utxoInput
     * @param PrivateKey , 根据需求传递是首字母大写或者小写PrivateKey
     * @return
     */
    public static String checkPrivateKey(String action,JSONObject utxoInput,String PrivateKey){
        Object privateKey = utxoInput.get(PrivateKey);
        if (privateKey != null) {
            if (!Util.checkPrivateKey((String) privateKey)) {
                return error(action, INVALIDPRPVATEKEY);
            }
        }else return error(action, PRPVATEKEYNOTNULL);

        return null;
    }

    public static String checkOutputs(String action,JSONObject outputs){
        Object address = outputs.get("address");
        if (address != null) {
            if (!Util.checkAddress((String) address)) {
                return error(action, INVALIDADDRESS);
            }
        }else return error(action, ADDRESSNOTNULL);

        Object amount = outputs.get("amount");
        if (amount != null) {
            if (!Util.checkAmount(amount)) {
                return error(action, INVALIDAMOUNT);
            }
        }else return error(action, AMOUNTNOTNULL);

        return null;
    }

    public static String checkChangeAddress(String action,JSONObject outputs){
        Object address = outputs.get("ChangeAddress");
        if (address != null) {
            if (!Util.checkAddress((String) address)) {
                return error(action, INVALIDCHANGEADDRESS);
            }
        }else return error(action, CHANGEADDRESSNOTNULL);
        return null;
    }

    public static String checkFeeAndHost(String action,JSONObject jsonObject){
        Object fee = jsonObject.get("Fee");
        if (fee != null) {
            if (!Util.checkAmount(fee)) {
                return error(action, INVALFEE);
            }
        }else return error(action, FEENOTNULL);

        Object confirmation = jsonObject.get("Confirmation");
        if (confirmation != null) {
            if (!Util.checkAmount(confirmation)) {
                return error(action, INVALCONFIRMATION);
            }
        }else return error(action, CONFIRMATIONNOTNULL);

        Object Host = jsonObject.get("Host");
        if (Host == null) return error(action,HOSTNOTNULL );

        return null;
    }
}
