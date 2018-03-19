
package org.elastos.ela;

/**
 * Created by nan on 18/1/10.
 */
public class ReqSign {
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    String privateKey;
    int amount;



    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("privateKey:"+privateKey+" ");
        sb.append("amount:"+amount);
        return sb.toString();
    }

}
