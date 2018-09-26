package org.elastos.ela;

import javax.xml.bind.DatatypeConverter;
import java.io.DataInputStream;
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

    public static void DeSerialize(DataInputStream o) throws IOException {
        // Usage
        o.readByte();

        //Data
        int len = (int)Util.ReadVarUint(o);
        byte[] b = new  byte[len];
        o.read(b,0,len);
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
    public static TxAttribute NewTxNonceAttribute(String memo)
    {
        Random r = new Random();
        TxAttribute ta = new TxAttribute();
        ta.Usage = (byte) 0x81;
        ta.Data = memo.getBytes();
        ta.Size = 0;
        return ta;
    }

}
