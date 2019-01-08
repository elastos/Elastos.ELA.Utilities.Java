package org.elastos.common;

import org.elastos.ela.bitcoinj.Utils;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by nan on 18/1/14.
 */
public class Common {

    public static byte SUFFIX_STANDARD   = (byte)0xAC;
    public static byte SUFFIX_MULTISIG   = (byte)0xAE;
    public static byte SUFFIX_CROSSCHAIN = (byte)0xAF;

    public static byte PREFIX_SINGLESIG  = 0x21;
    public static byte PREFIX_MULTISIG   = 0x12;
    public static byte PREFIX_IDENTITYID = 0x67;

    public static byte PREFIX_CONTRANCT  = 0x1c;
    public static byte PREFIX_CROSSCHAIN = (byte)0x4B;
    public static byte PREFIX_PLEDGE     = 0x1F;

    public final static String SYSTEM_ASSET_ID = "a3d0eaa466df74983b5d7c543de6904f4c9418ead5ffd6d25814234a96db37b0";
    public final static byte[] ELA_ASSETID = Utils.reverseBytes(DatatypeConverter.parseHexBinary(SYSTEM_ASSET_ID));
}
