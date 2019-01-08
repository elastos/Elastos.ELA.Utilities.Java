package org.elastos.ela.payload;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.elastos.common.Util.WriteVarUint;
public class PayloadRecord {
    String RecordType;
    byte[] RecordData;

    public void Serialize(DataOutputStream o) throws SDKException {
        try {
            WriteVarUint(o, this.RecordType.length());
            o.write(this.RecordType.getBytes());
            Util.WriteVarBytes(o,this.RecordData);
        }catch (Exception e){
            throw new SDKException(ErrorCode.ParamErr("PayloadRecord serialize exception :" + e));
        }
    }

    public PayloadRecord(String type, String data){
        this.RecordType = type;
        this.RecordData = data.getBytes();
    }
}
