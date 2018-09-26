package org.elastos.common;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

public class ErrorCode {
    public static String getError(int code , String msg){
        HashMap map = new HashMap();
        map.put("Code",code);
        map.put("Desc","Error");
        map.put("Result",msg);
        return JSON.toJSONString(map);
    }

    //account error
    public static String  PrikeyLengthError    = getError(51001, "encryptedPrivateKey length error");
    public static String  SaltLengthError      = getError(51002, "salt n dkLen length error");
    public static String  EncryptedPrivateKeyAddressPasswordErr = getError(51015, "encryptedPrivateKey address password not match.");
    public static String  EncriptPrivateKeyError = getError(51003, "encript privatekey error,");
    public static String  PasswordNotNull      = getError(51004, "password can not be empty");
    public static String  InvalidPassword      = getError(51005, "invalid Password");
    public static String  InvalidPrpvateKey    = getError(51006, "invalid privateKey");
    public static String  PrivateKeyNotNull    = getError(51007, "private key can not be empty");
    public static String  InvalidAddress       = getError(51008, "invalid address");
    public static String  AddressNotNull       = getError(51009, "address can not be empty");


    public static String  InvalidTxid          = getError(51010, "invalid txid");
    public static String  TxidnotNull          = getError(51011, "txid can not be empty");
    public static String  InvalidIndex         = getError(51012, "invalid index");
    public static String  IndexNotNull         = getError(51013, "index can not be empty");
    public static String  InvalidChangeAddress = getError(51014, "invalid changeAddress");
    public static String  ChangeAddressNotNull = getError(51015, "changeAddress can not be empty");
    public static String  InvalidAmount        = getError(51016, "invalid amount");
    public static String  AmountNotNull        = getError(51017, "amount can not be empty");
    public static String  KeystoreExist        = getError(51018, "keystore file exist");
    public static String  KeystoreNotExist     = getError(51019, "keystore file not exist");
    public static String  AccountExist         = getError(51020, "account exist");
    public static String  AccountNotExist      = getError(51021, "account not exist");
    public static String  InvalidRecordType    = getError(51022, "recordType cannot be Chinese");
    public static String  RecordTypeNotNull    = getError(51023, "recordType can not be empty");
    public static String  InvalidRecordData    = getError(51024, "recordData cannot be Chinese");
    public static String  RecordDataNotNull    = getError(51025, "recordData can not be empty");
    public static String  InvalidBlockHash     = getError(51026, "invalid blockHash");
    public static String  BlockHashNotNull     = getError(51027, "blockHash key can not be empty");
    public static String  InvalidM             = getError(51016, "invalid M");
    public static String  MNotNull             = getError(51017, "M can not be empty");

    // java-config.json
    public static String  HostNotNull          = getError(52001, "host can not be empty");
    public static String  InvalFee             = getError(52002, "invalid fee");
    public static String  FeeNotNull           = getError(52003, "fee can not be empty");
    public static String  InvalConfirmation    = getError(52004, "invalid confirmation");
    public static String  ConfirmationNotNull  = getError(52005, "confirmation can not be empty");


    //exception error
    public static String ParamErr(String msg) {
        return getError(53001, msg);
    }
}
