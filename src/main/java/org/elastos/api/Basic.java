package org.elastos.api;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static org.elastos.ela.PayloadRegisterAsset.ElaPrecision;
import static org.elastos.ela.PayloadRegisterAsset.MaxPrecision;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/21
 */
public class Basic {

    private static final Logger LOGGER = LoggerFactory.getLogger(Basic.class);
    /**
     * 生成私钥
     * @return 返回json字符串
     */
    public static String genPrivateKey(){
        String privateKey = Ela.getPrivateKey();

        LOGGER.info(getSuccess("genPrivateKey",privateKey));
        return getSuccess("genPrivateKey",privateKey);
    }

    /**
     * 生成公钥
     * @param jsonObject  私钥
     * @return 返回json字符串
     */
    public static String genPublicKey(JSONObject jsonObject){
        try {
            Verify.verifyParameter(Verify.Type.PrivateKeyUpper,jsonObject);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }

        String privateKey = jsonObject.getString("PrivateKey");
        String publicKey = Ela.getPublicFromPrivate(privateKey);

        LOGGER.info(getSuccess("genPublicKey",publicKey));
        return getSuccess("genPublicKey",publicKey);
    }

    /**
     * 生成地址
     * @param jsonObject  私钥
     * @return  返回Json字符串
     */
    public static String genAddress(JSONObject jsonObject){

        try {
            Verify.verifyParameter(Verify.Type.PrivateKeyUpper,jsonObject);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }
        String privateKey = jsonObject.getString("PrivateKey");
        String address    = Ela.getAddressFromPrivate(privateKey);

        LOGGER.info(getSuccess("genAddress",address));
        return getSuccess("genAddress",address);
    }

    /**
     * 生成身份id
     * @param jsonObject  私钥
     * @return  返回Json字符串
     */
    public static String genIdentityID(JSONObject jsonObject){

        try {
            Verify.verifyParameter(Verify.Type.PrivateKeyUpper,jsonObject);
        }catch (Exception e){
            LOGGER.error(e.toString());
            return e.toString();
        }
        String privateKey = jsonObject.getString("PrivateKey");
        String address    = Ela.getIdentityIDFromPrivate(privateKey);

        LOGGER.info(getSuccess("genIdentityID",address));
        return getSuccess("genIdentityID",address);
    }

    /**
     * 生成私钥、公钥、地址
     * @return  返回json字符串
     */
    public static String gen_priv_pub_addr(){
        String privateKey = Ela.getPrivateKey();
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        String address    = Ela.getAddressFromPrivate(privateKey);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put("PrivateKey",privateKey);
        resultMap.put("PublicKey",publicKey);
        resultMap.put("Address",address);

        LOGGER.info(getSuccess("gen_priv_pub_addr",resultMap));
        return getSuccess("gen_priv_pub_addr",resultMap);
    }

    /**
     * 校验地址是否为ela合法地址
     * @param addresses 字典格式或者数组格式的地址
     * @return
     */
    public static String checkAddress(JSONObject addresses){
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        JSONArray addressesJSONArray = addresses.getJSONArray("Addresses");
        Object addressesObject = addressesJSONArray.get(0);
        if (addressesObject instanceof String){
            for (int i = 0 ; i < addressesJSONArray.size() ; i ++){
                boolean boo =  Util.checkAddress((String)addressesJSONArray.get(i));
                resultMap.put((String)addressesJSONArray.get(i),boo);
            }
        }else {
            for (int i = 0 ; i < addressesJSONArray.size() ; i ++){
                JSONObject o = (JSONObject) addressesJSONArray.get(i);
                String address = o.getString("address");
                boolean boo =  Util.checkAddress(address);
                resultMap.put(address,boo);
            }
        }

        LOGGER.info(getSuccess("checkAddress",resultMap));
        return getSuccess("checkAddress",resultMap) ;
    }

