package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.InterfaceParams;
import org.elastos.ela.*;
import org.elastos.ela.payload.PayloadRegisterProducer;
import org.elastos.ela.payload.PayloadUpdateProducer;
import org.elastos.ela.payload.ProcessProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.elastos.api.Basic.getRawTxMap;
import static org.elastos.common.InterfaceParams.*;

public class DposTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(DposTransaction.class);

    //注册候选人
    public static String genRegisterProducerTx(JSONObject inputsAddOutpus) {

        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);
            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);

            //payloadRegisterProducer
            PayloadRegisterProducer registerProducer = Basic.payloadRegisterProducer(json_transaction);

            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);

            RawTx rawTx = Ela.registerProducerTransaction(UTXOTxInputs, txOutputs, registerProducer);

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    //更新候选人信息
    public static String genUpdateProducerTx(JSONObject inputsAddOutpus) {
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);

            //updateProducer
            PayloadUpdateProducer updateProducer = Basic.payloadUpdateProducer(json_transaction);

            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);

            RawTx rawTx = Ela.updateProducerTransaction(UTXOTxInputs, txOutputs, updateProducer);

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    //取消候选人交易
    public static String genCancelProducerTx(JSONObject inputsAddOutpus) {
        return processProducerTransaction(inputsAddOutpus, Tx.CANCEL_PRODUCER);
    }

    //Activate候选人交易
    public static String genActivateProducerTx(JSONObject inputsAddOutpus) {
        return processProducerTransaction(inputsAddOutpus, Tx.ACTIVATE_PRODUCER);
    }

    private static String processProducerTransaction(JSONObject inputsAddOutpus, byte txType) {
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);

            //cancelProducer
            ProcessProducer activateProducer = Basic.processProducer(json_transaction);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputs(outputs).toArray(new TxOutput[outputs.size()]);

            RawTx rawTx = Ela.processProducerTransaction(UTXOTxInputs, txOutputs, activateProducer, txType);

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    //投票交易
    public static String genVoteTx(JSONObject inputsAddOutpus) {
        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);

            //解析inputs
            UTXOTxInput[] UTXOTxInputs = Basic.parseInputs(utxoInputs).toArray(new UTXOTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputsVote(outputs).toArray(new TxOutput[outputs.size()]);

            //创建rawTransaction
            RawTx rawTx = Ela.makeAndSignTx(UTXOTxInputs, txOutputs);

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genreturndepositcointx(JSONObject inputsAddOutpus) {

        try {
            final JSONObject json_transaction = inputsAddOutpus.getJSONObject(InterfaceParams.TRANSACTION);

            //解析inputs
            final JSONArray utxoInputs = json_transaction.getJSONArray(INPUTS);
            List<UTXOTxInput> inputList = Basic.parseInputs(utxoInputs);
            //解析outputs
            final JSONArray outputs = json_transaction.getJSONArray(OUTPUTS);
            List<TxOutput> outputList = Basic.parseOutputs(outputs);

            RawTx rawTx = Ela.returnProducerTransaction(inputList.toArray(new UTXOTxInput[utxoInputs.size()]), outputList.toArray(new TxOutput[outputs.size()]));

            LinkedHashMap<String, Object> resultMap = getRawTxMap(rawTx.getRawTxString(), rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess(resultMap));
            return Basic.getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }
}
