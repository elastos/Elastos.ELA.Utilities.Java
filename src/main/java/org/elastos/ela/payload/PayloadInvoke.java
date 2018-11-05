package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;

import javax.xml.bind.DatatypeConverter;
import java.io.DataOutputStream;

public class PayloadInvoke {
    private String CodeHash; //Uint168
    private byte[] Code;
    private String ProgramHash; //Uint168
    private long Gas;//Fixed64


    public PayloadInvoke(String codeHash,byte[] code, String programHash, long gas){
        this.CodeHash = codeHash;
        this.Code = code;
        this.ProgramHash = programHash;
        this.Gas = gas;
    }

    public void Serialize (DataOutputStream o) throws SDKException {
        try {
            o.write(DatatypeConverter.parseBase64Binary(this.CodeHash));
            Util.WriteVarBytes(o,this.Code);
            o.write(DatatypeConverter.parseBase64Binary(this.ProgramHash));
            o.writeLong(Long.reverseBytes(this.Gas));
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("PayloadInvoke serialize exception :" + e));
        }
    }
}
