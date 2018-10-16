package org.elastos.api;

import net.sf.json.JSONObject;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.Util;

import javax.xml.bind.DatatypeConverter;

import static org.elastos.ela.Util.ToScriptHash;
/**
 * @author: DongLei.Tan
 * @contact: tandonglei28@gmail.com
 * @time: 2018/9/21
 */
public class Verify {
    public enum Type{
        PrivateKeyLower("privateKey"),
        PrivateKeyUpper("PrivateKey"),
        AddressLower("address"),
        ChangeAddress("ChangeAddress"),
        AmountLower("amount"),
        TokenAmountLower("amount"),
        AssetIdLower("assetId"),
        MUpper("M"),
        Host("Host"),
        Fee("Fee"),
        Confirmation("Confirmation"),
        TxidLower("txid"),
        IndexLower("index"),
        PasswordLower("password"),
        RecordTypeLower("recordType"),
        RecordDataLower("recordData"),
        BlockHashUpper("BlockHash"),

        NameLower("name"),
        DescriptionLower("description"),
        PrecisionLower("precision");
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
            case PrivateKeyUpper:
                Object PrivateKeyUpper = jsonObject.get(type.getValue());
                if (PrivateKeyUpper != null) {
                    if (((String)PrivateKeyUpper).length() != 64){
                        throw new SDKException(ErrorCode.InvalidPrpvateKey);
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String)PrivateKeyUpper);
                    }catch (Exception e){
                        throw new SDKException(ErrorCode.InvalidPrpvateKey);
                    }
                }else throw new SDKException(ErrorCode.PrivateKeyNotNull);
                break;
            case BlockHashUpper:
                Object BlockHashUpper = jsonObject.get(type.getValue());
                if (BlockHashUpper != null) {
                    if (((String)BlockHashUpper).length() != 64){
                        throw new SDKException(ErrorCode.InvalidBlockHash);
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String)BlockHashUpper);
                    }catch (Exception e){
                        throw new SDKException(ErrorCode.InvalidBlockHash);
                    }
                }else throw new SDKException(ErrorCode.BlockHashNotNull);
                break;
            case TokenAmountLower:
                Object TokenAmountLower = jsonObject.get(type.getValue());
                if (TokenAmountLower != null) {
                    if (TokenAmountLower instanceof String ){}else throw new SDKException(ErrorCode.InvalidTokenAmount);
                }else throw new SDKException(ErrorCode.TokenAmountNotNull) ;
                break;
            case AssetIdLower:
                Object AssetIdLower = jsonObject.get(type.getValue());
                if (AssetIdLower != null) {
                    if (((String)AssetIdLower).length() != 64){
                        throw new SDKException(ErrorCode.InvalidAssetId);
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String)AssetIdLower);
                    }catch (Exception e){
                        throw new SDKException(ErrorCode.InvalidAssetId);
                    }
                }else throw new SDKException(ErrorCode.AssetIdNotNull);
                break;
            case PrivateKeyLower:
                Object PrivateKeyLower = jsonObject.get(type.getValue());
                if (PrivateKeyLower != null) {
                    if (((String)PrivateKeyLower).length() != 64){
                        throw new SDKException(ErrorCode.InvalidPrpvateKey);
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String)PrivateKeyLower);
                    }catch (Exception e){
                        throw new SDKException(ErrorCode.InvalidPrpvateKey);
                    }
                }else throw new SDKException(ErrorCode.PrivateKeyNotNull);
                break;
            case TxidLower:
                Object TxidLower = jsonObject.get(type.getValue());
                if (TxidLower != null) {
                    if (((String)TxidLower).length() != 64){
                        throw new SDKException(ErrorCode.InvalidTxid);
                    }
                }else throw new SDKException(ErrorCode.TxidnotNull);
                break;
            case PasswordLower:
                Object PasswordLower = jsonObject.get(type.getValue());
                if (PasswordLower != null) {
                    if (!(PasswordLower instanceof String)){
                        throw new SDKException(ErrorCode.InvalidPassword);
                    }
                }else throw new SDKException(ErrorCode.PasswordNotNull);
                break;
            case AddressLower:
                Object AddressLower = jsonObject.get(type.getValue());
                if (AddressLower != null) {
                    try {
                        byte[] sh = ToScriptHash((String) AddressLower);
                        if(sh[0]!=33&&sh[0]!=18){
                            throw new SDKException(ErrorCode.InvalidAddress);
                        }
                    }catch (Exception e) {
                        throw new SDKException(ErrorCode.InvalidAddress);
                    }
                }else throw new SDKException(ErrorCode.AddressNotNull);
                break;
            case ChangeAddress:
                Object ChangeAddress = jsonObject.get(type.getValue());
                if (ChangeAddress != null) {
                    try {
                        byte[] sh = ToScriptHash((String) ChangeAddress);
                        if(sh[0]!=33&&sh[0]!=18){
                            throw new SDKException(ErrorCode.InvalidChangeAddress);
                        }
                    }catch (Exception e) {
                        throw new SDKException(ErrorCode.InvalidChangeAddress);
                    }
                }else throw new SDKException(ErrorCode.ChangeAddressNotNull);
                break;
            case Host:
                Object Host = jsonObject.get(type.getValue());
                if (Host == null) throw new SDKException(ErrorCode.HostNotNull);
                break;
            case AmountLower:
                Object AmountLower = jsonObject.get(type.getValue());
                if (AmountLower != null) {
                    if (AmountLower instanceof Long || AmountLower instanceof Integer && (int)AmountLower >= 0){}else throw new SDKException(ErrorCode.InvalidAmount);
                }else throw new SDKException(ErrorCode.AmountNotNull);
                break;
            case MUpper:
                Object MUpper = jsonObject.get(type.getValue());
                if (MUpper != null) {
                    if (MUpper instanceof Integer && (int)MUpper >= 0){}else throw new SDKException(ErrorCode.InvalidM);
                }else throw new SDKException(ErrorCode.MNotNull);
                break;
            case Fee:
                Object fee = jsonObject.get(type.getValue());
                if (fee != null) {
                    if (fee instanceof Long || fee instanceof Integer && (int)fee >= 0){}else throw new SDKException(ErrorCode.InvalFee);
                }else throw new SDKException(ErrorCode.FeeNotNull) ;
                break;
            case Confirmation:
                Object Confirmation = jsonObject.get(type.getValue());
                if (Confirmation != null) {
                    if (Confirmation instanceof Integer && (int)Confirmation >= 0){}else throw new SDKException(ErrorCode.InvalConfirmation);
                }else throw new SDKException(ErrorCode.ConfirmationNotNull);
                break;
            case IndexLower:
                Object IndexLower = jsonObject.get(type.getValue());
                if (IndexLower != null) {
                    if (IndexLower instanceof Long || IndexLower instanceof Integer && (int)IndexLower  >= 0){}else throw new SDKException(ErrorCode.InvalidIndex);
                }else throw new SDKException(ErrorCode.IndexNotNull);
                break;
            case RecordTypeLower:
                Object RecordTypeLower = jsonObject.get(type.getValue());
                if (RecordTypeLower != null) {
                    if (Util.isChinese((String)RecordTypeLower))throw new SDKException(ErrorCode.InvalidRecordType);
                }else throw new SDKException(ErrorCode.RecordTypeNotNull);
                break;
            case RecordDataLower:
                Object RecordDataLower = jsonObject.get(type.getValue());
                if (RecordDataLower != null) {
                }else throw new SDKException(ErrorCode.RecordDataNotNull);
                break;


            case NameLower:
                Object NameLower = jsonObject.get(type.getValue());
                if (NameLower != null) {
                    boolean isWord=((String)NameLower).matches("[a-zA-Z]+");
                    if (!isWord)throw new SDKException(ErrorCode.InvalidName);
                }else throw new SDKException(ErrorCode.NameNotNull);
                break;
            case DescriptionLower:
                Object DescriptionLower = jsonObject.get(type.getValue());
                if (DescriptionLower != null) {
                    if (Util.isChinese((String)DescriptionLower))throw new SDKException(ErrorCode.InvalidDescription);
                }else throw new SDKException(ErrorCode.DescriptionNotNull);
                break;
            case PrecisionLower:
                Object PrecisionLower = jsonObject.get(type.getValue());
                if (PrecisionLower != null) {
                    if (PrecisionLower instanceof Integer && (int)PrecisionLower >= 0){}else throw new SDKException(ErrorCode.InvalidPrecision);
                }else throw new SDKException(ErrorCode.PrecisionNotNull);
                break;
        }
    }

}
