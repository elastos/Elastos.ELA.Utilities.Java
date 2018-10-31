package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.Util;

import javax.xml.bind.DatatypeConverter;
import java.io.DataOutputStream;

public class payloadInvoke {
    private String CodeHash; //Uint168
    private byte[] Code;
    private String ProgramHash; //Uint168
    private long Gas;//Fixed64


    public void Serialize (DataOutputStream o) throws SDKException {
        try {
            o.write(DatatypeConverter.parseBase64Binary(this.CodeHash));
            Util.WriteVarBytes(o,this.Code);
            o.write(DatatypeConverter.parseBase64Binary(this.ProgramHash));
            o.writeLong(Long.reverseBytes(this.Gas)); //TODO this is nesessary to reverse?
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("PayloadDeploy serialize exception :" + e));
        }
    }
}
