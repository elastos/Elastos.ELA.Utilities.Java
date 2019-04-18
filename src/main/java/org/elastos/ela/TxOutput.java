package org.elastos.ela;

import org.elastos.common.Common;
import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.common.Util;
import org.elastos.ela.bitcoinj.Utils;
import org.elastos.ela.outputpayload.VoteOutput;

import static org.elastos.ela.payload.PayloadRegisterAsset.ElaPrecision;

import javax.xml.bind.DatatypeConverter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by nan on 18/1/10.
 */
public class TxOutput {
    private byte[] SystemAssetID; //32 byte unit256
    private byte[] TokenAssetID; //32 byte unit256
    private long Value; //Fixed64
    private BigInteger TokenValue;
    private int OutputLock = 0; //uint32
    private byte[] ProgramHash; //21byte unit168
    private String Address;
    private byte OutputType;
    private VoteOutput OutputPayload;

    //transaciton version
    public static byte DEFAULT_OUTPUT = 0x00;
    public static byte VOTE_OUTPUT = 0x01;

    public TxOutput(String address, String amount, String assetId, int precision) {
        Output(address, amount, assetId, precision);
        this.OutputType = DEFAULT_OUTPUT;
    }

    public TxOutput(String address, String amount, String assetId, int precision, VoteOutput outputPayload, byte outputType) {
        Output(address, amount, assetId, precision);
        this.OutputType = outputType;
        this.OutputPayload = outputPayload;
    }

    private void Output(String address, String amount, String assetId, int precision) {
        this.Address = address;
        if (assetId.toLowerCase().equals(Common.SYSTEM_ASSET_ID)) {
            this.SystemAssetID = Common.ELA_ASSETID;
            this.Value = Util.multiplyAmountELA(new BigDecimal(amount), ElaPrecision).toBigInteger().longValue();
        } else {
            this.TokenAssetID = Utils.reverseBytes(DatatypeConverter.parseHexBinary(assetId));
            this.TokenValue = Util.multiplyAmountETH(new BigDecimal(amount), precision).toBigIntegerExact();
        }

        //programHash
        String DESTROY_ADDRESS = "0000000000000000000000000000000000";
        if (address.equals(DESTROY_ADDRESS)) {
            this.ProgramHash = new byte[21];
        } else {
            this.ProgramHash = Util.ToScriptHash(address);
        }
    }

    void Serialize(DataOutputStream o) throws SDKException {
        try {
            if (this.SystemAssetID != null) {
                o.write(this.SystemAssetID);
                o.writeLong(Long.reverseBytes(this.Value));
            } else if (this.TokenAssetID != null) {
                o.write(this.TokenAssetID);
                //因为TokenValue始终为正整数，所以取消bigInter第一个字节（补码）
                byte[] toPositiveBigInteger = Util.BigIntegerToPositiveBigInteger(this.TokenValue);
                Util.WriteVarBytes(o, toPositiveBigInteger);
            }
            o.writeInt(Integer.reverseBytes(this.OutputLock));
            o.write(this.ProgramHash);

            if (this.OutputType >= VOTE_OUTPUT) {
                o.write(this.OutputType);
                this.OutputPayload.Serialize(o);
            } else if (this.OutputType == DEFAULT_OUTPUT) {
                o.write(this.OutputType);
            }
        } catch (Exception e) {
            throw new SDKException(ErrorCode.ParamErr("TxOutput serialize exception :" + e));
        }
    }

    public static Map DeSerialize(DataInputStream o) throws IOException {
        // AssetID
        byte[] buf = new byte[32];
        o.read(buf, 0, 32);
        DatatypeConverter.printHexBinary(Utils.reverseBytes(buf));

        // Value
        long value = o.readLong();
        long v = Long.reverseBytes(value);

        // OutputLock
        long outputLock = o.readInt();
        Long.reverseBytes(outputLock);

        // ProgramHash
        byte[] program = new byte[21];
        o.read(program, 0, 21);
        byte[] programHash = program;
        String address = Util.ToAddress(programHash);

        Map<String, Object> outputMap = new LinkedHashMap<String, Object>();
        outputMap.put("address:", address);
        outputMap.put("amount:", v);
        return outputMap;
    }

    public byte[] getSystemAssetID() {
        return this.SystemAssetID;
    }

    public long getValue() {
        return this.Value;
    }

    public BigInteger getTokenValue() {
        return this.TokenValue;
    }

    public byte[] getTokenAssetID() {
        return this.TokenAssetID;
    }

    public long getOutputLock() {
        return this.OutputLock;
    }

    public byte[] getProgramHash() {
        return this.ProgramHash;
    }

    public String getAddress() {
        return this.Address;
    }
}
