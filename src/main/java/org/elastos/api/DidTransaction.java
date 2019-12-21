package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.*;
import org.elastos.ela.payload.PayloadRegisterAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static org.elastos.api.Basic.getRawTxAndAssetIdMap;
import static org.elastos.api.Basic.getRawTxMap;
import static org.elastos.common.InterfaceParams.*;

public class DidTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(DidTransaction.class);

    public static String genDidTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            // outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);

            //Payload
            String payload = json_transaction.getString(PAYLOAD);

            RawTx rawTx = Ela.didTransaction(UTXOTxInputs,txOutputs,payload);
            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genDidTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray PrivateKeys = json_transaction.getJSONArray(PRIVATEKEYS);

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
            //Payload
            String payload = json_transaction.getString(PAYLOAD);

            String changeAddress = json_transaction.getString(CHANGE_ADDRESS);

            //创建rawTransaction
            String rawTx = UsableUtxo.makeAndSignTxByDid(privateList, outputList, changeAddress ,payload);
            LinkedHashMap<String, Object> resultMap = getRawTxAndAssetIdMap(rawTx, UsableUtxo.txHash,Asset.AssetId);

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }
}
