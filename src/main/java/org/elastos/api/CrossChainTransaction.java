package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.InterfaceParams;
import org.elastos.common.SDKException;
import org.elastos.ela.*;
import org.elastos.ela.payload.PayloadTransferCrossChainAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.elastos.api.Basic.getRawTxMap;

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

    public static String genCrossChainTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(InterfaceParams.INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(InterfaceParams.OUTPUTS);
            final JSONArray CrossChainAsset = json_transaction.getJSONArray(InterfaceParams.CROSS_CHAIN_ASSET);
            final JSONArray privateKeySign = json_transaction.getJSONArray(InterfaceParams.PRIVATEKEY_SIGN);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseCrossChainOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            //解析 CrossChain
            PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);
            //解析 签名所需要的私钥
            List<String> privateKeySignList = Basic.parsePrivates(privateKeySign);

            RawTx rawTx = Ela.crossChainSignTx(UTXOTxInputs, txOutputs,payloadTransferCrossChainAssets, privateKeySignList);
            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genCrossChainTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray PrivateKeys = json_transaction.getJSONArray(InterfaceParams.PRIVATEKEYS);
            final JSONArray outputs = json_transaction.getJSONArray(InterfaceParams.OUTPUTS);
            final JSONArray CrossChainAsset = json_transaction.getJSONArray(InterfaceParams.CROSS_CHAIN_ASSET);

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> txOutputs = Basic.parseCrossChainOutputs(outputs);
            //解析 CrossChain
            PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);

            Verify.verifyParameter(Verify.Type.ChangeAddress,json_transaction);
            String changeAddress = json_transaction.getString(InterfaceParams.CHANGE_ADDRESS);


            RawTx rawTx = UsableUtxo.makeAndSignTxByCrossChain(privateList, txOutputs,payloadTransferCrossChainAssets,changeAddress);

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
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

    public static String genCrossChainMultiSignTx(JSONObject inputsAddOutpus){

        final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
        final JSONArray utxoInputs = json_transaction.getJSONArray(InterfaceParams.INPUTS);

        if (utxoInputs.size() < 2) {
            final JSONArray outputs = json_transaction.getJSONArray(InterfaceParams.OUTPUTS);
            final JSONArray privateKeyScripte = json_transaction.getJSONArray(InterfaceParams.PRIVATEKEY_SCRIPTE);
            final JSONArray CrossChainAsset = json_transaction.getJSONArray(InterfaceParams.CROSS_CHAIN_ASSET);

            try {
                //解析inputs
                UTXOTxInput[] UTXOTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
                //解析outputs
                TxOutput[] txOutputs = Basic.parseCrossChainOutputs(outputs).toArray(new TxOutput[outputs.size()]);
                //解析 CrossChain
                PayloadTransferCrossChainAsset[] payloadTransferCrossChainAssets = Basic.parseCrossChainAsset(CrossChainAsset).toArray(new PayloadTransferCrossChainAsset[CrossChainAsset.size()]);
                //解析 创建赎回脚本所需要的私钥
                List<String> privateKeyScripteList = Basic.parsePrivates(privateKeyScripte);

                final int M = json_transaction.getInt(InterfaceParams.M);

                //得到 签名所需要的私钥
                ArrayList<String> privateKeySignList = Basic.genPrivateKeySignByM(M, privateKeyScripte);

                RawTx rawTx = Ela.crossChainMultiSignTx(UTXOTxInputs,txOutputs,payloadTransferCrossChainAssets, privateKeyScripteList, privateKeySignList, M);
                LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

                LOGGER.info(Basic.getSuccess(resultMap));
                return Basic.getSuccess(resultMap);
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
