package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

public class PayloadInvoke {
    private byte[] CodeHash; //Uint160，后续可能更改uint168
    private byte[] Code;
    private byte[] ProgramHash; //Uint168
    private long Gas;//Fixed64


    public PayloadInvoke(byte[] codeHash,byte[] code, byte[] programHash, long gas){
        this.CodeHash = codeHash;
        this.Code = code;
        this.ProgramHash = programHash;
        this.Gas = gas;
    }

    public void Serialize (DataOutputStream o) throws SDKException {
        try {
            Util.WriteVarBytes(o,this.CodeHash);
//            o.write(this.CodeHash);

            Util.WriteVarBytes(o,this.Code);

            o.write(this.ProgramHash);
            o.writeLong(Long.reverseBytes(this.Gas));
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("PayloadInvoke serialize exception :" + e));
        }
    }
}


