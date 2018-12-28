package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.InterfaceParams;
import org.elastos.common.SDKException;
import org.elastos.ela.*;
import org.elastos.ela.payload.PayloadRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

import static org.elastos.api.Basic.getRawTxMap;
import static org.elastos.common.InterfaceParams.*;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/21
 */
public class ELATransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ELATransaction.class);
    /**
     * genRawTx
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return  返回RawTransaction的json字符串
     * @throws Exception
     */
    public static String genRawTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            //解析payloadRecord
            PayloadRecord payload   = Basic.parsePayloadRecord(json_transaction);

            boolean bool = json_transaction.has(MEMO);

            //创建rawTransaction
            RawTx rawTx ;

            if (payload != null && bool){
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            }else if (payload == null && !bool){
                rawTx = Ela.makeAndSignTx(UTXOTxInputs,txOutputs);
            }else if (bool){
                String memo = json_transaction.getString(MEMO);
                rawTx = Ela.makeAndSignTx(UTXOTxInputs,txOutputs,memo);
            }else{
                rawTx = Ela.makeAndSignTx(UTXOTxInputs,txOutputs,payload);
            }
            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
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
    public static String genRawTxByPrivatekey(JSONObject inputsAddOutpus){

        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray PrivateKeys = json_transaction.getJSONArray(PRIVATEKEYS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析PrivateKeys
            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> outputList = Basic.parseOutputs(outputs);
            //解析payloadRecord,tx=2 没有，tx=3 有payload
            PayloadRecord payload   = Basic.parsePayloadRecord(json_transaction);

            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);

            String changeAddress = json_transaction.getString(CHANGE_ADDRESS);

            String rawTx ;
            boolean bool = json_transaction.has(MEMO);
            if (payload != null && bool){
                return ErrorCode.ParamErr("payloadrecord And Memo can't be used at the same time");
            }else if (payload == null && !bool){
                rawTx = UsableUtxo.makeAndSignTx(privateList, outputList, changeAddress);
            }else if (bool){
                String memo = json_transaction.getString(MEMO);
                rawTx = UsableUtxo.makeAndSignTx(privateList, outputList, changeAddress,memo);
            }else{
                rawTx = UsableUtxo.makeAndSignTx(privateList, outputList, changeAddress,payload);
            }
            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx, UsableUtxo.txHash);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    /**
     * 多签生成RawTrnsaction
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */

    public static String genMultiSignTx(JSONObject inputsAddOutpus){

        final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
        final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);

        if (utxoInputs.size() < 2) {
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);
            final JSONArray privateKeyScripte = json_transaction.getJSONArray(PRIVATEKEY_SCRIPTE);

            try {
                //解析inputs
                UTXOTxInput[] UTXOTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
                //解析outputs
                TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
                //解析payloadRecord
                PayloadRecord payload = Basic.parsePayloadRecord(json_transaction);

                //解析 创建赎回脚本所需要的私钥
                List<String> privateKeyScripteList = Basic.parsePrivates(privateKeyScripte);

                boolean bool = json_transaction.has(MEMO);

                Verify.verifyParameter(Verify.Type.M,json_transaction);
                final int M = json_transaction.getInt(InterfaceParams.M);

                //得到 签名所需要的私钥
                ArrayList<String> privateKeySignList = Basic.genPrivateKeySignByM(M, privateKeyScripte);

                RawTx rawTx;
                if (payload != null && bool){
                    return ErrorCode.ParamErr("payloadrecord And Memo can't be used at the same time");
                }else if (payload == null && !bool){
                    rawTx = Ela.multiSignTransaction(UTXOTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M);
                }else if (bool){
                    String memo = json_transaction.getString(MEMO);
                    rawTx = Ela.multiSignTransaction(UTXOTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M, memo);
                }else{
                    rawTx = Ela.multiSignTransaction(UTXOTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M,payload);
                }
                LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

                return Basic.getSuccess(resultMap);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return e.toString();
            }
        }
        // TODO 处理多签多个inputs逻辑
        return (new SDKException(ErrorCode.ParamErr("multi Sign does not support multi inputs"))).toString();
    }

    /**
     * 反解析rawTransaction得到TXid,address,value
     * @param rawTransaction
     * @return
     * @throws IOException
     */
    public static String decodeRawTx(String rawTransaction) throws IOException {

        byte[] rawTxByte = DatatypeConverter.parseHexBinary(rawTransaction);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawTxByte);
        DataInputStream dos = new DataInputStream(byteArrayInputStream);
        Map resultMap = Tx.deserialize(dos);

        LOGGER.info(Basic.getSuccess(resultMap));
        return Basic.getSuccess(resultMap);
    }
}
