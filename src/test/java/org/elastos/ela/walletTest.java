package org.elastos.ela;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.elastos.wallet.KeystoreFile;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class walletTest {

    @Test
    public void test(){
        //集合一
        List _first=new ArrayList();
        _first.add("EQSpUzE4XYJhBSx5j7Tf2cteaKdFdixfVB");
        _first.add("Eazj14ifau5eH1SP5F8MJRuiSsPMiGbJV1");
        //集合二
        List _second=new ArrayList();
        _second.add("5AABB11492D59E4B3CB52A2589A325406DCEBBCC74B4B9F75DDCB3A26EB09C1E");
        _second.add("5FA927E5664E563F019F50DCD4D7E2D9404F2D5D49E31F9482912E23D6D7B9EB");


        System.out.println(FinishUtxo.availablePrivate(_second,_first));
    }
}
