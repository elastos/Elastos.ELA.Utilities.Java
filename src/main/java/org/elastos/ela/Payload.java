package org.elastos.ela;

import java.io.DataOutputStream;
import java.io.IOException;

public class Payload {
    String RecordType;
    byte[] RecorcData;

    public void Serialize(DataOutputStream o) throws IOException {
    o.writeChars(this.RecordType);
    Util.WriteVarBytes(o,this.RecorcData);
    }

    public Payload(String type, String data){
        this.RecordType = type;
        this.RecorcData = data.getBytes();
    }
}
