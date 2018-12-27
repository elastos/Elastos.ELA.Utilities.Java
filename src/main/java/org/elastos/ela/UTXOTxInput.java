package org.elastos.ela;

import org.elastos.common.Util;
import org.elastos.ela.bitcoinj.Utils;

import javax.xml.bind.DatatypeConverter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nan on 18/1/10.
 */
public class UTXOTxInput {
    public byte[] getReferTxID() {
        return ReferTxID;
    }

    public int getReferTxOutputIndex() {
        return ReferTxOutputIndex;
    }

    public int getSequence() {
        return Sequence;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getProgramHash() {
        return programHash;
    }

    //Indicate the previous Tx which include the UTXO output for usage
    private byte[] ReferTxID; //32byte unit256

    //The index of output in the referTx output list
    private int ReferTxOutputIndex; //uint16

    // Sequence number
    private int Sequence =0;


    private String privateKey;

    //String programHash;


    private String programHash; //21byte

    /**
     *
     * @param txid  utxo引用的交易id
     * @param index utxo在引用交易中的输出索引
     * @param privateKey utxo所属账户的私钥
     * @param address utxo所属账户的地址
     */
    public UTXOTxInput(String txid, int index, String privateKey, String address){
        this.ReferTxID = Utils.reverseBytes(DatatypeConverter.parseHexBinary(txid));
        this.privateKey = privateKey;
        this.ReferTxOutputIndex = index;
        this.programHash = DatatypeConverter.printHexBinary(Util.ToScriptHash(address));
    }

    public void serialize(DataOutputStream o) throws IOException {
        o.write(this.ReferTxID);
        o.writeShort(Short.reverseBytes((short)this.ReferTxOutputIndex));
        o.writeInt(Integer.reverseBytes(this.Sequence));
    }

    public static Map deSerialize(DataInputStream o) throws IOException {
        //ReferTxID
        byte[] buf = new byte[32];
        o.read(buf,0,32);
        String Txid = DatatypeConverter.printHexBinary(Utils.reverseBytes(buf));

        Map<String, Object> inputMap = new HashMap<String, Object>();
        inputMap.put("txid:",Txid);

        //ReferTxOutputIndex
        Short.reverseBytes(o.readShort());
        //Sequence
        Integer.reverseBytes(o.readInt());

        return inputMap;
    }
}