    public static String getSuccess(String action, Object resultMap){
        HashMap map = new HashMap();
        map.put("Action",action);
        map.put("Desc","SUCCESS");
        map.put("Result",resultMap);

        return JSON.toJSONString(map);
    }

    /**
     * 根据blockHash 生成地址
     *
     * @param jsonObject
     * @return 返回Json字符串
     */
    public static String genGenesisAddress(JSONObject jsonObject) {

        String address = null;
        try {
            Verify.verifyParameter(Verify.Type.BlockHashUpper,jsonObject);
            String blockHash = jsonObject.getString("BlockHash");
            address = ECKey.toGenesisSignAddress(blockHash);
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
        LOGGER.info(getSuccess("genGenesisAddress",address));
        return getSuccess("genGenesisAddress",address);
    }


    /**
     * 生成多签地址
     *
     * @return 返回json字符串
     */
    public static String genMultiSignAddress(JSONObject jsonObject) {

        String address = null;
        try {
            final JSONArray PrivateKeys = jsonObject.getJSONArray("PrivateKeys");
            List<String> privateKeyList = new ArrayList<String>();
            for (int a = 0; a < PrivateKeys.size(); a++) {
                Verify.verifyParameter(Verify.Type.PrivateKeyLower,(JSONObject) PrivateKeys.get(a));

                JSONObject privateKeyJson = (JSONObject) PrivateKeys.get(a);
                String privatekey = privateKeyJson.getString("privateKey");
                privateKeyList.add(privatekey);
            }

            final int M = jsonObject.getInt("M");
            address = Ela.getMultiSignAddress(privateKeyList, M);
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }

        return getSuccess("genMultiSignAddress" , address);
    }


    public static PayloadRecord parsePayloadRecord(JSONObject json_transaction) throws SDKException {
        Object payload = json_transaction.get("PayloadRecord");
        if (payload != null){
            final JSONObject PayloadObject = json_transaction.getJSONObject("PayloadRecord");

            Verify.verifyParameter(Verify.Type.RecordTypeLower,PayloadObject);
            Verify.verifyParameter(Verify.Type.RecordDataLower,PayloadObject);

            return new PayloadRecord(PayloadObject.getString("recordType"),PayloadObject.getString("recordData"));
        }
        return null;
    }

    public static PayloadRegisterAsset payloadRegisterAsset(JSONObject json_transaction) throws SDKException {
        Object payload = json_transaction.get("PayloadRegisterAsset");
        if (payload != null){
            final JSONObject PayloadObject = json_transaction.getJSONObject("PayloadRegisterAsset");
            String assetname = PayloadObject.getString("name");
            String description = PayloadObject.getString("description");
            int precision = PayloadObject.getInt("precision");
            String address = PayloadObject.getString("address");
            long amount = PayloadObject.getLong("amount");

            //生成assetId
            Asset asset = new Asset(assetname, description, (byte) precision, (byte) 0x00);
            return new PayloadRegisterAsset(asset,amount,address);
        }throw new SDKException(ErrorCode.ParamErr("PayloadRegisterAsset can not be empty"));
    }


    public static TxOutput[] parseRegisterAsset(JSONObject json_transaction) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        //添加注册资产
        final JSONObject PayloadObject = json_transaction.getJSONObject("PayloadRegisterAsset");
        String tokenAddress = PayloadObject.getString("address");
        String tokenAmount = PayloadObject.getString("amount");
        outputList.add(new TxOutput(tokenAddress, tokenAmount,Asset.AssetId,MaxPrecision));

        //添加消费ELA
        final JSONObject output = json_transaction.getJSONObject("Outputs");
        String amount = output.getString("amount");
        String address = output.getString("address");
        outputList.add(new TxOutput(address, amount,Common.SystemAssetID,ElaPrecision));

        return outputList.toArray(new TxOutput[outputList.size()]);
    }

