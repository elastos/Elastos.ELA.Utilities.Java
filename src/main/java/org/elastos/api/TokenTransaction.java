package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
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
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

            //解析inputs
            utxoTxInput[] utxoTxInputs = Basic.parseInputs(utxoInputs).toArray(new utxoTxInput[utxoInputs.size()]);
            //解析PayloadRegisterAsset
            PayloadRegisterAsset payload   = Basic.payloadRegisterAsset(json_transaction);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseRegisterOutput(payload,json_transaction);

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = Ela.makeAndSignTx(utxoTxInputs,txOutputs,payload);
            resultMap.put("rawTx",rawTx.getRawTxString());
            resultMap.put("txHash",rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genRegisterTx",resultMap));
            return Basic.getSuccess("genRegisterTx",resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genRegisterTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析PayloadRegisterAsset
            PayloadRegisterAsset payload   = Basic.payloadRegisterAsset(json_transaction);
            //解析outputs
            LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();

            String changeAddress = json_transaction.getString("ChangeAddress");

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            String rawTx = UsableUtxo.makeAndSignTxByToken(privateList, outputList, changeAddress ,payload);
            resultMap.put("rawTx",rawTx);
            resultMap.put("txHash",UsableUtxo.txHash);

            LOGGER.info(Basic.getSuccess("genRegisterTxByPrivateKey",resultMap));
            return Basic.getSuccess("genRegisterTxByPrivateKey",resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenTxByPrivateKey(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray PrivateKeys = json_transaction.getJSONArray("PrivateKeys");
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");

            List<String> privateList = Basic.parsePrivates(PrivateKeys);
            //解析outputs
            LinkedList<TxOutput> txOutputs = Basic.parseOutputsByAsset(outputs);

            String changeAddress = json_transaction.getString("ChangeAddress");

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();

            String rawTx = UsableUtxo.makeAndSignTx(privateList, txOutputs, changeAddress);

            resultMap.put("rawTx",rawTx);
            resultMap.put("txHash",UsableUtxo.txHash);

            LOGGER.info(Basic.getSuccess("genTokenTxByPrivateKey",resultMap));
            return Basic.getSuccess("genTokenTxByPrivateKey",resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenTx(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");

            //解析inputs
            utxoTxInput[] utxoTxInputs = Basic.parseInputs(utxoInputs).toArray(new utxoTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputsByAsset(outputs).toArray(new TxOutput[outputs.size()]);

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx  = Ela.makeAndSignTx(utxoTxInputs,txOutputs);

            resultMap.put("rawTx",rawTx.getRawTxString());
            resultMap.put("txHash",rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genTokenTx",resultMap));
            return Basic.getSuccess("genTokenTx",resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genTokenMultiSignTx(JSONObject inputsAddOutpus){

        final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
        JSONObject json_transaction = (JSONObject) transaction.get(0);
        final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

        final JSONArray outputs = json_transaction.getJSONArray("Outputs");
        final JSONArray privateKeyScripte = json_transaction.getJSONArray("PrivateKeyScripte");

        try {
            //解析inputs
            utxoTxInput[] utxoTxInputs = Basic.parseInputsAddress(utxoInputs).toArray(new utxoTxInput[utxoInputs.size()]);
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
                rawTx = Ela.multiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M);
            }else if (bool){
                String memo = json_transaction.getString("Memo");
                rawTx = Ela.multiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M, memo);
            }else{
                rawTx = Ela.multiSignTransaction(utxoTxInputs, txOutputs, privateKeyScripteList, privateKeySignList, M,payload);
            }
            resultMap.put("rawTx", rawTx.getRawTxString());
            resultMap.put("txHash", rawTx.getTxHash());


            return Basic.getSuccess("genTokenMultiSignTx", resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }
}
