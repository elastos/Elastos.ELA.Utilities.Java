package org.elastos.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.common.*;
import org.elastos.ela.*;
import org.elastos.ela.bitcoinj.Utils;
import org.elastos.ela.contract.ContractParameterType;
import org.elastos.ela.contract.FunctionCode;
import org.elastos.ela.outputpayload.VoteContent;
import org.elastos.ela.outputpayload.VoteOutput;
import org.elastos.ela.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.util.*;

import static org.elastos.api.Verify.Type.*;
import static org.elastos.common.InterfaceParams.*;
import static org.elastos.common.InterfaceParams.OWNER_PUBLICKEY;
import static org.elastos.common.Opcode.PACK;
import static org.elastos.common.Opcode.TAILCALL;
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
     * generate privateKey
     *
     * @return 返回json字符串
     */
    public static String genPrivateKey() {
        String privateKey = Ela.getPrivateKey();

        LOGGER.info(getSuccess(privateKey));
        return getSuccess(privateKey);
    }

    /**
     * generate publicKey
     *
     * @param jsonObject 私钥
     * @return 返回json字符串
     */
    public static String genPublicKey(JSONObject jsonObject) {
        try {
            Verify.verifyParameter(Verify.Type.PrivateKey, jsonObject);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }

        String privateKey = jsonObject.getString(PRIVATEKEY);
        String publicKey = Ela.getPublicFromPrivate(privateKey);

        LOGGER.info(getSuccess(publicKey));
        return getSuccess(publicKey);
    }

    /**
     * generate address
     *
     * @param jsonObject 私钥
     * @return 返回Json字符串
     */
    public static String genAddress(JSONObject jsonObject) {

        try {
            Verify.verifyParameter(Verify.Type.PrivateKey, jsonObject);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
        String privateKey = jsonObject.getString(PRIVATEKEY);
        String address = Ela.getAddressFromPrivate(privateKey);

        LOGGER.info(getSuccess(address));
        return getSuccess(address);
    }

    /**
     * generate pledge address
     * @param jsonObject privateKey
     * @return String
     */
    public static String genPledgeAddress(JSONObject jsonObject) {
        String publicKey = jsonObject.getString(PUBLICKEY);
        String address = ECKey.getPledgeAddress(DatatypeConverter.parseHexBinary(publicKey));

        LOGGER.info(getSuccess(address));
        return getSuccess(address);
    }
    /**
     * generate identity id
     *
     * @param jsonObject 私钥
     * @return 返回Json字符串
     */
    public static String genIdentityID(JSONObject jsonObject) {

        try {
            Verify.verifyParameter(Verify.Type.PrivateKey, jsonObject);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
        String privateKey = jsonObject.getString(PRIVATEKEY);
        String address = Ela.getIdentityIDFromPrivate(privateKey);

        LOGGER.info(getSuccess(address));
        return getSuccess(address);
    }

    /**
     * generate privateKey and publicKey and address
     *
     * @return 返回json字符串
     */
    public static String gen_priv_pub_addr() {
        String privateKey = Ela.getPrivateKey();
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        String address = Ela.getAddressFromPrivate(privateKey);
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put(PRIVATEKEY, privateKey);
        resultMap.put(PUBLICKEY, publicKey);
        resultMap.put(ADDRESS, address);

        LOGGER.info(getSuccess(resultMap));
        return getSuccess(resultMap);
    }

    /**
     * Verify that the address is ela legal
     *
     * @param addresses 字典格式或者数组格式的地址
     * @return
     */
    public static String checkAddress(JSONObject addresses) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        JSONArray addressesJSONArray = addresses.getJSONArray(ADDRESSES);
        Object addressesObject = addressesJSONArray.get(0);
        if (addressesObject instanceof String) {
            for (int i = 0; i < addressesJSONArray.size(); i++) {
                boolean boo = Util.checkAddress((String) addressesJSONArray.get(i));
                resultMap.put((String) addressesJSONArray.get(i), boo);
            }
        } else {
            for (int i = 0; i < addressesJSONArray.size(); i++) {
                JSONObject o = (JSONObject) addressesJSONArray.get(i);
                String address = o.getString(ADDRESS);
                boolean boo = Util.checkAddress(address);
                resultMap.put(address, boo);
            }
        }

        LOGGER.info(getSuccess(resultMap));
        return getSuccess(resultMap);
    }

    /**
     * json return format
     *
     * @param resultMap
     * @return
     */
    public static String getSuccess(Object resultMap) {

        HashMap map = new HashMap();
        map.put("id", null);
        map.put("jsonrpc", "2.0");
        map.put("error", null);
        map.put("result", resultMap);

        return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    public static LinkedHashMap<String, Object> getRawTxMap(String rawTx, String txHash) {

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put(InterfaceParams.RAWTX, rawTx);
        resultMap.put(InterfaceParams.TXHASH, txHash);

        return resultMap;
    }

    public static LinkedHashMap<String, Object> getRawTxAndAssetIdMap(String rawTx, String txHash, String assetId) {

        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        resultMap.put(InterfaceParams.RAWTX, rawTx);
        resultMap.put(InterfaceParams.TXHASH, txHash);
        resultMap.put(InterfaceParams.ASSET_ID, assetId);
        return resultMap;
    }


    /**
     * generates the address based on blockHash
     *
     * @param jsonObject
     * @return 返回Json字符串
     */
    public static String genGenesisAddress(JSONObject jsonObject) {

        String address;
        try {
            Verify.verifyParameter(Verify.Type.BlockHash, jsonObject);
            String blockHash = jsonObject.getString(BLOCK_HASH);
            address = ECKey.toGenesisSignAddress(blockHash);
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
        LOGGER.info(getSuccess(address));
        return getSuccess(address);
    }


    /**
     * generate multi sign address
     *
     * @return 返回json字符串
     */
    public static String genMultiSignAddress(JSONObject jsonObject) {

        String address;
        try {
            final JSONArray PrivateKeys = jsonObject.getJSONArray(PRIVATEKEYS);
            List<String> privateKeyList = new ArrayList<String>();
            for (int a = 0; a < PrivateKeys.size(); a++) {
                Verify.verifyParameter(Verify.Type.PrivateKey, (JSONObject) PrivateKeys.get(a));

                JSONObject privateKeyJson = (JSONObject) PrivateKeys.get(a);
                String privatekey = privateKeyJson.getString(PRIVATEKEY);
                privateKeyList.add(privatekey);
            }

            final int M = jsonObject.getInt(InterfaceParams.M);
            address = Ela.getMultiSignAddress(privateKeyList, M);
        } catch (SDKException e) {
            LOGGER.error(e.toString());
            return e.toString();
        }

        return getSuccess(address);
    }

    public static String genNeoContractHashAndAddress(JSONObject jsonObject) {
        try {
            String contract = jsonObject.getString(CONTRACT);
            String contractHash = Ela.genNeoContractHash(contract);
            String contractAddress = Ela.genNeoContractAddress(contractHash);
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            resultMap.put(CONTRACT_HASH, contractHash);
            resultMap.put(CONTRACT_ADDRESS, contractAddress);

            LOGGER.info(getSuccess(resultMap));
            return getSuccess(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    public static String genNeoContractAddress(JSONObject jsonObject) {
        try {
            String contractHash = jsonObject.getString(CONTRACT_HASH);
            String contractAddress = Ela.genNeoContractAddress(contractHash);

            LOGGER.info(getSuccess(contractAddress));
            return getSuccess(contractAddress);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return e.toString();
        }
    }

    //==========================================================================================

    /**
     * parse payload recode
     *
     * @param json_transaction
     * @return
     * @throws SDKException
     */
    public static PayloadRecord parsePayloadRecord(JSONObject json_transaction) throws SDKException {
        Object payload = json_transaction.get(PAYLOAD);
        if (payload != null) {
            final JSONObject PayloadObject = json_transaction.getJSONObject(PAYLOAD);

            Verify.verifyParameter(RecordType, PayloadObject);
            Verify.verifyParameter(RecordData, PayloadObject);

            return new PayloadRecord(PayloadObject.getString(RECORD_TYPE), PayloadObject.getString(RECORD_DATA));
        } else return null;
    }

    /**
     * parse output
     *
     * @param outputs
     * @return
     * @throws SDKException
     */
    public static LinkedList<TxOutput> parseOutputs(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        return getOutputs(outputs, outputList);
    }

    public static LinkedList<TxOutput> getOutputs(JSONArray outputs, LinkedList<TxOutput> outputList) throws SDKException {
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

            Verify.verifyParameter(Address, output);
            Verify.verifyParameter(AmountStr, output);

            String amount = output.getString(AMOUNT);
            String address = output.getString(ADDRESS);
            outputList.add(new TxOutput(address, amount, Common.SYSTEM_ASSET_ID, ElaPrecision));
        }
        return outputList;
    }

    public static List<UTXOTxInput> parseInputs(JSONArray utxoInputs) throws SDKException {

        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0; i < utxoInputs.size(); i++) {
            JSONObject utxoInput = (JSONObject) utxoInputs.get(i);

            Verify.verifyParameter(Verify.Type.Txid, utxoInput);
            Verify.verifyParameter(Verify.Type.Vout, utxoInput);
            Verify.verifyParameter(Verify.Type.PrivateKey, utxoInput);


            String txid = utxoInput.getString(TX_ID);
            int vout = utxoInput.getInt(VOUT);
            String privateKey = utxoInput.getString(PRIVATEKEY);
            String address = Ela.getAddressFromPrivate(privateKey);

            inputList.add(new UTXOTxInput(txid, vout, privateKey, address));
        }
        return inputList;
    }

    public static List<UTXOTxInput> parseInputsAddress(JSONArray utxoInputs) throws SDKException {

        List<UTXOTxInput> inputList = new LinkedList<UTXOTxInput>();
        for (int i = 0; i < utxoInputs.size(); i++) {
            JSONObject utxoInput = (JSONObject) utxoInputs.get(i);

            Verify.verifyParameter(Verify.Type.Txid, utxoInput);
            Verify.verifyParameter(Verify.Type.Vout, utxoInput);
            Verify.verifyParameter(Address, utxoInput);

            String txid = utxoInput.getString(TX_ID);
            int vout = utxoInput.getInt(VOUT);
            String address = utxoInput.getString(ADDRESS);
            inputList.add(new UTXOTxInput(txid, vout, "", address));
        }
        return inputList;
    }

    public static List<String> parsePrivates(JSONArray PrivateKeys) throws SDKException {
        List<String> privateList = new LinkedList<String>();
        for (int i = 0; i < PrivateKeys.size(); i++) {
            JSONObject utxoInput = (JSONObject) PrivateKeys.get(i);
            Verify.verifyParameter(Verify.Type.PrivateKey, utxoInput);
            privateList.add(utxoInput.getString(PRIVATEKEY));
        }
        //去重
        return new ArrayList<>(new HashSet<>(privateList));
    }

    public static ArrayList<String> genPrivateKeySignByM(int M, JSONArray privateKeyScripte) throws SDKException {
        if (M > privateKeyScripte.size())
            throw new SDKException(ErrorCode.ParamErr("m cannot be greater than the number of privateKeys"));

        ArrayList<String> privateKeySignList = new ArrayList<String>();
        for (int n = 0; n < M; n++) {
            JSONObject privateKeys = (JSONObject) privateKeyScripte.get(n);
            String privatekey = privateKeys.getString(PRIVATEKEY);
            privateKeySignList.add(privatekey);
        }
        return privateKeySignList;
    }

    //=================================== crossChain =======================================================

    public static LinkedList<PayloadTransferCrossChainAsset> parseCrossChainAsset(JSONArray CrossChainAsset) throws SDKException {
        LinkedList<PayloadTransferCrossChainAsset> CrossChainAssetList = new LinkedList<PayloadTransferCrossChainAsset>();
        for (int n = 0; n < CrossChainAsset.size(); n++) {
            JSONObject output = (JSONObject) CrossChainAsset.get(n);
            Verify.verifyParameter(AmountStr, output);
            String address = output.getString(ADDRESS);
            String amount = output.getString(AMOUNT);
            long longValue = Util.multiplyAmountELA(new BigDecimal(amount), ElaPrecision).longValue();
            CrossChainAssetList.add(new PayloadTransferCrossChainAsset(address, longValue, n));
        }
        return CrossChainAssetList;
    }

    /**
     * crossChain parse output
     *
     * @param outputs
     * @return
     * @throws SDKException
     */
    public static LinkedList<TxOutput> parseCrossChainOutputs(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

            Verify.verifyParameter(AmountStr, output);

            //没有用到的blockhash是为了接口灵活，找零逻辑不用很麻烦
            String amount = output.getString(AMOUNT);
            String address = output.getString(ADDRESS);
            outputList.add(new TxOutput(address, amount, Common.SYSTEM_ASSET_ID, ElaPrecision));
        }
        return outputList;
    }

    //=================================== token =======================================================

    /**
     * token parse output
     *
     * @param outputs
     * @return
     * @throws SDKException
     */
    public static LinkedList<TxOutput> parseOutputsByAsset(JSONArray outputs) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);

            Verify.verifyParameter(Verify.Type.AssetId, output);
            Verify.verifyParameter(Address, output);
            Verify.verifyParameter(AmountStr, output);

            String assetId = output.getString(ASSET_ID);
            String address = output.getString(ADDRESS);
            String amount = output.getString(AMOUNT);
            int precision = ElaPrecision;
            if (!assetId.toLowerCase().equals(Common.SYSTEM_ASSET_ID)) {
                precision = MaxPrecision;
            }
            outputList.add(new TxOutput(address, amount, assetId, precision));

        }
        return outputList;
    }

    /**
     * parse register output
     *
     * @param payload
     * @param json_transaction
     * @return
     * @throws SDKException
     */
    public static TxOutput[] parseRegisterOutput(PayloadRegisterAsset payload, JSONObject json_transaction) throws SDKException {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        final JSONArray outputs = json_transaction.getJSONArray(InterfaceParams.OUTPUTS);
        //添加注册资产
        registerToOutput(payload, outputList);
        //添加消费ELA
        getOutputs(outputs, outputList);
        return outputList.toArray(new TxOutput[outputList.size()]);
    }

    public static void registerToOutput(PayloadRegisterAsset registerAsset, LinkedList<TxOutput> outputList) {

        Long amount = registerAsset.getAmount();

        String controller = registerAsset.getController();
        byte[] programHash = DatatypeConverter.parseHexBinary(controller);
        String address = Util.ToAddress(programHash);

        outputList.add(new TxOutput(address, amount.toString(), Asset.AssetId, MaxPrecision));
    }

    /**
     * get payload register Asset
     *
     * @param json_transaction
     * @return
     * @throws SDKException
     */
    public static PayloadRegisterAsset payloadRegisterAsset(JSONObject json_transaction) throws SDKException {
        Object payload = json_transaction.get(PAYLOAD);
        if (payload != null) {
            final JSONObject PayloadObject = json_transaction.getJSONObject(PAYLOAD);

            Verify.verifyParameter(Verify.Type.Name, PayloadObject);
            Verify.verifyParameter(Verify.Type.Description, PayloadObject);
            Verify.verifyParameter(Verify.Type.Precision, PayloadObject);
            Verify.verifyParameter(Address, PayloadObject);
            Verify.verifyParameter(Verify.Type.Amount, PayloadObject);

            String assetname = PayloadObject.getString(NAME);
            String description = PayloadObject.getString(DESCRIPTION);
            int precision = PayloadObject.getInt(PRECISION);
            String address = PayloadObject.getString(ADDRESS);
            long amount = PayloadObject.getLong(AMOUNT);

            //生成assetId
            Asset asset = new Asset(assetname, description, (byte) precision, (byte) 0x00);
            return new PayloadRegisterAsset(asset, amount, address);
        }
        throw new SDKException(ErrorCode.ParamErr(PAYLOAD + " can not be empty"));
    }


    //=================================== Neo Contract =======================================================

    public static void genfunctionCode(JSONObject json_transaction) throws SDKException {

        byte returnTypeByte;
        byte[] parameterTypes;
        byte[] code;

        //ParamTypes
        Object ParamTypes = json_transaction.get(PARAMS);
        if (ParamTypes != null) {
            final JSONArray paramTypes = json_transaction.getJSONArray(PARAMS);
            List<Byte> list = new ArrayList<>();
            for (int i = 0; i < paramTypes.size(); i++) {
                String paramType = paramTypes.getString(i);
                byte iota = parameterTypemap.get(paramType);
                list.add(iota);
            }
            parameterTypes = Util.byteToByteArray(list);
        } else throw new SDKException(ErrorCode.ParamErr(PARAMS + " can not be empty"));

        //ReturnType
        Object ReturnType = json_transaction.get(RETURN_TYPE);
        if (ReturnType != null) {
            String returnTypeStr = json_transaction.getString(RETURN_TYPE);
            returnTypeByte = parameterTypemap.get(returnTypeStr);
        } else throw new SDKException(ErrorCode.ParamErr(RETURN_TYPE + " can not be empty"));

        //ContractCode
        Object ContractCode = json_transaction.get(InterfaceParams.CONTRACT_CODE);
        if (ContractCode != null) {
            String contractCodeStr = json_transaction.getString(InterfaceParams.CONTRACT_CODE);
            code = DatatypeConverter.parseHexBinary(contractCodeStr);

//            if (!(code[code.length -1] == SMART_CONTRACT)){
//                // byte[] + byte
//                byte[] smartContractArray = new byte[1];
//                smartContractArray[0] = SMART_CONTRACT;
//                byte[] code_buf = new byte[code.length + smartContractArray.length];
//                System.arraycopy(code,0,code_buf,0,code.length);
//                System.arraycopy(smartContractArray,0,code_buf,code.length,smartContractArray.length);
//                code = code_buf;
//            }

            FunctionCode functionCode = new FunctionCode(returnTypeByte, parameterTypes, code);
            PayloadDeploy.Code = functionCode;

        } else throw new SDKException(ErrorCode.ParamErr(CONTRACT_CODE + " can not be empty"));
    }

    public static PayloadDeploy parsePayloadDeploy(JSONObject json_transaction) throws SDKException {

        //programHash
        JSONObject utxoInput = (JSONObject) json_transaction.getJSONArray(INPUTS).get(0);
        String privateKey = utxoInput.getString(PRIVATEKEY);
        String address = Ela.getAddressFromPrivate(privateKey);
        byte[] programHash = Util.ToScriptHash(address);

        //Payload
        Object PayloadDeploy = json_transaction.get(PAYLOAD);
        if (PayloadDeploy != null) {
            final JSONObject PayloadObject = json_transaction.getJSONObject(PAYLOAD);

            String name = PayloadObject.getString(NAME);
            String codeVersion = PayloadObject.getString(CODE_VERSION);
            String author = PayloadObject.getString(AUTHOR);
            String email = PayloadObject.getString(EMAIL);
            String description = PayloadObject.getString(DESCRIPTION);
            String gas = PayloadObject.getString(GAS);

            long longValue = Util.multiplyAmountELA(new BigDecimal(gas), ElaPrecision).toBigInteger().longValue();

            return new PayloadDeploy(name, codeVersion, author, email, description, programHash, longValue);
        }
        throw new SDKException(ErrorCode.ParamErr(PAYLOAD + " can not be empty"));
    }


    public static PayloadInvoke genPayloadInvoke(JSONObject json_transaction) throws SDKException {
        byte[] contractHash;
        byte[] reverseContractHash;
        byte[] paramByte;

        //ParamTypes
        Object ParamTypes = json_transaction.get(PARAMS);
        if (ParamTypes != null) {
            JSONArray paramTypes = json_transaction.getJSONArray(PARAMS);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream o = new DataOutputStream(baos);

            parseJsonToBytes(o, paramTypes);

            paramByte = baos.toByteArray();
        } else throw new SDKException(ErrorCode.ParamErr(PARAMS + " can not be empty"));

        //ContractHash
        Object ContractHash = json_transaction.get(CONTRACT_HASH);
        if (ContractHash != null) {
            //合约hash不需要反转
            contractHash = DatatypeConverter.parseHexBinary(json_transaction.getString(CONTRACT_HASH));
            //去掉1c 一个字节
            byte[] tmp = new byte[contractHash.length - 1];
            System.arraycopy(contractHash, 1, tmp, 0, tmp.length);
            contractHash = tmp;
            //合约hash反转是需要和编译器一致
            reverseContractHash = Utils.reverseBytes(contractHash);
        } else throw new SDKException(ErrorCode.ParamErr(CONTRACT_HASH + " can not be empty"));

        // paramByte + TAILCALL + contractHash
        byte[] tailCall = new byte[1];
        tailCall[0] = TAILCALL;
        byte[] code_buf = new byte[paramByte.length + tailCall.length + reverseContractHash.length];

        System.arraycopy(paramByte, 0, code_buf, 0, paramByte.length);
        System.arraycopy(tailCall, 0, code_buf, paramByte.length, tailCall.length);
        System.arraycopy(reverseContractHash, 0, code_buf, paramByte.length + tailCall.length, reverseContractHash.length);
        paramByte = code_buf;

        //programHash
        JSONObject utxoInput = (JSONObject) json_transaction.getJSONArray(INPUTS).get(0);
        String privateKey = utxoInput.getString(PRIVATEKEY);
        String address = Ela.getAddressFromPrivate(privateKey);
        byte[] programHash = Util.ToScriptHash(address);

        // gas
        Object Gas = json_transaction.get(GAS);
        if (Gas != null) {
            String gas = json_transaction.getString(GAS);
            long longValue = Util.multiplyAmountELA(new BigDecimal(gas), ElaPrecision).toBigInteger().longValue();
            return new PayloadInvoke(contractHash, paramByte, programHash, longValue);
        } else throw new SDKException(ErrorCode.ParamErr(GAS + " can not be empty"));
    }

    public static void parseJsonToBytes(DataOutputStream o, JSONArray params) throws SDKException {
        // 智能合约在虚拟机是基于堆栈数据结构的，先进后出
        try {
            for (int i = params.size() - 1; i >= 0; i--) {
                JSONObject param = params.getJSONObject(i);
                Iterator iterator = param.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    switch (key) {
                        case "Boolean":
                            Paramsbuilder.emitPushBool(o, param.getBoolean(key));
                            break;
                        case "Integer":
                            Paramsbuilder.emitPushInteger(o, param.getLong(key));
                            break;
                        case "String":
                            Paramsbuilder.emitPushByteArray(o, param.getString(key).getBytes());
                            break;
                        case "Hash256":
                        case "Hash168":
                        case "ByteArray":
                            Paramsbuilder.emitPushByteArray(o, DatatypeConverter.parseHexBinary(param.getString(key)));
                            break;
                        case "Hash160":
                            byte[] paramByte = DatatypeConverter.parseHexBinary(param.getString(key));
                            if (paramByte.length == 21) {
                                byte[] tmp = new byte[paramByte.length - 1];
                                System.arraycopy(paramByte, 1, tmp, 0, tmp.length);
                                paramByte = tmp;
                            }
                            Paramsbuilder.emitPushByteArray(o, paramByte);
                            break;
                        case "Array":
                            JSONArray paramJSONArray = param.getJSONArray(key);
                            parseJsonToBytes(o, paramJSONArray);
                            Paramsbuilder.emitPushInteger(o, (long) paramJSONArray.size());
                            Paramsbuilder.emit(o, PACK);
                    }
                }
            }
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr(PARAMS + " serialize err : " + e));
        }
    }

    //=================================== Dpos =======================================================
    public static PayloadRegisterProducer payloadRegisterProducer(JSONObject json_transaction) throws SDKException {
        final JSONObject PayloadObject = json_transaction.getJSONObject(PAYLOAD);

        Verify.verifyParameter(Verify.Type.PrivateKey, PayloadObject);

        String privateKey = PayloadObject.getString(PRIVATEKEY);
        String ownerPublicKey = PayloadObject.getString(OWNER_PUBLICKEY);
        String nodePublicKey = PayloadObject.getString(NODE_PUBLICKEY);
        String nickName = PayloadObject.getString(NICK_NAME);
        String url = PayloadObject.getString(URL);
        long location = PayloadObject.getLong(LOCATION);
        String address = PayloadObject.getString(NET_ADDRESS);

        return new PayloadRegisterProducer(DatatypeConverter.parseHexBinary(ownerPublicKey), DatatypeConverter.parseHexBinary(nodePublicKey), nickName, url, location, address, DatatypeConverter.parseHexBinary(privateKey));
    }

    public static PayloadUpdateProducer payloadUpdateProducer(JSONObject json_transaction) throws SDKException {
        final JSONObject PayloadObject = json_transaction.getJSONObject(PAYLOAD);

        String privateKey = PayloadObject.getString(PRIVATEKEY);
        String ownerPublicKey = PayloadObject.getString(OWNER_PUBLICKEY);
        String nodePublicKey = PayloadObject.getString(NODE_PUBLICKEY);
        String nickName = PayloadObject.getString(NICK_NAME);
        String url = PayloadObject.getString(URL);
        long location = PayloadObject.getLong(LOCATION);
        String address = PayloadObject.getString(NET_ADDRESS);
        return new PayloadUpdateProducer(DatatypeConverter.parseHexBinary(ownerPublicKey), DatatypeConverter.parseHexBinary(nodePublicKey), nickName, url, location, address, DatatypeConverter.parseHexBinary(privateKey));
    }

    public static ProcessProducer processProducer(JSONObject json_transaction) throws SDKException {
        final JSONObject PayloadObject = json_transaction.getJSONObject(PAYLOAD);

        String privateKey = PayloadObject.getString(PRIVATEKEY);
        String publicKey = PayloadObject.getString(OWNER_PUBLICKEY);
        return new ProcessProducer(DatatypeConverter.parseHexBinary(publicKey), DatatypeConverter.parseHexBinary(privateKey));
    }

    public static LinkedList<TxOutput> parseOutputsVote(JSONArray outputs) {
        LinkedList<TxOutput> outputList = new LinkedList<TxOutput>();
        for (int t = 0; t < outputs.size(); t++) {
            JSONObject output = (JSONObject) outputs.get(t);
            String amount = output.getString(AMOUNT);
            String address = output.getString(ADDRESS);

            Object payload = output.get(PAYLOAD);
            if (payload != null){
                JSONObject p = (JSONObject) payload;
                Object content = p.get(CONTENTS);
                if (content != null) {
                    JSONArray contents = (JSONArray) content;
                    VoteContent[] voteContents = parseOuputPayload(contents,VoteContent.DELEGATE);

                    VoteOutput voteOutput = new VoteOutput(VoteOutput.VERSION, voteContents);

                    outputList.add(new TxOutput(address, amount, Common.SYSTEM_ASSET_ID, ElaPrecision, voteOutput, TxOutput.VOTE_OUTPUT));
                }
            } else {
                outputList.add(new TxOutput(address, amount, Common.SYSTEM_ASSET_ID, ElaPrecision));
            }

        }
        return outputList;
    }

    private static VoteContent[] parseOuputPayload(JSONArray outputPayloads , byte voteType) {
        LinkedList<VoteContent> voteContentList = new LinkedList<VoteContent>();

        for (Object outputPayload : outputPayloads) {
            JSONObject payload = (JSONObject) outputPayload;
            JSONArray candidates = payload.getJSONArray(CANDIDATES);

            LinkedList<byte[]> candidatesList = new LinkedList<byte[]>();

            for (Object candidate : candidates) {
                JSONObject addresses = (JSONObject) candidate;
                String publicKey = addresses.getString(PUBLICKEY);
                candidatesList.add(DatatypeConverter.parseHexBinary(publicKey));
            }
            voteContentList.add(new VoteContent(voteType, candidatesList));
        }

        return voteContentList.toArray(new VoteContent[voteContentList.size()]);
    }
}