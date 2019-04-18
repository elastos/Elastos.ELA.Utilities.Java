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
    public enum Type {
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

        Type(String t) {
            this.type = t;
        }

        public String getValue() {
            return type;
        }
    }


    public static void verifyParameter(Type type, JSONObject jsonObject) throws SDKException {
        switch (type) {
            case AssetId:
            case BlockHash:
            case PrivateKey:
                Object pr = jsonObject.get(type.getValue());
                if (pr != null) {
                    if (((String) pr).length() != 64) {
                        throw new SDKException(ErrorCode.invalidParam(type.getValue()));
                    }
                    try {
                        DatatypeConverter.parseHexBinary((String) pr);
                    } catch (Exception e) {
                        throw new SDKException(ErrorCode.invalidParam(type.getValue() + " " + e));
                    }
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case Txid:
                Object TxidLower = jsonObject.get(type.getValue());
                if (TxidLower != null) {
                    if (((String) TxidLower).length() != 64) {
                        throw new SDKException(ErrorCode.invalidParam(type.getValue()));
                    }
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case ChangeAddress:
            case Address:
                Object addr = jsonObject.get(type.getValue());
                if (addr != null) {
                    try {
                        byte[] sh = ToScriptHash((String) addr);
                        if (sh[0] != 33 && sh[0] != 18 && sh[0] != 28 && sh[0] != 31) {
                            throw new SDKException(ErrorCode.invalidParam(type.getValue()));
                        }
                    } catch (Exception e) {
                        throw new SDKException(ErrorCode.invalidParam(type.getValue() + " " + e));
                    }
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case Host:
                Object Host = jsonObject.get(type.getValue());
                if (Host == null) throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case Amount:
            case Precision:
            case Confirmation:
            case M:
            case Vout:
                Object in = jsonObject.get(type.getValue());
                if (in != null) {
                    if (in instanceof Long || in instanceof Integer && (int) in >= 0) {
                    } else throw new SDKException(ErrorCode.invalidParam(type.getValue() + " requires int type"));
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case RecordType:
                Object RecordTypeLower = jsonObject.get(type.getValue());
                if (RecordTypeLower != null) {
                    if (Util.isChinese((String) RecordTypeLower))
                        throw new SDKException(ErrorCode.invalidParam(type.getValue() + " cannot be Chinese"));
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case RecordData:
                Object RecordDataLower = jsonObject.get(type.getValue());
                if (RecordDataLower != null) {
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case Name:
                Object NameLower = jsonObject.get(type.getValue());
                if (NameLower != null) {
                    boolean isWord = ((String) NameLower).matches("[a-zA-Z0-9]+");
                    if (!isWord)
                        throw new SDKException(ErrorCode.invalidParam(type.getValue() + " can only be an english character and number"));
                } else throw new SDKException(type.getValue() + " can not be empty");
                break;

            case Description:
                Object DescriptionLower = jsonObject.get(type.getValue());
                if (DescriptionLower != null) {
                    if (!isAscii((String) DescriptionLower))
                        throw new SDKException(ErrorCode.invalidParam(type.getValue() + " description can only be ASCII"));
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case RpcConfiguration:
                Object RpcConfiguration = jsonObject.get(type.getValue());
                if (RpcConfiguration != null) {
                    if (RpcConfiguration instanceof JSONObject) {
                    } else
                        throw new SDKException(ErrorCode.invalidParam(type.getValue() + " requires JsonObject type"));
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;

            case RegisterAssetFee:
            case User:
            case Fee:
            case Password:
            case AmountStr:
            case Pass:
                Object str = jsonObject.get(type.getValue());
                if (str != null) {
                    if (str instanceof String) {
                    } else throw new SDKException(ErrorCode.invalidParam(type.getValue() + " requires String type"));
                } else throw new SDKException(ErrorCode.paramNotNull(type.getValue()));
                break;
        }
    }
}
