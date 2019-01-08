package org.elastos.ela;

import org.elastos.common.Util;
import org.elastos.ela.bitcoinj.Sha256Hash;
import org.elastos.ela.payload.*;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by nan on 18/1/10.
 */
public class Tx {
    private byte TxType;
    private byte PayloadVersion;
    private TxAttribute[] Attributes;
    private UTXOTxInput[] UTXOInputs;
    private TxOutput[] Outputs;
    private int LockTime; //uint32
    private List<Program> Programs;
    private byte[] hash;    //256byte

    private PayloadRecord parloadRecord;
    private PayloadRegisterAsset payloadRegisterAsset;
    private PayloadTransferCrossChainAsset[] CrossChainAsset;
    private PayloadDeploy payloadDeploy;
    private PayloadInvoke payloadInvoke;

    //Transction types
    final static byte COIN_BASE = 0x00;
    final static byte REGISTER_ASSET = 0x01;
    final static byte TRANSFER_ASSET = 0x02;
    final static byte RECORD = 0x03;
    final static byte Deploy = 0x04;
    final static byte SIDECHAINPOW = 0x05;
    final static byte RECHARGE_TO_SIDE_CHAIN = 0x06;
    final static byte WITHDRAW_FROM_SIDE_CHAIN = 0x07;
    final static byte TRANSFER_CROSS_CHAIN_ASSET = 0x08;
    final static byte INVOKE = 0x09;


    Map<String,String> hashMapPriv = new HashMap<String,String>();

