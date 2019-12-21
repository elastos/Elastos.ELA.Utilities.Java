package org.elastos.ela;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elastos.api.Basic;
import org.elastos.api.ELATransaction;
import org.elastos.common.Common;
import org.elastos.common.Util;
import org.elastos.ela.bitcoinj.Utils;
import org.junit.Test;
import static org.elastos.ela.payload.PayloadRegisterAsset.ElaPrecision;
import javax.xml.bind.DatatypeConverter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by nan on 18/1/15.
 */
public class ElaTest {

    /**
     * 处理 ELA类的方法
     * @throws Exception
     */
    @Test
    public void  processMethod() throws Exception{
        String filePath   = "C:/DNA/src/ela_tool/src/test/java/org/elastos/ela/transaction";

        FileInputStream inputStream = new FileInputStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        JSONObject jsonObject = JSONObject.fromObject(bufferedReader.readLine());
        JSONArray jsonArray = jsonObject.getJSONArray("params");
        String method = jsonObject.getString("method");
        System.out.println("method :" + method);
        if (jsonArray.size() == 0) {
            if (method.equals("getPrivateKey") ) {
                System.out.println(Basic.genPrivateKey());
            }
            if (method.equals("get_priv_put_addr")) {
                System.out.println((Basic.gen_priv_pub_addr()));
            }
        }
        if (jsonArray.size() != 0){
            JSONObject param = (JSONObject)jsonArray.get(0);
            if (method.equals("getPublicKey")){
                System.out.println((Basic.genPublicKey(param)));
            }
            if (method.equals("getAddress")){
                System.out.println((Basic.genAddress(param)));
            }
            if (method.equals("getRawTransaction")){
                System.out.println(ELATransaction.genRawTx(param));
            }
        }
        inputStream.close();
        bufferedReader.close();
    }

    /**
     * 签名
     * @throws Exception
     */
    @Test
    public void makeAndSignTx() throws Exception {
        List<UTXOTxInput> inputs = new LinkedList<UTXOTxInput>();
        inputs.add(new UTXOTxInput(
                "cd96ca8f7bf76b25dbb90e2edd33bb6f7df95fa804f4af89d177ff72d8fabf0e",
                1,      //矿工为1，转账为0
                "5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB",
                "EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB"));
//        inputs.add(new UTXOTxInput(
//                "cd96ca8f7bf76b25dbb90e2edd33bb6f7df95fa804f4af89d177ff72d8fabf0e",
//                1,
//                "A65E9FB6735C5FD33F839036B15D2DA373E15AED38054B69386E322C6BE52994",
//                "EgSph8GNaNSMwpv6UseAihsAc5sqSrA7ga"));
//        inputs.add(new UTXOTxInput(
//                "22bade15481f1af8240993207e1df61144a7776e6087994d240917a887f72052",
//                0,
//                "4C573939323F11BCDB57B61CCE095D4B1E55E986F9944F88072141F3DFA883A3",
//                "Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1"));

        List<TxOutput>  outputs = new LinkedList<TxOutput>();
        outputs.add(new TxOutput("EXkNxfzanRVZumirTudiTBfvMyV2Rt6w95","0.00001", Common.SYSTEM_ASSET_ID,ElaPrecision));

        RawTx rawTx = Ela.makeAndSignTx(inputs.toArray(new UTXOTxInput[inputs.size()]),outputs.toArray(new TxOutput[outputs.size()]));
        System.out.println("rawTx:"+rawTx.getRawTxString());
        System.out.println("txHash:"+rawTx.getTxHash());
    }

    @Test
    public void revers(){
        byte[] hex = DatatypeConverter.parseHexBinary("972e8ee63b6054ff923f6b68480fb82d10a8907a231e68941348d9e789b39dc9");
        System.out.println(DatatypeConverter.printHexBinary(Utils.reverseBytes(hex)).toLowerCase());
    }

    /**
     * 生成私钥、公钥、地址 
     */
    @Test
    public void generateBasic(){

        //生成私钥
        String privateKey = Ela.getPrivateKey();
        System.out.println("Private key = " + privateKey);

        //生成公钥
        String publicKey = Ela.getPublicFromPrivate(privateKey);
        System.out.println("Public Key = " + publicKey);

        //生成地址
        String address = Ela.getAddressFromPrivate(privateKey);
        System.out.println("Address = " + address);
    }

    @Test
    public void decoderRawTransaction() throws IOException {
        String rawTxString = "020001022242566678696446644B61657463326654376A35785342684A595834457A557053514501D0021094539952F33A05245F4319E9B59843874FFBFB31FCF2A239337DEE6CC600000000000001B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A30070AE1993A70A000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178B00000000014140934D6679F3E5ECCDACD96AA6AD6771E37045E0F978B934DAD2E3A305542345E88BC1191AA1B93870575D2328E1AA4A89AADC0E577FEE5399C34837C6AA835CB02321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC";

        byte[] rawTxByte = DatatypeConverter.parseHexBinary(rawTxString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawTxByte);
        DataInputStream dos = new DataInputStream(byteArrayInputStream);
        Tx.deserialize(dos);
    }

    @Test
    public void test() throws IOException {
        String rawTxString = "020001022242566678696446644B61657463326654376A35785342684A595834457A557053514501D0021094539952F33A05245F4319E9B59843874FFBFB31FCF2A239337DEE6CC600000000000001B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A30070AE1993A70A000000000021C3B5C32D6FE7CAC86A855276D087C443FB12178B00000000014140934D6679F3E5ECCDACD96AA6AD6771E37045E0F978B934DAD2E3A305542345E88BC1191AA1B93870575D2328E1AA4A89AADC0E577FEE5399C34837C6AA835CB02321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC";

        byte[] rawTxByte = DatatypeConverter.parseHexBinary(rawTxString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawTxByte);
        DataInputStream dos = new DataInputStream(byteArrayInputStream);

        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outs);

        int temp = 0;
        while ((temp = dos.read()) != -1){
            out.write(temp);
//            int len =  (int)Util.ReadVarUint(dos);
//            System.out.println("len:"+ len);
//            byte[] b = new  byte[len];
//            out.writeByte(dos.read(b,0,len));
        }


        String s = DatatypeConverter.printHexBinary(outs.toByteArray());
        System.out.println(s);
        System.out.println(rawTxString.equals(s));

    }

}