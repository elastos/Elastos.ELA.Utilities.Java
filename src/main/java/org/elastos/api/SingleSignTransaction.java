package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.*;
import org.elastos.ela.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/21
 */
public class SingleSignTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleSignTransaction.class);
    /**
     * 生成RawTrnsaction
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return  返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransaction(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");

            //解析inputs
            UTXOTxInput[] utxoTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            //解析payloadRecord
            PayloadRecord payload   = Basic.parsePayloadRecord(json_transaction);

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = new RawTx("","");
            if (payload == null){
                rawTx = Ela.makeAndSignTx(utxoTxInputs,txOutputs);
            }else {
                rawTx = Ela.makeAndSignTx(utxoTxInputs,txOutputs,payload);
            }
            resultMap.put("rawTx",rawTx.getRawTxString());
            resultMap.put("txHash",rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genRawTransaction",resultMap));
            return Basic.getSuccess("genRawTransaction",resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }


    /**
     * 根据私钥获取utxo生成RawTrnsaction
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTransactionByPrivateKey(JSONObject inputsAddOutpus){

        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");

            //解析PrivateKeys
            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> outputList = Basic.parseOutputs(outputs);
            //解析payloadRecord,tx=2 没有，tx=3 有payload
            PayloadRecord payload   = Basic.parsePayloadRecord(json_transaction);

            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);

            String changeAddress = json_transaction.getString("ChangeAddress");

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();

            String rawTx = "";
            if (payload == null){
                rawTx = FinishUtxo.makeAndSignTx(privateList, outputList, changeAddress);
            }else {
                rawTx = FinishUtxo.makeAndSignTx(privateList, outputList, changeAddress,payload);
            }
            resultMap.put("rawTx", rawTx);
            resultMap.put("txHash", FinishUtxo.txHash);

            LOGGER.info(Basic.getSuccess("genRawTransactionByPrivateKey" ,resultMap));
            return Basic.getSuccess("genRawTransactionByPrivateKey" ,resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    /**
     * 单签垮链转账
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */

    public static String genCrossChainRawTransaction(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");
            final JSONArray CrossChainAsset = json_transaction.getJSONArray("CrossChainAsset");
            final JSONArray privateKeySign = json_transaction.getJSONArray("PrivateKeySign");

            //解析inputs
            UTXOTxInput[] utxoTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseCrossChainOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            //解析 CrossChain
            PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);
            //解析 签名所需要的私钥
            List<String> privateKeySignList = Basic.parsePrivates(privateKeySign);

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = Ela.CrossChainSignTx(utxoTxInputs, txOutputs,payloadTransferCrossChainAssets, privateKeySignList);
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genCrossChainRawTransaction" ,resultMap));
            return Basic.getSuccess("genCrossChainRawTransaction" , resultMap);
        } catch (Exception e) {
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
