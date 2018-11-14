package org.elastos.ela;

import org.elastos.common.Helper;
import org.elastos.wallet.Account;
import org.elastos.wallet.WalletMgr;
import org.junit.Test;

import java.util.Base64;

public class walletTest {

    @Test
    public void test() throws Exception {
        WalletMgr.createAccount("12345");
//        System.out.println(WalletMgr.getAccountPrivateKey("12345","Edk9NAffJDBGkoBqd4mGUEczTxozw3Vw4D"));
//        byte[] salt = Base64.getDecoder().decode("FG3urh4Nmz+dl7tBZVtGLA==");
//        byte[] priKey = Account.getGcmDecodedPrivateKey("5kAS6b71X9nCSs0MitlCoqp6uZiV9AFwZD7vlLrHiOMD+Jh1Eg5aOqOmzG12xl4S", "12345", "EXyrCCxQaDEb3qZuWHDJ9QmsJyFv9MHAYm", salt, 16384);
//        System.out.println(Account.parsePrivateKey(Helper.hexToBytes(priKey)));
    }
}
