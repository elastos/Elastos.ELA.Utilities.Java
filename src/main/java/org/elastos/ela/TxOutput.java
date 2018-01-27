package org.elastos.ela;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by nan on 18/1/10.
 */
public class TxOutput {
    private byte[] AssetID= Common.ELA_ASSETID; //32 byte unit256
    private long Value; //Fixed64
    private long OutputLock = 0; //uint32
    private byte[] ProgramHash; //21byte unit168
    private String Address;


    /**
     *
     * @param Address 地址
     * @param amount 金额
     */
    public TxOutput(String Address,long amount){
        this.Address = Address;
        this.Value = amount;
        this.ProgramHash = Util.ToScriptHash(Address);
    }

    void Serialize(DataOutputStream o) throws IOException {
        o.write(this.AssetID);
        o.writeLong(Long.reverseBytes(this.Value));
        o.writeInt(Integer.reverseBytes((int)this.OutputLock));
        o.write(this.ProgramHash);

    }

    public byte[] getAssetID() {
        return AssetID;
    }

    public long getValue() {
        return Value;
    }

    public long getOutputLock() {
        return OutputLock;
    }

    public byte[] getProgramHash() {
        return ProgramHash;
    }

    public String getAddress() {
        return Address;
    }
}
