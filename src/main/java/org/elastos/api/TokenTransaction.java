package org.elastos.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.ela.Ela;
import org.elastos.ela.RawTx;
import org.elastos.ela.TxOutput;
import org.elastos.ela.utxoTxInput;
import org.elastos.ela.payload.PayloadRecord;
import org.elastos.ela.payload.PayloadRegisterAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TokenTransaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenTransaction.class);

    public static String genRegisterTransaction(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");

            //解析inputs
            utxoTxInput[] utxoTxInputs = Basic.parseInputs(utxoInputs).toArray(new utxoTxInput[utxoInputs.size()]);
            //解析PayloadRegisterAsset
            PayloadRegisterAsset payload   = Basic.payloadRegisterAsset(json_transaction);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseRegisterOutput(json_transaction);

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx = Ela.makeAndSignTx(utxoTxInputs,txOutputs,payload);
            resultMap.put("rawTx",rawTx.getRawTxString());
            resultMap.put("txHash",rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genRegisterTransaction",resultMap));
            return Basic.getSuccess("genRegisterTransaction",resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genRawTransactionByToken(JSONObject inputsAddOutpus){
        try {
            final JSONArray transaction = inputsAddOutpus.getJSONArray("Transactions");
            JSONObject json_transaction = (JSONObject) transaction.get(0);
            final JSONArray utxoInputs = json_transaction.getJSONArray("UTXOInputs");
            final JSONArray outputs = json_transaction.getJSONArray("Outputs");

            //解析inputs
            utxoTxInput[] utxoTxInputs = Basic.parseInputs(utxoInputs).toArray(new utxoTxInput[utxoInputs.size()]);
            //解析outputs
            TxOutput[] txOutputs = Basic.parseOutputsByAsset(outputs).toArray(new TxOutput[outputs.size()]);
            //解析payloadRecord
            PayloadRecord payload   = Basic.parsePayloadRecord(json_transaction);

            boolean bool = json_transaction.has("Memo");

            //创建rawTransaction
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            RawTx rawTx ;

            if (payload != null && bool){
                return ErrorCode.ParamErr("PayloadRecord And Memo can't be used at the same time");
            }else if (payload == null && !bool){
                rawTx = Ela.makeAndSignTx(utxoTxInputs,txOutputs);
            }else if (bool){
                String memo = json_transaction.getString("Memo");
                rawTx = Ela.makeAndSignTx(utxoTxInputs,txOutputs,memo);
            }else{
                rawTx = Ela.makeAndSignTx(utxoTxInputs,txOutputs,payload);
            }
            resultMap.put("rawTx",rawTx.getRawTxString());
            resultMap.put("txHash",rawTx.getTxHash());

            LOGGER.info(Basic.getSuccess("genRawTransactionByToken",resultMap));
            return Basic.getSuccess("genRawTransactionByToken",resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genMultiSignRawTransactionByToken(JSONObject inputsAddOutpus){

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


            return Basic.getSuccess("genMultiSignRawTransactionByToken", resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }
}