    public static LinkedList<TxOutput>  parseOutputsByAsset(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

//            Verify.verifyParameter(Verify.Type.AddressLower,output);
//            Verify.verifyParameter(Verify.Type.AmountLower,output);

            String  assetId = output.getString("assetId");
            String address = output.getString("address");
            String  amount = output.getString("amount");
            int precision = output.getInt("precision");
            if (!assetId.toLowerCase().equals(Common.SystemAssetID)){
                precision = MaxPrecision;
            }
            outputList.add(new TxOutput(address,amount,assetId,precision));

        }
        return outputList;
    }


    public static LinkedList<TxOutput> parseOutputs(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

            Verify.verifyParameter(Verify.Type.AddressLower,output);
            Verify.verifyParameter(Verify.Type.AmountLower,output);

            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }
        return outputList;
    }


    public static LinkedList<TxOutput> parseCrossChainOutputs(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

            Verify.verifyParameter(Verify.Type.AmountLower,output);

            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }
        return outputList;
    }

    public static List<UTXOTxInput> parseInputs(JSONArray utxoInputs) throws SDKException {

        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0 ; i < utxoInputs.size() ; i++){
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);

            Verify.verifyParameter(Verify.Type.TxidLower,utxoInput);
            Verify.verifyParameter(Verify.Type.IndexLower,utxoInput);
            Verify.verifyParameter(Verify.Type.PrivateKeyLower,utxoInput);


            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInt("index");
            String privateKey = utxoInput.getString("privateKey");
            String address = Ela.getAddressFromPrivate(privateKey);

            inputList.add(new UTXOTxInput(txid,index,privateKey,address));
        }
        return inputList;
    }

    public static List<UTXOTxInput> parseInputsAddress(JSONArray utxoInputs) throws SDKException {

        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0 ; i < utxoInputs.size() ; i++){
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);

            Verify.verifyParameter(Verify.Type.TxidLower,utxoInput);
            Verify.verifyParameter(Verify.Type.IndexLower,utxoInput);
            Verify.verifyParameter(Verify.Type.AddressLower,utxoInput);


            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInt("index");
            String address = utxoInput.getString("address");
            inputList.add(new UTXOTxInput(txid,index,"",address));
        }
        return inputList;
    }

    public static List<String> parsePrivates(JSONArray PrivateKeys) throws SDKException {
        List<String> privateList = new LinkedList<String>();
        for (int i = 0; i < PrivateKeys.size(); i++) {
            JSONObject utxoInput = (JSONObject) PrivateKeys.get(i);
            Verify.verifyParameter(Verify.Type.PrivateKeyLower,utxoInput);
            privateList.add(utxoInput.getString("privateKey"));
        }
        //去重
        ArrayList<String> privates = new ArrayList<String>(new HashSet<String>(privateList));
        return privates;
    }

    public static ArrayList<String> genPrivateKeySignByM(int M , JSONArray privateKeyScripte) throws SDKException {
        if (M > privateKeyScripte.size()) throw new SDKException(ErrorCode.ParamErr("M cannot be greater than the number of privateKeys"));

        ArrayList<String> privateKeySignList = new ArrayList<String>();
        for (int n = 0; n < M; n++) {
            JSONObject privateKeys = (JSONObject) privateKeyScripte.get(n);
            String privatekey = privateKeys.getString("privateKey");
            privateKeySignList.add(privatekey);
        }
        return privateKeySignList;
    }

    public static LinkedList<PayloadTransferCrossChainAsset> parseCrossChainAsset(JSONArray CrossChainAsset) throws SDKException {
        LinkedList<PayloadTransferCrossChainAsset> CrossChainAssetList = new LinkedList<PayloadTransferCrossChainAsset>();
        for (int n = 0; n < CrossChainAsset.size(); n++) {
            JSONObject output = (JSONObject) CrossChainAsset.get(n);
            Verify.verifyParameter(Verify.Type.AmountLower,output);
            String address = output.getString("address");
            Long amount = output.getLong("amount");
            int index_ = n;
            CrossChainAssetList.add(new PayloadTransferCrossChainAsset(address, amount, index_));
        }
        return CrossChainAssetList;
    }
}
