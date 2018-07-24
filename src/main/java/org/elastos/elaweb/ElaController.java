package org.elastos.elaweb;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.*;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by mdj17 on 2018/1/28.
 */
public class ElaController {


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
                String privateKey = param.getString("PrivateKey");
                return genPublicKey(privateKey);
            }
            if (method.equals("genAddress")){
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
            privateList.add(utxoInput.getString("privateKey"));
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        LinkedList<TxOutput> outputList = ParsingOutputs(outputs);


        String zeroAddress = json_transaction.getString("ZeroAddress");

        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String rawTx = FinishUtxo.finishUtxo(privateList, outputList, zeroAddress);
        if (rawTx.length() > 80){
            resultMap.put("rawTx", rawTx);
            resultMap.put("txHash", FinishUtxo.txHash);
            return formatJson("genRawTransaction" ,resultMap);
        }else {
            resultMap.put("error",rawTx);
            return formatJson_error("genRawTransaction" ,resultMap);
        }
    }



    public static LinkedList<TxOutput> ParsingOutputs(JSONArray outputs){
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);
            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }
        return outputList;
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

    public static String  formatJson_error(String action , Object resultMap) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Action", action);
        map.put("Desc", 40000);
        map.put("Result", resultMap);

        JSONObject jsonParam = new JSONObject();
        jsonParam.accumulateAll(map);
        return jsonParam.toString();
    }
}
