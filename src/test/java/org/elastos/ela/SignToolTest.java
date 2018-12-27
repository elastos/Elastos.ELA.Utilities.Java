package org.elastos.ela;

import org.elastos.api.ELATransaction;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;


/**
 * Created by nan on 18/1/15.
 */
public class  SignToolTest {
    @Test
    public void doSign() throws Exception {
        byte[] signature = SignTool.doSign("hello".getBytes(),
                DatatypeConverter.parseHexBinary("C1DCA64C32B6F79FCABE35219818342625471AF884DCF6EE705AAFB13B1FFF7F"));

    }
}