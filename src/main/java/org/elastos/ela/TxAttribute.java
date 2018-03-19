package org.elastos.ela;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by nan on 18/1/10.
 */
public class TxAttribute {
    byte Usage;
    byte[] Data;
    int Size;

    public void Serialize(DataOutputStream o) throws IOException {
        o.writeByte(this.Usage);
        Util.WriteVarBytes(o,this.Data);

        return;
    }
    public static TxAttribute NewTxNonceAttribute()
    {
        Random r = new Random();
        TxAttribute ta = new TxAttribute();
        ta.Usage = 0x00;
        ta.Data = Long.toString(r.nextLong(),10).getBytes();
        ta.Size = 0;
        return ta;
    }


}
