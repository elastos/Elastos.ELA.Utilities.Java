package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.*;
import org.elastos.ela.payload.PayloadTransferCrossChainAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/26
 */
public class CrossChainTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrossChainTransaction.class);

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
            utxoTxInput[] utxoTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new utxoTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseCrossChainOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            //解析 CrossChain
            PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);
            //解析 签名所需要的私钥
            List<String> privateKeySignList = Basic.parsePrivates(privateKeySign);

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = Ela.crossChainSignTx(utxoTxInputs, txOutputs,payloadTransferCrossChainAssets, privateKeySignList);
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genCrossChainRawTransaction" ,resultMap));
            return Basic.getSuccess("genCrossChainRawTransaction" , resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genCrossChainRawTransactionByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");
            final JSONArray CrossChainAsset = json_transaction.getJSONArray("CrossChainAsset");

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> txOutputs = Basic.parseCrossChainOutputs(outputs);
            //解析 CrossChain
            PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);

            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);
            String changeAddress = json_transaction.getString("ChangeAddress");

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = FinishUtxo.makeAndSignTxByCrossChain(privateList, txOutputs,payloadTransferCrossChainAssets,changeAddress);
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genCrossChainRawTransactionByPrivateKey" ,resultMap));
            return Basic.getSuccess("genCrossChainRawTransactionByPrivateKey" , resultMap);
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
                utxoTxInput[] utxoTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new utxoTxInput[utxoInputs.size()]);
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
                RawTx rawTx = Ela.crossChainMultiSignTx(utxoTxInputs,txOutputs,payloadTransferCrossChainAssets, privateKeyScripteList, privateKeySignList, M);
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
