package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.*;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.payload.PayloadRegisterAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class TokenTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenTransaction.class);

    public static String genRegisterTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject("transaction");
            final JSONArray utxoInputs = json_transaction.getJSONArray("inputs");

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析PayloadRegisterAsset
            PayloadRegisterAsset payload   = Basic.payloadRegisterAsset(json_transaction);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseRegisterOutput(payload,json_transaction);

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = Ela.makeAndSignTx(UTXOTxInputs,txOutputs,payload);
            resultMap.put("rawTx",rawTx.getRawTxString());
            resultMap.put("txHash",rawTx.getTxHash());
            resultMap.put("assetId",Asset.AssetId);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genRegisterTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject("transaction");
            final JSONArray PrivateKeys = json_transaction.getJSONArray("privateKeys");

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析PayloadRegisterAsset
            PayloadRegisterAsset payload   = Basic.payloadRegisterAsset(json_transaction);
            //解析outputs
            LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();

            String changeAddress = json_transaction.getString("changeAddress");

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            String rawTx = UsableUtxo.makeAndSignTxByToken(privateList, outputList, changeAddress ,payload);
            resultMap.put("rawTx",rawTx);
            resultMap.put("txHash",UsableUtxo.txHash);
            resultMap.put("assetId",Asset.AssetId);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject("transaction");
            final JSONArray PrivateKeys = json_transaction.getJSONArray("privateKeys");
            final JSONArray outputs = json_transaction.getJSONArray("outputs");

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> txOutputs = Basic.parseOutputsByAsset(outputs);

            String changeAddress = json_transaction.getString("changeAddress");

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();

            String rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress);

            resultMap.put("rawTx",rawTx);
            resultMap.put("txHash",UsableUtxo.txHash);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject("transaction");
            final JSONArray utxoInputs = json_transaction.getJSONArray("inputs");
            final JSONArray outputs = json_transaction.getJSONArray("outputs");

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputsByAsset(outputs).toArray(new TxOutput[outputs.size()]);

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx  = Ela.makeAndSignTx(UTXOTxInputs,txOutputs);

            resultMap.put("rawTx",rawTx.getRawTxString());
            resultMap.put("txHash",rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenMultiSignTx(JSONObject inputsAddOutpus){

        final JSONObject json_transaction = inputsAddOutpus.getJSONObject("transaction");
        final JSONArray utxoInputs = json_transaction.getJSONArray("inputs");

        final JSONArray outputs = json_transaction.getJSONArray("outputs");
        final JSONArray privateKeyScripte = json_transaction.getJSONArray("privateKeyScripte");

        try {
            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputsByAsset(outputs).toArray(new TxOutput[outputs.size()]);
            //解析payloadRecord
            PayloadRecord payload = Basic.parsePayloadRecord(json_transaction);

            //解析 创建赎回脚本所需要的私钥
            List<String> privateKeyScripteList = Basic.parsePrivates(privateKeyScripte);


            Verify.verifyParameter(Verify.Type.M,json_transaction);
            final int M = json_transaction.getInt("m");

            //得到 签名所需要的私钥
            ArrayList<String> privateKeySignList = Basic.genPrivateKeySignByM(M, privateKeyScripte);

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx  = Ela.multiSignTransaction(UTXOTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M);
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());


            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }
}
