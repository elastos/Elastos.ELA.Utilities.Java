package org.elastos.ela;

import org.elastos.common.SDKException;
import org.elastos.ela.bitcoinj.LazyECPoint;
import org.elastos.ela.bitcoinj.Utils;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.math.ec.FixedPointCombMultiplier;
import org.spongycastle.math.ec.FixedPointUtil;

import javax.annotation.Nullable;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by nan on 18/1/14.
 */
public class ECKey {
    private final static X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256r1");
    public static final ECDomainParameters CURVE;
    private static final SecureRandom secureRandom = new SecureRandom();
    private final BigInteger priv;
    private final LazyECPoint pub;

    static{
        FixedPointUtil.precompute(CURVE_PARAMS.getG(), 12);
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(),
                CURVE_PARAMS.getH());
    }


    public ECKey(){
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(CURVE, secureRandom);
        generator.init(keygenParams);
        AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
        ECPublicKeyParameters pubParams = (ECPublicKeyParameters) keypair.getPublic();
        this.priv = privParams.getD();
        pub = new LazyECPoint(CURVE.getCurve(), pubParams.getQ().getEncoded(true));
    }

    public byte[] getPrivateKeyBytes() {
        return Utils.bigIntegerToBytes(this.priv, 32);
    }
    public byte[] getPubBytes(){
        return pub.getEncoded();
    }


    public static byte[] generateKey(int len){
        byte[] key = new byte[len];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        return key;
    }

    public static byte[] generateKey(){
        return generateKey(32);
    }
    /**
     * Returns public key point from the given private key. To convert a byte array into a BigInteger, use <tt>
     * new BigInteger(1, bytes);</tt>
     */
    public static byte[] publicBytesFromPrivate(byte[] priv) {
        BigInteger privKey = new BigInteger(1,priv);
        /*
         * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group order,
         * but that could change in future versions.
         */
        if (privKey.bitLength() > CURVE.getN().bitLength()) {
            privKey = privKey.mod(CURVE.getN());
        }
        return new FixedPointCombMultiplier().multiply(CURVE.getG(), privKey).getEncoded(true);
    }

    public static ECPoint publicPointFromPrivate(BigInteger privKey) {
        /*
         * TODO: FixedPointCombMultiplier currently doesn't support scalars longer than the group order,
         * but that could change in future versions.
         */
        if (privKey.bitLength() > CURVE.getN().bitLength()) {
            privKey = privKey.mod(CURVE.getN());
        }
        return new FixedPointCombMultiplier().multiply(CURVE.getG(), privKey);
    }

    protected ECKey(@Nullable BigInteger priv, ECPoint pub) {
        this(priv, new LazyECPoint(checkNotNull(pub)));
    }

    protected ECKey(@Nullable BigInteger priv, LazyECPoint pub) {
        if (priv != null) {
            checkArgument(priv.bitLength() <= 32 * 8, "private key exceeds 32 bytes: %s bits", priv.bitLength());
            // Try and catch buggy callers or bad key imports, etc. Zero and one are special because these are often
            // used as sentinel values and because scripting languages have a habit of auto-casting true and false to
            // 1 and 0 or vice-versa. Type confusion bugs could therefore result in private keys with these values.
            checkArgument(!priv.equals(BigInteger.ZERO));
            checkArgument(!priv.equals(BigInteger.ONE));
        }
        this.priv = priv;
        this.pub = checkNotNull(pub);
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated from it (this is slow), either
     * compressed or not.
     */
    public static ECKey fromPrivate(BigInteger privKey, boolean compressed) {
        ECPoint point = publicPointFromPrivate(privKey);
        return new ECKey(privKey, getPointWithCompression(point, compressed));
    }
    /**
     * Creates an ECKey given the private key only. The public key is calculated from it (this is slow). The resulting
     * public key is compressed.
     */
    public static ECKey fromPrivate(byte[] privKeyBytes) {
        return fromPrivate(new BigInteger(1, privKeyBytes));
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated from it (this is slow). The resulting
     * public key is compressed.
     */
    public static ECKey fromPrivate(BigInteger privKey) {
        return fromPrivate(privKey, true);
    }

    private static ECPoint getPointWithCompression(ECPoint point, boolean compressed) {
        if (point.isCompressed() == compressed)
            return point;
        point = point.normalize();
        BigInteger x = point.getAffineXCoord().toBigInteger();
        BigInteger y = point.getAffineYCoord().toBigInteger();
        return CURVE.getCurve().createPoint(x, y, compressed);
    }
    public byte[] getProgram(int singType){
        return Util.CreateSingleSignatureRedeemScript(this.getPubBytes(),singType);
    }
    public byte[] getSingleSignProgramHash(int signType){
        return Util.ToCodeHash(this.getProgram(signType),signType);
    }

    // 1 单签
    public String toAddress(){
        return Util.ToAddress(this.getSingleSignProgramHash(1));
    }

    // 3 身份id
    public String toIdentityID(){
        return Util.ToAddress(this.getSingleSignProgramHash(3));
    }

    //生成X地址
    public static byte[] getGenesisSignatureProgram(String GenesisBlockHash) throws SDKException {
        return Util.GenGenesisAddressRedeemScript(GenesisBlockHash);
    }
    public static byte[] getGenesisSignProgramHash(String GenesisBlockHash) throws SDKException {
        return Util.ToCodeHash(getGenesisSignatureProgram(GenesisBlockHash),4);
    }
    public static String toGenesisSignAddress(String GenesisBlockHash) throws SDKException {
        return Util.ToAddress(getGenesisSignProgramHash(GenesisBlockHash));
    }

}
