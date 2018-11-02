package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.ela.Ela;
import org.elastos.ela.RawTx;
import org.elastos.ela.TxOutput;
import org.elastos.ela.utxoTxInput;
import org.elastos.ela.contract.FunctionCode;
import org.elastos.ela.payload.PayloadDeploy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

import static org.elastos.api.Basic.genfunctionCode;

public class NeoContractTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeoContractTransaction.class);

    public static String genDeyplyContractTransaction(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);

            //解析inputs
            utxoTxInput[] utxoTxInput = Basic.parseDeployInputs(json_transaction);
            // outputs
            TxOutput[] output = Basic.parseOutput(json_transaction);
            //functionCode
            FunctionCode functionCode = genfunctionCode(json_transaction);
            //PayloadDeploy
            PayloadDeploy payloadDeploy = Basic.parsePayloadDeploy(json_transaction);

            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = Ela.deployContractTransaction(utxoTxInput,output,functionCode,payloadDeploy);
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genDeyplyContractTransaction" ,resultMap));
            return Basic.getSuccess("genDeyplyContractTransaction" , resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

}
