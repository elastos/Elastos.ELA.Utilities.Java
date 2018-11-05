package org.elastos.ela;

import org.elastos.common.Util;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static org.junit.Assert.*;

/**
 * Created by nan on 18/1/11.
 */
public class UtilTest {
    @Test
    public void writeVarUint() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        Util.WriteVarUint(dos,1);
        System.out.println(DatatypeConverter.printHexBinary(baos.toByteArray()));
        assertEquals("01",DatatypeConverter.printHexBinary(baos.toByteArray()));
        baos.reset();

        Util.WriteVarUint(dos,65534);
        System.out.println(DatatypeConverter.printHexBinary(baos.toByteArray()));
        assertEquals("FDFEFF",DatatypeConverter.printHexBinary(baos.toByteArray()));
        baos.reset();

        Util.WriteVarUint(dos,65535);
        System.out.println(DatatypeConverter.printHexBinary(baos.toByteArray()));
        assertEquals("FDFFFF",DatatypeConverter.printHexBinary(baos.toByteArray()));
        baos.reset();

        Util.WriteVarUint(dos,65536L);
        System.out.println(DatatypeConverter.printHexBinary(baos.toByteArray()));
        assertEquals("FE00000100",DatatypeConverter.printHexBinary(baos.toByteArray()));
        baos.reset();

        Util.WriteVarUint(dos,4294967295L);
        System.out.println(DatatypeConverter.printHexBinary(baos.toByteArray()));
        assertEquals("FEFFFFFFFF",DatatypeConverter.printHexBinary(baos.toByteArray()));
        baos.reset();

        Util.WriteVarUint(dos,4294967296L);
        System.out.println(DatatypeConverter.printHexBinary(baos.toByteArray()));
        assertEquals("FF0000000001000000",DatatypeConverter.printHexBinary(baos.toByteArray()));
        baos.reset();

        Util.WriteVarUint(dos,9223372036854775807L);
        System.out.println(DatatypeConverter.printHexBinary(baos.toByteArray()));
        assertEquals("FFFFFFFFFFFFFFFF7F",DatatypeConverter.printHexBinary(baos.toByteArray()));
        baos.reset();




    }
    @Test
    public void ToScriptHash(){
        byte[] hash = Util.ToScriptHash("ENWzVcjJWuHT5HzVMQ7aBUurHAg3iybynF");
        String hashString = DatatypeConverter.printHexBinary(hash);
        System.out.println(hashString);
        assertEquals("213AD589D302A4DF4E3250E661CCEFC88AA1477AC8",hashString);
        String address = Util.ToAddress(hash);
        System.out.println(address);
        assertEquals("ENWzVcjJWuHT5HzVMQ7aBUurHAg3iybynF",address);

    }
//    @Test
//    public void checkAddress() {
//        String address = "";
//        boolean ret = Util.checkAddress(address);
//        assertFalse(ret);
//
//        address = "EdiARnGaq5aoiXdvDbM58f4wXm3DWC4F6t";
//        ret = Util.checkAddress(address);
//        assertTrue(ret);
//
//        address = "EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZz";
//        ret = Util.checkAddress(address);
//        assertTrue(ret);
//
//        address = "EXgtxGg4ep6vM6uCqWuxkP9KG4AGFyufZy";
//        ret = Util.checkAddress(address);
//        assertFalse(ret);
//
//        address = "8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8QT";
//        ret = Util.checkAddress(address);
//        assertTrue(ret);
//
//        address = "8Frmgg4KMudMEPc5Wow5tYXH8XBgctT8Qx";
//        ret = Util.checkAddress(address);
//        assertFalse(ret);
//    }

    @Test
    public void sortByteArrayArrayUseRevertBytesSequence() {
        byte[][] baa = {{0x01, 0x02, 0x02, 0x01}, {0x02, 0x02, 0x03, 0x01}};

        Util.sortByteArrayArrayUseRevertBytesSequence(baa);

        for (int i = 0; i < baa.length; i++) {
            System.out.println(DatatypeConverter.printHexBinary(baa[i]));
        }
    }
}