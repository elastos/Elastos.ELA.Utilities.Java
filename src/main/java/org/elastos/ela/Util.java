package org.elastos.ela;

import org.elastos.common.ErrorCode;
import org.elastos.common.SDKException;
import org.elastos.ela.bitcoinj.Base58;
import org.elastos.ela.bitcoinj.Sha256Hash;
import org.elastos.ela.bitcoinj.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by nan on 18/1/11.
 * TODO:
 */
public class Util {
    public static void WriteVarUint(DataOutputStream writer, long value) throws IOException {
        byte[] buf =new byte[9];
        if (value < 0xFD) {
            writer.writeByte((byte)value);
        } else if (value <= 0xFFFF) {
            writer.writeByte(0xFD);
            short s = FormatTransfer.reverseShort((short)value);
            writer.writeShort(s);
        } else if (value <= 0xFFFFFFFFL) {
            writer.writeByte(0xFE);
            int n = Integer.reverseBytes((int)value);
            writer.writeInt(n);
        } else {
            writer.writeByte(0xFF);
            long l = Long.reverseBytes(value);
            writer.writeLong(l);
        }
        return ;
    }
    public static void WriteVarBytes(DataOutputStream writer,byte[] value) throws IOException {
        WriteVarUint(writer,value.length);
        writer.write(value);
    }


    public static long ReadVarUint(DataInputStream read) throws IOException {
        byte n = read.readByte();
        if ((n & 0xFF) < 0xFD) {
            return n & 0xFF;
        } if ((n & 0xFF) == 0xFD) {
            short shortNumber = read.readShort();
            short number = FormatTransfer.reverseShort(shortNumber);
            return number;
        }else if ((n & 0xFF) == 0xFE){
            int intNumber = read.readInt();
            int number = Integer.reverseBytes(intNumber);
            return number;
        }else if ((n & 0xFF) == 0xFF){
            long longNumber = read.readLong();
            long number = Long.reverseBytes(longNumber);
            return number;
        }
        return 0;
    }

    /**
     * 地址到 公钥/脚本 哈希 转换 可逆(ToAddress)
     * @param address
     * @return program hash 21byte
     */
    public static byte[]  ToScriptHash(String address ){

        byte[] decoded = Base58.decodeChecked(address);

        BigInteger bi = new BigInteger(decoded);
        byte[] ph = new byte[21];
        System.arraycopy(bi.toByteArray(),0,ph,0,21);

        return ph;
    }

    /**
     * 检查地址合法性
     * @param address
     * @return
     */
    public static boolean checkAddress(String address){
        try {
            byte[] sh = ToScriptHash(address);
            if(sh[0]!=33&&sh[0]!=18){
                return false;
            }
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 公钥/脚本合约 到 公钥/脚本合约 哈希 转换 单向
     * @param code
     * @param signType
     * @return
     */
    public static byte[] ToCodeHash(byte[] code, int signType) {

        byte[] f = Utils.sha256hash160(code);
        byte[] g = new byte[f.length+1];
        // 1 是单签
        if (signType == 1) {
            g[0] = 33;
            System.arraycopy(f,0,g,1,f.length);
        // 2 是多签
        } else if (signType == 2) {
            g[0] = 18;
        // 3 身份id
        } else if (signType == 3){
            g[0] = 103;
        }else return null;
        System.arraycopy(f,0,g,1,f.length);
        return g;

    }

    /**
     * 公钥/脚本 哈希 到地址转换 可逆（ToScriptHash)
     * @param programHash
     * @return
     */
    public static String ToAddress(byte[] programHash){
        byte[] f = Sha256Hash.hashTwice(programHash);
        byte[] g = new byte[programHash.length+4];
        System.arraycopy(programHash,0,g,0,programHash.length);
        System.arraycopy(f,0,g,programHash.length,4);

        //BigInteger bi = new BigInteger(g);

        return Base58.encode(g);
    }

    public static byte[] CreateSingleSignatureRedeemScript(byte[] pubkey, int signType) {
        byte[] script = new byte[35];
        script[0] = 33;
        System.arraycopy(pubkey,0,script,1,33);
        // 1 单签
        if (signType == 1){
            script[34] = (byte)0xAC;
        // 3 身份id
        }else if (signType == 3){
            script[34] = (byte)0xAD;
        }
        return script;
    }
    public static void sortByteArrayArrayUseRevertBytesSequence(byte[][] hashes) {
        Arrays.sort(hashes,new Comparator(){

            public int compare(Object o1, Object o2) {
                int ret;
                byte[] ba1 = (byte[])o1;
                byte[] ba2 = (byte[])o2;
                for(int i=ba1.length-1;i>=0;i--){
                    ret = (ba1[i]&0xff) - (ba2[i]&0xff);
                    if(ret !=0 ) return ret;
                }
                return 0;
            }
        });
    }

    public static long IntByString(String value){
        String[] split = value.split("\\.");
        if (split.length == 2){
            long front = (long) Integer.parseInt(split[0]) * 100000000;

            String after = split[1];
            if (after.length() == 8){
                long afterInt = (long)Integer.parseInt(after);
                long Value = front + afterInt;
                return Value;

            }else{
                after = after + '0' * (8 - after.length());
                long afterInt = (long)Integer.parseInt(after);
                long Value = front + afterInt;
                return Value;
            }
        }else {
            long Value = (long) Integer.parseInt(value) * 100000000;
            return Value;
        }
    }

}

