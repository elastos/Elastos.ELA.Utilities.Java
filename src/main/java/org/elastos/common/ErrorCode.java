package org.elastos.common;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

public class ErrorCode {
    public static String getError(int code, String msg) {
        HashMap map = new HashMap();
        map.put("Code", code);
        map.put("Desc", "Error");
        map.put("Result", msg);
        return JSON.toJSONString(map);
    }

    //account error
    public static String PRIKEY_LENGTH_ERROR = getError(51001, "encryptedPrivateKey length error");
    public static String SALT_LENGTHE_RROR = getError(51002, "salt n dkLen length error");
    public static String ENCRYPTED_PRIVATE_KEY_ADDRESS_PASSWORD_ERR = getError(51015, "encryptedPrivateKey address password not match");
    public static String ENCRIPT_PRIVATE_KEY_ERROR = getError(51003, "encript privatekey error,");
    public static String PASSWORD_NOT_NULL = getError(51004, "password can not be empty");
    public static String INVALID_PASSWORD = getError(51005, "invalid Password");
    public static String INVALID_PRPVATE_KEY = getError(51006, "invalid privateKey");
    public static String PRIVATE_KEY_NOT_NULL = getError(51007, "private key can not be empty");
    public static String INVALID_ADDRESS = getError(51008, "invalid address");
    public static String ADDRESS_NOT_NULL = getError(51009, "address can not be empty");


    public static String INVALID_TX_ID = getError(51010, "invalid txid");
    public static String TX_ID_NOT_NULL = getError(51011, "txid can not be empty");
    public static String INVALID_INDEX = getError(51012, "invalid index");
    public static String INDEX_NOT_NULL = getError(51013, "index can not be empty");
    public static String INVALID_CHANGE_ADDRESS = getError(51014, "invalid changeAddress");
    public static String CHANGE_ADDRESS_NOT_NULL = getError(51015, "changeAddress can not be empty");
    public static String INVALID_AMOUNT = getError(51016, "amount requires long type");
    public static String AMOUNT_NOT_NULL = getError(51017, "amount can not be empty");
    public static String KEY_STORE_EXIST = getError(51018, "keystore file exist");
    public static String KEY_STORE_NOT_EXIST = getError(51019, "keystore file not exist");
    public static String ACCOUNT_EXIST = getError(51020, "account exist");
    public static String ACCOUNT_NOT_EXIST = getError(51021, "account not exist");
    public static String INVALID_RECORD_TYPE = getError(51022, "recordType cannot be Chinese");
    public static String RECORD_TYPE_NOT_NULL = getError(51023, "recordType can not be empty");
    public static String INVALID_RECORD_DATA = getError(51024, "recordData cannot be Chinese");
    public static String RECORD_DATA_NOT_NULL = getError(51025, "recordData can not be empty");
    public static String INVALID_BLOCK_HASH = getError(51026, "invalid blockHash");
    public static String BLOCK_HASH_NOT_NULL = getError(51027, "blockHash key can not be empty");
    public static String INVALID_M = getError(51028, "invalid M");
    public static String M_NOT_NULL = getError(51029, "M can not be empty");

    public static String INVALID_ASSET_ID = getError(51030, "invalid assetId");
    public static String ASSET_ID_NOT_NULL = getError(51031, "assetId can not be empty");
    public static String INVALID_DESCRIPTION = getError(51032, "description can only be ASCII");
    public static String DESCRIPTION_NOT_NULL = getError(51033, "description can not be empty");
    public static String INVALID_NAME = getError(51034, "name can only be an english character and number");
    public static String NAME_NOT_NULL = getError(51035, "name can not be empty");
    public static String INVALID_PRECISION = getError(51036, "invalid precision");
    public static String PRECISION_NOT_NULL = getError(51037, "precision can not be empty");
    public static String INVALID_AMOUNT_STR = getError(51038, "amount requires string type");

    // java-config.json
    public static String HOST_NOT_NULL = getError(52001, "host can not be empty");
    public static String INVALID_FEE = getError(52002, "fee requires String type");
    public static String FEE_NOT_NULL = getError(52003, "fee can not be empty");
    public static String INVALID_CONFIRMATION = getError(52004, "invalid confirmation");
    public static String CONFIRMATION_NOT_NULL = getError(52005, "confirmation can not be empty");
    public static String REGISTERASSETFEE_NOT_NULL = getError(51038, "registerAssetFee can not be empty");
    public static String INVALID_REGISTERASSETFEE = getError(51038, "registerAssetFee requires String type");


    //exception error
    public static String ParamErr(String msg) {
        return getError(53001, msg);
    }
}
