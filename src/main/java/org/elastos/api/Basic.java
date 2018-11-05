package org.elastos.api;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.*;
import org.elastos.ela.*;
import org.elastos.ela.contract.ContractParameterType;
import org.elastos.ela.contract.FunctionCode;
import org.elastos.ela.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;

import static org.elastos.common.Opcode.PACK;
import static org.elastos.ela.Tx.SMART_CONTRACT;
import static org.elastos.ela.payload.PayloadRegisterAsset.ElaPrecision;
import static org.elastos.ela.payload.PayloadRegisterAsset.MaxPrecision;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/21
 */
public class Basic {

    private static final HashMap<String, Byte> parameterTypemap = ContractParameterType.ContractParameterTypemap();
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

        String address ;
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

        String address ;
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

            Verify.verifyParameter(Verify.Type.NameLower,PayloadObject);
            Verify.verifyParameter(Verify.Type.DescriptionLower,PayloadObject);
            Verify.verifyParameter(Verify.Type.PrecisionLower,PayloadObject);
            Verify.verifyParameter(Verify.Type.AddressLower,PayloadObject);
            Verify.verifyParameter(Verify.Type.AmountLower,PayloadObject);

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


    public static TxOutput[] parseRegisterOutput(JSONObject json_transaction) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        final JSONArray outputs = json_transaction.getJSONArray("Outputs");

        //添加注册资产
        final JSONObject PayloadObject = json_transaction.getJSONObject("PayloadRegisterAsset");
        String tokenAddress = PayloadObject.getString("address");
        String tokenAmount = PayloadObject.getString("amount");
        outputList.add(new TxOutput(tokenAddress, tokenAmount,Asset.AssetId,MaxPrecision));

        for (int t = 0; t < outputs.size(); t++) {
            final JSONObject output = (JSONObject) outputs.get(t);
            //添加消费ELA
            Verify.verifyParameter(Verify.Type.AddressLower,output);
            Verify.verifyParameter(Verify.Type.AmountStrLower,output);

            String amount = output.getString("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount,Common.SYSTEM_ASSET_ID,ElaPrecision));
        }
        return outputList.toArray(new TxOutput[outputList.size()]);
    }

    public static LinkedList<TxOutput> parseOutputsAmountStr(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

            Verify.verifyParameter(Verify.Type.AddressLower,output);
            Verify.verifyParameter(Verify.Type.AmountStrLower,output);

            String amount = output.getString("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount,Common.SYSTEM_ASSET_ID,ElaPrecision));
        }
        return outputList;
    }

    public static LinkedList<TxOutput>  parseOutputsByAsset(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

            Verify.verifyParameter(Verify.Type.AssetIdLower,output);
            Verify.verifyParameter(Verify.Type.AddressLower,output);
            Verify.verifyParameter(Verify.Type.AmountStrLower,output);

            String  assetId = output.getString("assetId");
            String address = output.getString("address");
            String  amount = output.getString("amount");
            int precision = ElaPrecision;
            if (!assetId.toLowerCase().equals(Common.SYSTEM_ASSET_ID)){
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

            //没有用到的blockhash是为了接口灵活，找零逻辑不用很麻烦
            long amount = output.getLong("amount");
            String address = output.getString("address");
            outputList.add(new TxOutput(address, amount));
        }
        return outputList;
    }