    // sigin sign
    public void sign(String privateKey,byte[] code) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.serializeUnsigned(dos);
        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        this.Programs.add(new Program(code,signature));
    }

    // multi sign
    public void multiSign(String privateKey,byte[] code) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        this.serializeUnsigned(dos);

        byte[] signature = SignTool.doSign(baos.toByteArray(), DatatypeConverter.parseHexBinary(privateKey));
        Program  program =  new Program(code,signature);
        if (this.Programs.size() == 0){
            this.Programs.add(program);
        }else {
            //合并signature
            byte[] byte_buf = new byte[this.Programs.get(0).Parameter.length + program.Parameter.length];
            System.arraycopy(this.Programs.get(0).Parameter,0,byte_buf,0,this.Programs.get(0).Parameter.length);
            System.arraycopy(program.Parameter,0,byte_buf,this.Programs.get(0).Parameter.length,program.Parameter.length);

            this.Programs.get(0).Parameter = byte_buf;
        }
    }

    //Ordinary transaction
    public static Tx newTransferAssetTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs) {
        Tx tx = new Tx();
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    //Ordinary transaction memo
    public static Tx newTransferAssetTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs , String memo) {
        Tx tx = new Tx();
        commonalityTransaction(tx,TransactionType,inputs,outputs,memo);
        return tx;
    }

    // RECORD Transaction
    public static Tx recordTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRecord parloadRecord) {
        Tx tx = new Tx();
        tx.parloadRecord = parloadRecord;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // Register Aseet
    public static Tx registerAssetTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs, PayloadRegisterAsset payloadRegisterAsset) {
        Tx tx = new Tx();
        tx.payloadRegisterAsset = payloadRegisterAsset;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // CROSS_CHAIN Transaction
    public static Tx crossChainTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs , PayloadTransferCrossChainAsset[] CrossChainAsset) {
        Tx tx = new Tx();
        tx.CrossChainAsset = CrossChainAsset;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // DeployContract Transaction
    public static Tx deployContractTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs, PayloadDeploy payloadDeploy) {
        Tx tx = new Tx();
        tx.payloadDeploy = payloadDeploy;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // InvokeContract Transaction
    public static Tx invokeContractTransaction(byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs, PayloadInvoke payloadInvoke) {
        Tx tx = new Tx();
        tx.payloadInvoke = payloadInvoke;
        commonalityTransaction(tx,TransactionType,inputs,outputs);
        return tx;
    }

    // commonality transaction parameter
    private static void commonalityTransaction(Tx tx, byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs){
        commonalityTx(tx,TransactionType,inputs,outputs);

        tx.Attributes = new TxAttribute[1];
        TxAttribute ta = TxAttribute.NewTxNonceAttribute();
        tx.Attributes[0] = ta;
    }

    private static void commonalityTransaction(Tx tx, byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs, String memo){
        commonalityTx(tx,TransactionType,inputs,outputs);

        tx.Attributes = new TxAttribute[1];
        TxAttribute ta = TxAttribute.NewTxNonceAttribute(memo);
        tx.Attributes[0] = ta;
    }

    private static void commonalityTx(Tx tx, byte TransactionType, UTXOTxInput[] inputs, TxOutput[] outputs){
        tx.UTXOInputs = inputs;
        tx.Outputs = outputs;
        tx.TxType = TransactionType;
        tx.Programs = new ArrayList<Program>();

        for(UTXOTxInput txin : tx.UTXOInputs){

            tx.hashMapPriv.put(txin.getProgramHash(),txin.getPrivateKey());
        }
        //使用私钥构造出公钥,通过公钥构造出contract,通过contract构造出programhash,写入到 UTXOInputs
    }

    //serialize the Transaction
    public void serialize(DataOutputStream o) throws Exception {
        serializeUnsigned(o);

        //serialize  Transaction's programs
        Util.WriteVarUint(o,Programs.size());


        if (Programs.size() > 0) {
            for (Program p : this.Programs) {
                p.Serialize(o);
            }
        }
    }

    //serialize the Transaction data without contracts
    public void serializeUnsigned(DataOutputStream o) throws Exception{
        //txType
        o.writeByte(this.TxType);

        //PayloadVersion
        o.writeByte(this.PayloadVersion);

        //Payload
        getPayload(o);

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
                utxo.serialize(o);
            }
        }
        //[]*Outputs
        Util.WriteVarUint(o,this.Outputs.length);
        if (this.Outputs.length > 0) {
            for (TxOutput output : this.Outputs) {
                output.Serialize(o);
            }
        }

        o.writeInt(Integer.reverseBytes(this.LockTime));
    }

    public static Map deserialize(DataInputStream o) throws IOException {
        return deSerializeUnsigned(o);
    }

    public static Map deSerializeUnsigned(DataInputStream o) throws IOException {
        //txType
        byte TxType = o.readByte();

        //PayloadVersion
        byte PayloadVersion = o.readByte();

        //[]*txAttribute
        long len = 0;
        len = Util.ReadVarUint(o);
        TxAttribute.DeSerialize(o);

        //[]*UTXOInputs
        len =  Util.ReadVarUint(o);
        List<Map>  inputList = new LinkedList<Map>();
        for (int i = 0 ; i < len ; i++){
            Map inputMap = UTXOTxInput.deSerialize(o);
            inputList.add(inputMap);
        }

        //[]*Outputs
        List<Map> outputList = new LinkedList<Map>();
        len =  Util.ReadVarUint(o);
        for (int i = 0 ; i < len ; i++){
            Map outputMap = TxOutput.DeSerialize(o);
            outputList.add(outputMap);
        }

        Map<String, List<Map>> resultmap = new LinkedHashMap<String, List<Map>>();
        resultmap.put("inputs:",inputList);
        resultmap.put("outputs:",outputList);
        return resultmap;
    }

    public byte[] getHash() throws Exception {
        if(this.hash == null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            this.serializeUnsigned(dos);
            byte[] txUnsigned = baos.toByteArray();
            Sha256Hash sh = Sha256Hash.twiceOf(txUnsigned);
            this.hash = sh.getReversedBytes();
        }
        return this.hash;
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

    private void getPayload(DataOutputStream o) throws Exception {
        switch (this.TxType){
            case COIN_BASE:
                break;
            case REGISTER_ASSET:
                this.payloadRegisterAsset.Serialize(o);
                break;
            case TRANSFER_ASSET:
                break;
            case RECORD:
                this.parloadRecord.Serialize(o);
                break;
            case Deploy:
                this.payloadDeploy.Serialize(o);
                break;
            case TRANSFER_CROSS_CHAIN_ASSET:
                Util.WriteVarUint(o, this.CrossChainAsset.length);
                for (PayloadTransferCrossChainAsset ca : this.CrossChainAsset)
                    ca.Serialize(o);
                break;
            case INVOKE:
                this.payloadInvoke.Serialize(o);
                break;
        }
    }
}

