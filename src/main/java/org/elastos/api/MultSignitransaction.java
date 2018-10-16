package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/26
 */
public class MultSignitransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultSignitransaction.class);

    /**
     * 多签生成RawTrnsaction
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */

    public static String genMultiSignRawTransaction(JSONObject inputsAddOutpus){

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

        if (utxoInputs.size() < 2) {
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");
            final JSONArray privateKeyScripte = json_transaction.getJSONArray("PrivateKeyScripte");

            try {
                //解析inputs
                UTXOTxInput[] utxoTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
                //解析outputs
                TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
                //解析payloadRecord
                PayloadRecord payload = Basic.parsePayloadRecord(json_transaction);

                //解析 创建赎回脚本所需要的私钥
                List<String> privateKeyScripteList = Basic.parsePrivates(privateKeyScripte);

                boolean bool = json_transaction.has("Memo");

                Verify.verifyParameter(Verify.Type.MUpper,json_transaction);
                final int M = json_transaction.getInt("M");

                //得到 签名所需要的私钥
                ArrayList<String> privateKeySignList = Basic.genPrivateKeySignByM(M, privateKeyScripte);

                LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
                RawTx rawTx;
                if (payload != null && bool){
                    return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
                }else if (payload == null && !bool){
                    rawTx = Ela.MultiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M);
                }else if (bool){
                    String memo = json_transaction.getString("Memo");
                    rawTx = Ela.MultiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M, memo);
                }else{
                    rawTx = Ela.MultiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M,payload);
                }
                resultMap.put("rawTx", rawTx.getRawTxString());
                resultMap.put("txHash", rawTx.getTxHash());


                return Basic.getSuccess("genMultiSignRawTransaction", resultMap);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return e.toString();
            }
        }
        // TODO 处理多签多个inputs逻辑
        return (new SDKException(ErrorCode.ParamErr("multi Sign does not support multi inputs"))).toString();
    }


    public static String genMultiSignRawTransactionByToken(JSONObject inputsAddOutpus){

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        final JSONArray privateKeyScripte = json_transaction.getJSONArray("PrivateKeyScripte");

        try {
            //解析inputs
            UTXOTxInput[] utxoTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputsByAsset(outputs).toArray(new TxOutput[outputs.size()]);
            //解析payloadRecord
            PayloadRecord payload = Basic.parsePayloadRecord(json_transaction);

            //解析 创建赎回脚本所需要的私钥
            List<String> privateKeyScripteList = Basic.parsePrivates(privateKeyScripte);

            boolean bool = json_transaction.has("Memo");

            Verify.verifyParameter(Verify.Type.MUpper,json_transaction);
            final int M = json_transaction.getInt("M");

            //得到 签名所需要的私钥
            ArrayList<String> privateKeySignList = Basic.genPrivateKeySignByM(M, privateKeyScripte);

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx;
            if (payload != null && bool){
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            }else if (payload == null && !bool){
                rawTx = Ela.MultiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M);
            }else if (bool){
                String memo = json_transaction.getString("Memo");
                rawTx = Ela.MultiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M, memo);
            }else{
                rawTx = Ela.MultiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M,payload);
            }
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());


            return Basic.getSuccess("genMultiSignRawTransactionByToken", resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    /**
     * 跨链多签生成RawTrnsaction
     *
     * @param inputsAddOutpus 交易输入和交易输出的json字符串
     * @return 返回RawTransaction的json字符串
     * @throws Exception
     */

    public static String genCrossChainMultiSignRawTransaction(JSONObject inputsAddOutpus){

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

        if (utxoInputs.size() < 2) {
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");
            final JSONArray privateKeyScripte = json_transaction.getJSONArray("PrivateKeyScripte");
            final JSONArray CrossChainAsset = json_transaction.getJSONArray("CrossChainAsset");

            try {
                //解析inputs
                UTXOTxInput[] utxoTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
                //解析outputs
                TxOutput[] txOutputs = Basic.parseCrossChainOutputs(outputs).toArray(new TxOutput[outputs.size()]);
                //解析 CrossChain
                PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);
                //解析 创建赎回脚本所需要的私钥
                List<String> privateKeyScripteList = Basic.parsePrivates(privateKeyScripte);

                final int M = json_transaction.getInt("M");

                //得到 签名所需要的私钥
                ArrayList<String> privateKeySignList = Basic.genPrivateKeySignByM(M, privateKeyScripte);

                LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
                RawTx rawTx = Ela.CrossChainMultiSignTx(utxoTxInputs,txOutputs,payloadTransferCrossChainAssets, privateKeyScripteList, privateKeySignList, M);
                resultMap.put("rawTx", rawTx.getRawTxString());
                resultMap.put("txHash", rawTx.getTxHash());

                LOGGER.info(Basic.getSuccess("genCrossChainRawTransaction" ,resultMap));
                return Basic.getSuccess("genCrossChainRawTransaction" , resultMap);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                return e.toString();
            }
        } else {
            // TODO 处理多签多个inputs逻辑
            return (new SDKException(ErrorCode.ParamErr("multi Sign does not support multi inputs"))).toString();
        }
    }
}
