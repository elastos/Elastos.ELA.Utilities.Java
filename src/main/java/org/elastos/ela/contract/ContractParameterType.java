package org.elastos.ela.contract;

import java.util.HashMap;

public class ContractParameterType {
    public static HashMap<String,Byte> ContractParameterTypemap(){
        HashMap<String, Byte> parameterTypeMap = new HashMap<>();
        parameterTypeMap.put("Signature",(byte)0x00);
        parameterTypeMap.put("Boolean",(byte)0x01);
        parameterTypeMap.put("Integer",(byte)0x02);
        parameterTypeMap.put("Hash160",(byte)0x03);
        parameterTypeMap.put("Hash256",(byte)0x04);
        parameterTypeMap.put("ByteArray",(byte)0x05);
        parameterTypeMap.put("PublicKey",(byte)0x06);
        parameterTypeMap.put("String",(byte)0x07);
        parameterTypeMap.put("Object",(byte)0x08);
        parameterTypeMap.put("Hash168",(byte)0x09);
        parameterTypeMap.put("Array",(byte)0x10);
        parameterTypeMap.put("Void",(byte)0x11);

        return parameterTypeMap;
    }
}
