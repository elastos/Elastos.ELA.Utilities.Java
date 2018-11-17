package org.elastos.ela;

public class UTXOInputSort implements Comparable<UTXOInputSort>{

    private String txid;
    private String address;
    private String amount;
    private int vont;


    public UTXOInputSort(String txid, String address , int vont , String amount){
        this.txid       = txid;
        this.address    = address;
        this.amount     = amount;
        this.vont       = vont;
    }


    public int compareTo(UTXOInputSort o) {
        return this.amount.compareTo(o.amount);

    }

    public String getTxid(){
        return  this.txid;
    }

    public String getAddress() {
        return this.address;
    }

    public String getAmount(){
        return  this.amount;
    }

    public int getVont(){
        return  this.vont;
    }
}
