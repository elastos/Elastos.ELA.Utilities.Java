package org.elastos.ela;

import org.elastos.ela.bitcoinj.Sha256Hash;
import org.elastos.ela.bitcoinj.Utils;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by nan on 18/1/10.
 */
class Tx {
    byte TxType;
    byte PayloadVersion;
    //Payload        Payload
    TxAttribute[] Attributes;
    UTXOTxInput[] UTXOInputs;
    //BalanceInputs  []*BalanceTxInput
    TxOutput[] Outputs;
    long LockTime; //uint32
    List<Program> Programs;

    long Fee;
    long FeePerKB;
    byte[] hash;    //256byte
    Map<String,String> hashMapPriv = new HashMap<String,String>();

    public void sign(int index,String privateKey,byte[] code) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.SerializeUnsigned(dos);

        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        this.Programs.add(new Program(code,signature));

        return;
    }


    public static Tx  NewTransferAssetTransaction(UTXOTxInput[] inputs, TxOutput[] outputs) {

        Tx tx = new Tx();
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = 0x02;
        tx.Attributes = new TxAttribute[1];
        tx.Programs = new ArrayList<Program>();

        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs

        return tx;
    }


    //Serialize the Transaction
    public void Serialize(DataOutputStream o) throws IOException {
        SerializeUnsigned(o);

        //Serialize  Transaction's programs
        Util.WriteVarUint(o,Programs.size());


        if (Programs.size() > 0) {
            for (Program p : this.Programs) {
                p.Serialize(o);
            }
        }
        return;
    }


    public byte[][] getUniqAndOrdedProgramHashes() {


        String[] keys = (String[])hashMapPriv.keySet().toArray(new String[0]);

        byte[][] rhashes = new byte[keys.length][];
        for(int i=0;i<keys.length;i++){
            rhashes[i] = DatatypeConverter.parseHexBinary(keys[i]);
        }

        Util.sortByteArrayArrayUseRevertBytesSequence(rhashes);


        return rhashes;
    }



    public void SetPrograms(List<Program> programs) {
        this.Programs = programs;
    }

    public List<Program> GetPrograms() {

        return this.Programs;
    }

    //Serialize the Transaction data without contracts
    public void SerializeUnsigned(DataOutputStream o) throws IOException {
        //txType
        //w.Write([]byte{byte(tx.TxType)})
        o.writeByte(this.TxType);

        //PayloadVersion
        //w.Write([]byte{tx.PayloadVersion})
        o.writeByte(this.PayloadVersion);

        //Payload

        //[]*txAttribute
        Util.WriteVarUint(o, this.Attributes.length);

        if (this.Attributes.length > 0) {
            for (TxAttribute arr : this.Attributes) {
                arr.Serialize(o);
            }
        }
        //[]*UTXOInputs
        Util.WriteVarUint(o,this.UTXOInputs.length);
        if (this.UTXOInputs.length > 0) {
            for  (UTXOTxInput utxo : this.UTXOInputs) {
                utxo.Serialize(o);
            }
        }

        //[]*Outputs
        Util.WriteVarUint(o,this.Outputs.length);
        if (this.Outputs.length > 0) {
            for (TxOutput output : this.Outputs) {
                output.Serialize(o);
            }
        }

        o.writeInt(Integer.reverseBytes((int)this.LockTime));


        return;
    }

    public byte[] getHash() throws IOException {
        if(this.hash == null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            this.SerializeUnsigned(dos);
            byte[] txUnsigned = baos.toByteArray();
            Sha256Hash sh = Sha256Hash.twiceOf(txUnsigned);
            this.hash = sh.getReversedBytes();
        }
        return this.hash;
    }
    
    public byte[] GetMessage() {
        return new byte[0];
    }
}

