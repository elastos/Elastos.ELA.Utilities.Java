package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public class SignTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignTransaction.class);
    /**
     * 生成RawTrnsaction
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return  返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransaction(JSONObject inputsAddOutpus) throws IOException {

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

        //解析inputs
        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0 ; i < utxoInputs.size() ; i++){
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);

            try {
                Verify.verifyParameter(Verify.Type.TxidLower,utxoInput);
                Verify.verifyParameter(Verify.Type.IndexLower,utxoInput);
                Verify.verifyParameter(Verify.Type.PrivateKeyLower,utxoInput);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInt("index");
            String privateKey = utxoInput.getString("privateKey");
            String address = Ela.getAddressFromPrivate(privateKey);

            inputList.add(new UTXOTxInput(txid,index,privateKey,address));
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        List<TxOutput>  outputList = new LinkedList<TxOutput>();
        for (int j = 0 ; j < outputs.size() ; j ++){
            JSONObject output = (JSONObject)outputs.get(j);

            try {
                Verify.verifyParameter(Verify.Type.AddressLower,output);
                Verify.verifyParameter(Verify.Type.AmountLower,output);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            String address = output.getString("address");
            long amount = output.getLong("amount");
            outputList.add(new TxOutput(address,amount));
        }

        Object payload = json_transaction.get("PayloadRecord");
        String recordType = "";
        String recordData = "";
        if (payload != null){
            final JSONObject PayloadObject = json_transaction.getJSONObject("PayloadRecord");
            try {
                Verify.verifyParameter(Verify.Type.RecordTypeLower,PayloadObject);
                Verify.verifyParameter(Verify.Type.RecordDataLower,PayloadObject);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }
            recordType = PayloadObject.getString("recordType");
            recordData = PayloadObject.getString("recordData");
        }


        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        RawTx rawTx = new RawTx("","");
        if (payload == null){
            rawTx = Ela.makeAndSignTx(inputList.toArray(new UTXOTxInput[utxoInputs.size()]),outputList.toArray(new TxOutput[outputs.size()]));
        }else {
            rawTx = Ela.makeAndSignTx(inputList.toArray(new UTXOTxInput[utxoInputs.size()]),outputList.toArray(new TxOutput[outputs.size()]),new PayloadRecord(recordType,recordData));
        }
        resultMap.put("rawTx",rawTx.getRawTxString());
        resultMap.put("txHash",rawTx.getTxHash());

        LOGGER.info(Basic.getSuccess("genRawTransaction",resultMap));
        return Basic.getSuccess("genRawTransaction",resultMap);
    }


    /**
     * 根据私钥获取utxo生成RawTrnsaction
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransactionByPrivateKey(JSONObject inputsAddOutpus){

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");

        //解析inputs
        List<String> privateList = new LinkedList<String>();
        for (int i = 0; i < PrivateKeys.size(); i++) {
            JSONObject utxoInput = (JSONObject) PrivateKeys.get(i);
            try {
                Verify.verifyParameter(Verify.Type.PrivateKeyLower,utxoInput);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            privateList.add(utxoInput.getString("privateKey"));
        }

        //解析outputs
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);
            try {
                Verify.verifyParameter(Verify.Type.AddressLower,output);
                Verify.verifyParameter(Verify.Type.AmountLower,output);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }

            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }

        try {
            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }

        //解析payload,tx=2 没有，tx=3 有payload
        Object payload = json_transaction.get("PayloadRecord");
        String recordType = "";
        String recordData = "";
        if (payload != null){
            final JSONObject PayloadObject = json_transaction.getJSONObject("PayloadRecord");
            try {
                Verify.verifyParameter(Verify.Type.RecordTypeLower,PayloadObject);
                Verify.verifyParameter(Verify.Type.RecordDataLower,PayloadObject);
            }catch (Exception e){
                LOGGER.error(e.toString());
                return e.toString();
            }
            recordType = PayloadObject.getString("recordType");
            recordData = PayloadObject.getString("recordData");
        }

        String changeAddress = json_transaction.getString("ChangeAddress");

        //创建rawTransaction
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {
            String rawTx = "";
            if (payload == null){
                rawTx = FinishUtxo.makeAndSignTx(privateList, outputList, changeAddress);
            }else {
                rawTx = FinishUtxo.makeAndSignTx(privateList, outputList, changeAddress,new PayloadRecord(recordType,recordData));
            }
            resultMap.put("rawTx", rawTx);
            resultMap.put("txHash", FinishUtxo.txHash);

            LOGGER.info(Basic.getSuccess("genRawTransactionByPrivateKey" ,resultMap));
            return Basic.getSuccess("genRawTransactionByPrivateKey" ,resultMap);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }
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

        LOGGER.info(responseJSONObject.toString());
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

        LOGGER.info(Basic.getSuccess("decodeRawTransaction",resultMap));
        return Basic.getSuccess("decodeRawTransaction",resultMap);
    }
}
