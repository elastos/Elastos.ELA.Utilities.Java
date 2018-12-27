package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.Ela;
import org.elastos.ela.RawTx;
import org.elastos.ela.TxOutput;
import org.elastos.ela.UTXOTxInput;
import org.elastos.ela.payload.PayloadInvoke;
import org.elastos.ela.payload.PayloadDeploy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

import static org.elastos.api.Basic.genfunctionCode;
import static org.elastos.api.Basic.getRawTxMap;
import static org.elastos.common.InterfaceParams.*;

public class NeoContractTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeoContractTransaction.class);

    public static String genDeployContractTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            // outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            //functionCode
            genfunctionCode(json_transaction);
            //PayloadDeploy
            PayloadDeploy payloadDeploy = Basic.parsePayloadDeploy(json_transaction);

            RawTx rawTx = Ela.deployContractTransaction(UTXOTxInputs,txOutputs,payloadDeploy);
            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genInvokeContractTx(JSONObject inputsAddOutpus){
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            // outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);
            // invoke
            PayloadInvoke payloadInvoke = Basic.genPayloadInvoke(json_transaction);

            RawTx rawTx = Ela.invokenContractTransaction(UTXOTxInputs,txOutputs,payloadInvoke);
            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }
}