    public static List<utxoTxInput> parseInputs(JSONArray utxoInputs) throws SDKException {

        List<utxoTxInput> inputList = new LinkedList<utxoTxInput>();
        for (int i = 0 ; i < utxoInputs.size() ; i++){
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);

            Verify.verifyParameter(Verify.Type.TxidLower,utxoInput);
            Verify.verifyParameter(Verify.Type.IndexLower,utxoInput);
            Verify.verifyParameter(Verify.Type.PrivateKeyLower,utxoInput);


            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInt("index");
            String privateKey = utxoInput.getString("privateKey");
            String address = Ela.getAddressFromPrivate(privateKey);

            inputList.add(new utxoTxInput(txid,index,privateKey,address));
        }
        return inputList;
    }

    public static List<utxoTxInput> parseInputsAddress(JSONArray utxoInputs) throws SDKException {

        List<utxoTxInput> inputList = new LinkedList<utxoTxInput>();
        for (int i = 0 ; i < utxoInputs.size() ; i++){
            JSONObject utxoInput = (JSONObject)utxoInputs.get(i);

            Verify.verifyParameter(Verify.Type.TxidLower,utxoInput);
            Verify.verifyParameter(Verify.Type.IndexLower,utxoInput);
            Verify.verifyParameter(Verify.Type.AddressLower,utxoInput);

            String txid = utxoInput.getString("txid");
            int index = utxoInput.getInt("index");
            String address = utxoInput.getString("address");
            inputList.add(new utxoTxInput(txid,index,"",address));
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
        return new ArrayList<>(new HashSet<>(privateList));
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
            CrossChainAssetList.add(new PayloadTransferCrossChainAsset(address, amount, n));
        }
        return CrossChainAssetList;
    }


    //=================================== Neo Contract =======================================================

    public static FunctionCode genfunctionCode(JSONObject json_transaction) throws SDKException {

        byte returnTypeByte ;
        byte[] parameterTypes ;
        byte[] code ;

        Object ParamTypes = json_transaction.get("ParamTypes");
        if (ParamTypes != null){
            final JSONArray paramTypes = json_transaction.getJSONArray("ParamTypes");
            List<Byte> list = new ArrayList<>();
            for (int i = 0; i<paramTypes.size();i++){
                String paramType = paramTypes.getString(i);
                byte iota = parameterTypemap.get(paramType);
                list.add(iota);
            }
            parameterTypes = Util.byteToByteArray(list);
        }else throw  new SDKException(ErrorCode.ParamErr("ParamTypes can not be empty"));

        Object ReturnType = json_transaction.get("ReturnType");
        if (ReturnType != null){
            String returnTypeStr = json_transaction.getString("ReturnType");
            returnTypeByte = parameterTypemap.get(returnTypeStr);
        }else throw new SDKException(ErrorCode.ParamErr("ReturnType can not be empty"));

        Object ContractCode = json_transaction.get("ContractCode");
        if (ContractCode != null){
            String contractCodeStr = json_transaction.getString("ContractCode");
            code = DatatypeConverter.parseHexBinary(contractCodeStr);

            // byte[] + byte
            byte[] smartContractArray = new byte[1];
            smartContractArray[0] = SMART_CONTRACT;
            byte[] code_buf = new byte[code.length + smartContractArray.length];
            System.arraycopy(code,0,code_buf,0,code.length);
            System.arraycopy(smartContractArray,0,code_buf,code.length,smartContractArray.length);

            FunctionCode functionCode = new FunctionCode(returnTypeByte, parameterTypes, code_buf);
            PayloadDeploy.Code = functionCode;

            return functionCode;
        }else throw new SDKException(ErrorCode.ParamErr("ContractCode can not be empty"));
    }

    public static PayloadDeploy parsePayloadDeploy(JSONObject json_transaction) throws SDKException {

        JSONObject utxoInput = (JSONObject) json_transaction.getJSONArray("UTXOInputs").get(0);
        String privateKey = utxoInput.getString("privateKey");
        String publickey = Ela.getPublicFromPrivate(privateKey);

        Object PayloadDeploy = json_transaction.get("PayloadDeploy");
        if (PayloadDeploy != null){
            final JSONObject PayloadObject = json_transaction.getJSONObject("PayloadDeploy");

            String name = PayloadObject.getString("name");
            String codeVersion = PayloadObject.getString("codeVersion");
            String address = PayloadObject.getString("author");
            String email = PayloadObject.getString("email");
            String description = PayloadObject.getString("description");
            long gas = PayloadObject.getLong("Gas");

            return new PayloadDeploy(name,codeVersion,address,email,description,publickey,gas);
        }throw new SDKException(ErrorCode.ParamErr("PayloadDeploy can not be empty"));
    }


    public static PayloadInvoke genPayloadInvoke(JSONObject json_transaction) throws SDKException {
        String contractCode;
        byte[] paramByte;

        Object ParamTypes = json_transaction.get("ParamTypes");
        if (ParamTypes != null) {
            JSONArray paramTypes = json_transaction.getJSONArray("ParamTypes");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream o = new DataOutputStream(baos);

            parseJsonToBytes(o,paramTypes);

            paramByte = baos.toByteArray();
        }else throw new SDKException(ErrorCode.ParamErr("ParamTypes can not be empty"));

        Object ContractCode = json_transaction.get("ContractCode");
        if (ContractCode != null) {
            contractCode = json_transaction.getString("ContractCode");
        }else throw new SDKException(ErrorCode.ParamErr("ContractCode can not be empty"));

        JSONObject utxoInput = (JSONObject) json_transaction.getJSONArray("UTXOInputs").get(0);
        String privateKey = utxoInput.getString("privateKey");
        String publickey = Ela.getPublicFromPrivate(privateKey);

        Object Gas = json_transaction.get("Gas");
        if (Gas != null) {
            long gas = json_transaction.getLong("Gas");
            return new PayloadInvoke(contractCode,paramByte,publickey,gas);
        }else throw new SDKException(ErrorCode.ParamErr("Gas can not be empty"));
    }

    public static void parseJsonToBytes(DataOutputStream o, JSONArray params) throws SDKException {
        // 智能合约在虚拟机是基于堆栈数据结构的，先进后出
        try {
            for (int i = params.size() - 1; i >= 0 ; i--){
                JSONObject param = params.getJSONObject(i);
                Iterator iterator = param.keys();
                while(iterator.hasNext()) {
                    String  key = (String) iterator.next();
                    switch (key){
                        case "Boolean":
                            Paramsbuilder.emitPushBool(o,param.getBoolean(key));
                            break;
                        case "Integer":
                            Paramsbuilder.emitPushInteger(o,param.getLong(key));
                            break;
                        case "String":
                            Paramsbuilder.emitPushByteArray(o,key.getBytes());
                            break;
                        case "Hash256":
                        case "Hash168":
                        case "ByteArray":
                            Paramsbuilder.emitPushByteArray(o,DatatypeConverter.parseHexBinary(param.getString(key)));
                            break;
                        case "Hash160":
                            byte[] paramByte = DatatypeConverter.parseHexBinary(param.getString(key));
                            if (paramByte.length == 21){
                                byte[] tmp = new byte[paramByte.length - 1];
                                System.arraycopy(paramByte,1,tmp,0,tmp.length);
                                paramByte = tmp;
                            }
                            Paramsbuilder.emitPushByteArray(o,paramByte);
                            break;
                        case "Array":
                            JSONArray paramJSONArray = param.getJSONArray(key);
                            parseJsonToBytes(o,paramJSONArray);
                            Paramsbuilder.emitPushInteger(o,(long)paramJSONArray.size());
                            Paramsbuilder.emit(o,PACK);
                    }
                }
            }
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("ParamTypes serialize err : " + e));
        }

    }
}
