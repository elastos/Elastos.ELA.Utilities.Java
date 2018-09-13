package org.elastos.wallet;

import com.alibaba.fastjson.JSON;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.elastos.common.Curve;
import org.elastos.common.ErrorCode;
import org.elastos.common.Helper;
import org.elastos.common.Scrypt;
import org.elastos.ela.ECKey;
import org.elastos.ela.Ela;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.util.Base64;

public class Account {

    private String privateKey;
    public String address;
    public Scrypt scrypt;
    public String encryptedPrivateKey;
    public String salt;
    public String version = "1.0";

    public Account(){
        this.privateKey = Ela.getPrivateKey();
        this.address = Ela.getAddressFromPrivate(this.privateKey);
        this.scrypt = new Scrypt();
        this.salt = Base64.getEncoder().encodeToString(ECKey.generateKey(16));
    }

    public Account(String privateKey){
        this.privateKey = privateKey;
        this.address = Ela.getAddressFromPrivate(privateKey);
        this.scrypt = new Scrypt();
        this.salt = Base64.getEncoder().encodeToString(ECKey.generateKey(16));
    }
    public static String parsePrivateKey(byte[] rawkey) throws Exception{
        Security.addProvider(new BouncyCastleProvider());

        Object[]curveParams = new Object[]{Curve.P256.toString()};
        byte[] d = new byte[32];
        try {
            BigInteger b = new BigInteger(1, rawkey);
            ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec((String) curveParams[0]);
            ECParameterSpec paramSpec = new ECNamedCurveSpec(spec.getName(), spec.getCurve(), spec.getG(), spec.getN());
            ECPrivateKeySpec priSpec = new ECPrivateKeySpec(b, paramSpec);
            KeyFactory kf = KeyFactory.getInstance("EC", "BC");
            PrivateKey privateKey = kf.generatePrivate(priSpec);
            BCECPrivateKey pri = (BCECPrivateKey) privateKey;
            if (pri.getD().toByteArray().length == 33) {
                System.arraycopy(pri.getD().toByteArray(), 1, d, 0, 32);
            } else if (pri.getD().toByteArray().length == 31){
                d[0] = 0;
                System.arraycopy(pri.getD().toByteArray(), 0, d, 1, 31);
            } else {
                byte[] bytes = pri.getD().toByteArray();
                return Helper.toHexString(bytes);
            }
        }catch (Exception e){
            throw new Exception(ErrorCode.EncriptPrivateKeyError);
        }
        return Helper.toHexString(d);
    }

    public void exportGcmEncryptedPrivateKey(String passphrase)throws Exception{

        Security.addProvider(new BouncyCastleProvider());
        byte[] derivedkey = SCrypt.generate(passphrase.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(this.salt), this.scrypt.getN(), this.scrypt.getR(), this.scrypt.getP(), this.scrypt.getDkLen());
        byte[] derivedhalf = new byte[32];
        byte[] iv = new byte[12];
        System.arraycopy(derivedkey,0,iv,0,12);
        System.arraycopy(derivedkey,32,derivedhalf,0,32);

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(derivedhalf, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new GCMParameterSpec(128, iv));
            cipher.updateAAD(this.address.getBytes());
            byte[] encryptedkey = cipher.doFinal(DatatypeConverter.parseHexBinary(this.privateKey));
            encryptedPrivateKey = new String(Base64.getEncoder().encode(encryptedkey));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(ErrorCode.EncriptPrivateKeyError);
        }
    }

    public static byte[] getGcmDecodedPrivateKey(String encryptedPrivateKey, String passphrase ,String address ,byte[] salt , int n) throws Exception {
        if (encryptedPrivateKey == null) {
            throw new Exception(ErrorCode.PrikeyLengthError);
        }
        if (salt.length != 16) {
            throw new Exception(ErrorCode.SaltLengthError);
        }
        byte[] encryptedkey = new byte[]{};
        try{
            encryptedkey = Base64.getDecoder().decode(encryptedPrivateKey);
        }catch (Exception e){
            throw new Exception(ErrorCode.ParamErr("encryptedPriKey is wrong"));
        }

        int N = n;
        int r = 8;
        int p = 8;
        int dkLen = 64;

        byte[] derivedkey = SCrypt.generate(passphrase.getBytes(StandardCharsets.UTF_8), salt, N, r, p, dkLen);
        byte[] derivedhalf = new byte[32];
        byte[] iv = new byte[12];
        System.arraycopy(derivedkey, 0, iv, 0, 12);
        System.arraycopy(derivedkey, 32, derivedhalf, 0, 32);

        byte[] rawkey;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(derivedhalf, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new GCMParameterSpec(128,iv));
            cipher.updateAAD(address.getBytes());
            rawkey = cipher.doFinal(encryptedkey);
        } catch (Exception e) {
            throw new Exception(ErrorCode.EncryptedPrivateKeyAddressPasswordErr);
        }
        if (!address.equals(Ela.getAddressFromPrivate(parsePrivateKey(rawkey)))) {
            throw new Exception(ErrorCode.EncryptedPrivateKeyAddressPasswordErr);
        }
        return rawkey;
    }
    @Override
    public  String toString(){return JSON.toJSONString(this);}
}
