package org.elastos.ela;

import org.elastos.api.SingleSignTransaction;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by nan on 18/1/15.
 */
public class SignToolTest {
    @Test
    public void doSign() throws Exception {
        byte[] signature = SignTool.doSign("hello".getBytes(),
                DatatypeConverter.parseHexBinary("C1DCA64C32B6F79FCABE35219818342625471AF884DCF6EE705AAFB13B1FFF7F"));

    }

    /**
     * 发送rawTransaciton
     */
    @Test
    public void sendRawTx(){
        String rawTx = "2000100142D3537373537383435383839313339333436383801629F5FF9F3016D3F0BE021692C1D3488C5A9B739CE06105AEF9CB5E2493CEF5B00000000000001B037DB964A231458D2D6FFD5EA18944C4F90E63D547C5D3B9874DF66A4EAD0A3020000000000000000000000211C51ECECC3AC485ED4A6D8253C2B043A160AA07400000000014140EE83A6360D64CB35CD8FD0BF8ED738A9EB32C35082745AC025FA4325F0220A460471636CD4359B8D474D9831CD71EACC269B57989D6F0B95D331E81F3A519C582321037F3CAEDE72447B6082C1E8F7705FFD1ED6E24F348130D34CBC7C0A35C9E993F5AC";
        String rpcUrl = "http://127.0.0.1:10336";
        String txid = SingleSignTransaction.sendRawTransaction(rawTx, rpcUrl);
        System.out.println("Txid = " + txid);
    }

    @Test
    public void printhex(){
        String s = "测试";
        System.out.println(DatatypeConverter.printHexBinary(s.getBytes()));
    }
}