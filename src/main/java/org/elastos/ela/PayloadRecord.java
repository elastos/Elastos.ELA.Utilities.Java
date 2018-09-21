package org.elastos.ela;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.elastos.ela.Util.WriteVarUint;
public class PayloadRecord {
    String RecordType;
    byte[] RecordData;

    public void Serialize(DataOutputStream o) throws IOException {
        WriteVarUint(o, this.RecordType.length());
        o.write(this.RecordType.getBytes());
        Util.WriteVarBytes(o,this.RecordData);
    }

    public PayloadRecord(String type, String data){
        this.RecordType = type;
        this.RecordData = data.getBytes();
    }
}
