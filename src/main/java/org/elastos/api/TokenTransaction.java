package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.InterfaceParams;
import org.elastos.ela.*;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.payload.PayloadRegisterAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.elastos.api.Basic.getRawTxAndAssetIdMap;
import static org.elastos.api.Basic.getRawTxMap;
import static org.elastos.common.InterfaceParams.*;

public class TokenTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenTransaction.class);

    public static String genRegisterTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析PayloadRegisterAsset
            PayloadRegisterAsset payload   = Basic.payloadRegisterAsset(json_transaction);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseRegisterOutput(payload,json_transaction);

            //创建rawTransaction
            RawTx rawTx = Ela.makeAndSignTx(UTXOTxInputs,txOutputs,payload);
            LinkedHashMap<String, Object> resultMap = getRawTxAndAssetIdMap(rawTx.getRawTxString(), rawTx.getTxHash(),Asset.AssetId);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genRegisterTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray PrivateKeys = json_transaction.getJSONArray(PRIVATEKEYS);

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析PayloadRegisterAsset
            PayloadRegisterAsset payload   = Basic.payloadRegisterAsset(json_transaction);
            //解析outputs
            LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();

            String changeAddress = json_transaction.getString(CHANGE_ADDRESS);

            //创建rawTransaction
            String rawTx = UsableUtxo.makeAndSignTxByToken(privateList, outputList, changeAddress ,payload);
            LinkedHashMap<String, Object> resultMap = getRawTxAndAssetIdMap(rawTx, UsableUtxo.txHash,Asset.AssetId);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray PrivateKeys = json_transaction.getJSONArray(PRIVATEKEYS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> txOutputs = Basic.parseOutputsByAsset(outputs);

            String changeAddress = json_transaction.getString(CHANGE_ADDRESS);

            //创建rawTransaction

            String rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress);

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx, UsableUtxo.txHash);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputsByAsset(outputs).toArray(new TxOutput[outputs.size()]);

            //创建rawTransaction
            RawTx rawTx  = Ela.makeAndSignTx(UTXOTxInputs,txOutputs);

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenMultiSignTx(JSONObject inputsAddOutpus){

        final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
        final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);

        final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);
        final JSONArray privateKeyScripte = json_transaction.getJSONArray(PRIVATEKEY_SCRIPTE);

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
            final int M = json_transaction.getInt(InterfaceParams.M);

            //得到 签名所需要的私钥
            ArrayList<String> privateKeySignList = Basic.genPrivateKeySignByM(M, privateKeyScripte);

            RawTx rawTx  = Ela.multiSignTransaction(UTXOTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M);
            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }
}
