package org.elastos.ela.contract;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;

import java.io.DataOutputStream;

public class FunctionCode {
    private byte[] Code;
    private byte[] ParameterTypes;
    private byte ReturnType;
    private String codeHash; //Uint168


    public  FunctionCode(byte returnType, byte[] parameterTypes,byte[] code){
        this.ReturnType = returnType;
        this.ParameterTypes = parameterTypes;
        this.Code = code;
    }

    public void Serialize(DataOutputStream o) throws SDKException {
        try{
            Util.WriteVarBytes(o,this.Code);
            Util.WriteVarBytes(o,this.ParameterTypes);
            o.write(this.ReturnType);
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("FunctionCode serialize exception :" + e));
        }
    }
}
