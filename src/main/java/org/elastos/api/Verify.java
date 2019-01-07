package org.elastos.api;

import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;

import javax.xml.bind.DatatypeConverter;

import static org.elastos.common.Util.ToScriptHash;
import static org.elastos.common.Util.isAscii;

/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/21
 */
public class Verify {
    public enum Type{
        PrivateKey("privatekey"),
        Address("address"),
        ChangeAddress("changeaddress"),
        Amount("amount"),
        AmountStr("amount"),
        AssetId("assetid"),
        M("m"),
        Host("Host"),
        Fee("Fee"),
        RegisterAssetFee("RegisterAssetFee"),
        Confirmation("Confirmation"),
        Txid("txid"),
        Vout("vout"),
        Password("password"),
        RecordType("recordType"),
        RecordData("recordData"),
        BlockHash("blockhash"),

        Name("name"),
        Description("description"),
        Precision("precision"),

        RpcConfiguration("RpcConfiguration"),
        User("User"),
        Pass("Pass");

        private String type;

        private Type(String t) {
            this.type = t;
        }
        public String getValue(){
            return type;
        }
    }


    public static void verifyParameter(Type type , JSONObject jsonObject) throws SDKException {
        switch (type){
            case BlockHash:
                Object BlockHashUpper = jsonObject.get(type.getValue());
                if (BlockHashUpper != null) {
                    if (((String)BlockHashUpper).length() != 64){
                        throw new SDKException(ErrorCode.INVALID_BLOCK_HASH);
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String)BlockHashUpper);
                    }catch (Exception e){
                        throw new SDKException(ErrorCode.INVALID_BLOCK_HASH);
                    }
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case AmountStr:
                Object AmountStrLower = jsonObject.get(type.getValue());
                if (AmountStrLower != null) {
                    if (AmountStrLower instanceof String ){}else throw new SDKException(ErrorCode.INVALID_AMOUNT_STR);
                }else throw new SDKException(type.getValue() + " can not be empty") ;
                break;

            case AssetId:
                Object AssetIdLower = jsonObject.get(type.getValue());
                if (AssetIdLower != null) {
                    if (((String)AssetIdLower).length() != 64){
                        throw new SDKException(ErrorCode.INVALID_ASSET_ID);
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String)AssetIdLower);
                    }catch (Exception e){
                        throw new SDKException(ErrorCode.INVALID_ASSET_ID);
                    }
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case PrivateKey:
                Object PrivateKeyLower = jsonObject.get(type.getValue());
                if (PrivateKeyLower != null) {
                    if (((String)PrivateKeyLower).length() != 64){
                        throw new SDKException(ErrorCode.INVALID_PRPVATE_KEY);
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String)PrivateKeyLower);
                    }catch (Exception e){
                        throw new SDKException(ErrorCode.INVALID_PRPVATE_KEY);
                    }
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Txid:
                Object TxidLower = jsonObject.get(type.getValue());
                if (TxidLower != null) {
                    if (((String)TxidLower).length() != 64){
                        throw new SDKException(ErrorCode.INVALID_TX_ID);
                    }
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Password:
                Object PasswordLower = jsonObject.get(type.getValue());
                if (PasswordLower != null) {
                    if (!(PasswordLower instanceof String)){
                        throw new SDKException(ErrorCode.INVALID_PASSWORD);
                    }
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Address:
                Object AddressLower = jsonObject.get(type.getValue());
                if (AddressLower != null) {
                    try {
                        byte[] sh = ToScriptHash((String) AddressLower);
                        if(sh[0]!=33 && sh[0]!=18 && sh[0]!=28){
                            throw new SDKException(ErrorCode.INVALID_ADDRESS);
                        }
                    }catch (Exception e) {
                        throw new SDKException(ErrorCode.INVALID_ADDRESS);
                    }
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case ChangeAddress:
                Object ChangeAddress = jsonObject.get(type.getValue());
                if (ChangeAddress != null) {
                    try {
                        byte[] sh = ToScriptHash((String) ChangeAddress);
                        if(sh[0]!=33&&sh[0]!=18){
                            throw new SDKException(ErrorCode.INVALID_CHANGE_ADDRESS);
                        }
                    }catch (Exception e) {
                        throw new SDKException(ErrorCode.INVALID_CHANGE_ADDRESS);
                    }
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Host:
                Object Host = jsonObject.get(type.getValue());
                if (Host == null) throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Amount:
                Object AmountLower = jsonObject.get(type.getValue());
                if (AmountLower != null) {
                    if (AmountLower instanceof Long || AmountLower instanceof Integer && (int)AmountLower >= 0){}else throw new SDKException(ErrorCode.INVALID_AMOUNT);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case M:
                Object MUpper = jsonObject.get(type.getValue());
                if (MUpper != null) {
                    if (MUpper instanceof Integer && (int)MUpper >= 0){}else throw new SDKException(ErrorCode.INVALID_M);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Fee:
                Object fee = jsonObject.get(type.getValue());
                if (fee != null) {
                    if (fee instanceof String){}else throw new SDKException(ErrorCode.INVALID_FEE);
                }else throw new SDKException(type.getValue() + " can not be empty") ;
                break;
            case RegisterAssetFee:
                Object RegisterAssetFee = jsonObject.get(type.getValue());
                if (RegisterAssetFee != null) {
                    if (RegisterAssetFee instanceof String){}else throw new SDKException(ErrorCode.INVALID_REGISTERASSETFEE);
                }else throw new SDKException(type.getValue() + " can not be empty") ;
                break;
            case Confirmation:
                Object Confirmation = jsonObject.get(type.getValue());
                if (Confirmation != null) {
                    if (Confirmation instanceof Integer && (int)Confirmation >= 0){}else throw new SDKException(ErrorCode.INVALID_CONFIRMATION);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Vout:
                Object IndexLower = jsonObject.get(type.getValue());
                if (IndexLower != null) {
                    if (IndexLower instanceof Long || IndexLower instanceof Integer && (int)IndexLower  >= 0){}else throw new SDKException(ErrorCode.INVALID_VOUT);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case RecordType:
                Object RecordTypeLower = jsonObject.get(type.getValue());
                if (RecordTypeLower != null) {
                    if (Util.isChinese((String)RecordTypeLower))throw new SDKException(ErrorCode.INVALID_RECORD_TYPE);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case RecordData:
                Object RecordDataLower = jsonObject.get(type.getValue());
                if (RecordDataLower != null) {
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;

            case Name:
                Object NameLower = jsonObject.get(type.getValue());
                if (NameLower != null) {
                    boolean isWord=((String)NameLower).matches("[a-zA-Z0-9]+");
                    if (!isWord)throw new SDKException(ErrorCode.INVALID_NAME);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Description:
                Object DescriptionLower = jsonObject.get(type.getValue());
                if (DescriptionLower != null) {
                    if (!isAscii((String)DescriptionLower))throw new SDKException(ErrorCode.INVALID_DESCRIPTION);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case Precision:
                Object PrecisionLower = jsonObject.get(type.getValue());
                if (PrecisionLower != null) {
                    if (PrecisionLower instanceof Integer && (int)PrecisionLower >= 0){}else throw new SDKException(ErrorCode.INVALID_PRECISION);
                }else throw new SDKException(type.getValue() + " can not be empty");
                break;
            case RpcConfiguration:
                Object RpcConfiguration = jsonObject.get(type.getValue());
                if (RpcConfiguration != null) {
                    if (RpcConfiguration instanceof JSONObject){}else throw new SDKException(ErrorCode.INVALID_RPCCONFIGURATION);
                }else throw new SDKException(type.getValue() + " can not be empty") ;
                break;
            case User:
                Object User = jsonObject.get(type.getValue());
                if (User != null) {
                    if (User instanceof String){}else throw new SDKException(ErrorCode.INVALID_USER);
                }else throw new SDKException(type.getValue() + " can not be empty") ;
                break;
            case Pass:
                Object Pass = jsonObject.get(type.getValue());
                if (Pass != null) {
                    if (Pass instanceof String){}else throw new SDKException(ErrorCode.INVALID_PASS);
                }else throw new SDKException(type.getValue() + " can not be empty") ;
                break;
        }
    }

}
