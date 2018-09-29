package org.elastos.ela;

import org.elastos.ela.bitcoinj.Utils;

import javax.xml.bind.DatatypeConverter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by nan on 18/1/10.
 */
public class TxOutput {
    private byte[] AssetID= Common.ELA_ASSETID; //32 byte unit256
    private long Value; //Fixed64
    private long OutputLock = 0; //uint32
    private byte[] ProgramHash; //21byte unit168
    private String Address;
    private final String DESTROY_ADDRESS = "0000000000000000000000000000000000";

    /**
     *
     * @param address 地址
     * @param amount 金额
     */
    public TxOutput(String address,long amount){
        this.Address = address;
        this.Value = amount;
        if (address.equals(DESTROY_ADDRESS)){
            this.ProgramHash = new byte[21];
        }else {
            this.ProgramHash = Util.ToScriptHash(address);
        }
    }

    void Serialize(DataOutputStream o) throws IOException {
        o.write(this.AssetID);
        o.writeLong(Long.reverseBytes(this.Value));
        o.writeInt(Integer.reverseBytes((int)this.OutputLock));
        o.write(this.ProgramHash);

    }

    public static Map DeSerialize(DataInputStream o) throws IOException {
        // AssetID
        byte[] buf = new byte[32];
        o.read(buf,0,32);
        DatatypeConverter.printHexBinary(Utils.reverseBytes(buf));

        // Value
        long value =  o.readLong();
        long v = Long.reverseBytes(value);

        // OutputLock
        long outputLock =  o.readInt();
        Long.reverseBytes(outputLock);

        // ProgramHash
        byte[] program = new byte[21];
        o.read(program,0,21);
        byte[] programHash = program;
        String address = Util.ToAddress(programHash);

        Map<String, Object> outputMap = new LinkedHashMap<String, Object>();
        outputMap.put("Address:",address);
        outputMap.put("Value:",v);
        return outputMap;
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
