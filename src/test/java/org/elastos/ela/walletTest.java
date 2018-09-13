package org.elastos.ela;

import org.elastos.wallet.WalletMgr;
import org.junit.Test;

public class walletTest {

    @Test
    public void test() throws Exception {
//        WalletMgr.createAccount("12345");
        System.out.println(WalletMgr.getAccountPrivateKey("12345","ENKj2J5dGjSRgHHxBZ3yLjB6RyXvHikW5K"));
//        byte[] salt = Base64.getDecoder().decode("FG3urh4Nmz+dl7tBZVtGLA==");
//        String priKey = Account.getGcmDecodedPrivateKey("5kAS6b71X9nCSs0MitlCoqp6uZiV9AFwZD7vlLrHiOMD+Jh1Eg5aOqOmzG12xl4S", "12345", "EXyrCCxQaDEb3qZuWHDJ9QmsJyFv9MHAYm", salt, 16384);
//        System.out.println(Account.parsePrivateKey(Helper.hexToBytes(priKey)));
    }
}
