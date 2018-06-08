package org.elastos.ela;

import org.elastos.ela.bitcoinj.Sha256Hash;
import org.elastos.ela.bitcoinj.Utils;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;
import org.spongycastle.crypto.signers.RandomDSAKCalculator;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by nan on 18/1/14.
 */
public class SignTool {
    public static byte[] doSign(byte[] data, byte[] privateKey) {
        BigInteger privateKeyForSigning = new BigInteger(1,privateKey) ;
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
//        ECDSASigner signer = new ECDSASigner(new RandomDSAKCalculator());
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKeyForSigning, ECKey.CURVE);
        signer.init(true, privKey);

        while(true){
            BigInteger[] components = signer.generateSignature(Sha256Hash.hash(data));
            byte[] r = Utils.bigIntegerToBytes(components[0],32);
            byte[] s = Utils.bigIntegerToBytes(components[1],32);
            //byte[] r = components[0].abs().toByteArray();
            //byte[] s = components[1].abs().toByteArray();
            if(r.length>32||s.length>32) continue;

            byte[] signature = new byte[r.length+s.length];
            System.arraycopy(r,0,signature,0,r.length);
            System.arraycopy(s,0,signature,r.length,s.length);
            return signature;
        }

    }

    public static boolean verify(byte[] msg,byte[] sig,byte[] pub){
        return false;
    }
}
