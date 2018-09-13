package org.elastos.common;

import com.alibaba.fastjson.JSON;

public class Scrypt implements Cloneable{
    private int n = 16384;
    private int r = 8;
    private int p = 8;
    private int DkLen = 64;
    private String Salt;

    public int getDkLen() {
        return DkLen;
    }

    public void setDkLen(int dkLen) {
        DkLen = dkLen;
    }


    public Scrypt() {
    }

    public Scrypt(int n, int r, int p) {
        this.n = n;
        this.r = r;
        this.p = p;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    @Override
    public Scrypt clone() {
        Scrypt o = null;
        try {
            o = (Scrypt) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}