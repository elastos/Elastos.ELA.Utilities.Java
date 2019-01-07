package org.elastos.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;

public class ErrorCode {
    public static String getError(int code, String msg) {

        HashMap m = new HashMap();
        m.put("code",code);
        m.put("id",null);
        m.put("message",msg);

        HashMap map = new HashMap();
        map.put("error", m);
        map.put("jsonrpc", "2.0");
        map.put("result", null);
        return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
    }

    //account error
    public static String PRIKEY_LENGTH_ERROR = getError(51001, "encryptedPrivateKey length error");
    public static String SALT_LENGTHE_RROR = getError(51002, "salt n dkLen length error");
    public static String ENCRYPTED_PRIVATE_KEY_ADDRESS_PASSWORD_ERR = getError(51003, "encryptedPrivateKey address password not match");
    public static String ENCRIPT_PRIVATE_KEY_ERROR = getError(51004, "encript privatekey error,");
    public static String INVALID_PASSWORD = getError(51005, "invalid Password");
    public static String INVALID_PRPVATE_KEY = getError(51006, "invalid privateKey");
    public static String INVALID_ADDRESS = getError(51007, "invalid address");

    public static String INVALID_TX_ID = getError(51008, "invalid txid");
    public static String INVALID_VOUT = getError(51009, "invalid vout");
    public static String INVALID_CHANGE_ADDRESS = getError(51010, "invalid changeAddress");
    public static String INVALID_AMOUNT = getError(51011, "amount requires long type");
    public static String KEY_STORE_EXIST = getError(51012, "keystore file exist");
    public static String KEY_STORE_NOT_EXIST = getError(51013, "keystore file not exist");
    public static String ACCOUNT_EXIST = getError(51014, "account exist");
    public static String ACCOUNT_NOT_EXIST = getError(51015, "account not exist");
    public static String INVALID_RECORD_TYPE = getError(51016, "recordType cannot be Chinese");
    public static String INVALID_BLOCK_HASH = getError(51017, "invalid blockHash");
    public static String INVALID_M = getError(51018, "invalid m");

    public static String INVALID_ASSET_ID = getError(51019, "invalid assetId");
    public static String INVALID_DESCRIPTION = getError(51020, "description can only be ASCII");
    public static String INVALID_NAME = getError(51021, "name can only be an english character and number");
    public static String INVALID_PRECISION = getError(51022, "invalid precision");
    public static String INVALID_AMOUNT_STR = getError(51023, "amount requires string type");


    // java-config.json
    public static String INVALID_FEE = getError(52001, "Fee requires String type");
    public static String INVALID_CONFIRMATION = getError(52002, "invalid Confirmation");
    public static String INVALID_REGISTERASSETFEE = getError(51003, "RegisterAssetFee requires String type");
    public static String INVALID_RPCCONFIGURATION = getError(52004, "Fee requires jsonObject type");
    public static String INVALID_USER = getError(52005, "User requires String type");
    public static String INVALID_PASS = getError(52006, "Pass requires String type");

    //exception error
    public static String ParamErr(String msg) {
        return getError(53001, msg);
    }


}
