package org.elastos.common;


import org.elastos.ela.bitcoinj.Utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.elastos.common.Opcode.*;

public class Paramsbuilder {

    public static void emit(DataOutputStream o , byte op) throws IOException {
        o.writeByte(op);
    }

    public static void emitPushBool(DataOutputStream o , boolean data) throws IOException {
        if (data){
            emit(o,PUSHT);
        }else emit(o,PUSHF);
    }

    public static void emitPushInteger(DataOutputStream o, Long data) throws IOException {
        if (data == -1){
            emit(o,PUSHM1);
        }else if (data == 0){
            emit(o,PUSH0);
        }else if (data > 0 && data <16){
            emit(o,(byte)(PUSH1 - 1 + data));
        }else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeLong(data);
            byte[] reverseBytes = Utils.reverseBytes(baos.toByteArray());
            emitPushByteArray(o,reverseBytes);
        }
    }

    public static void emitPushByteArray(DataOutputStream o, byte[] data) throws IOException {
        int len = data.length;
        if (len < PUSHBYTES75){
            o.writeByte((byte)len);
        }else if (len < 0x100){
            emit(o,PUSHDATA1);
            o.writeByte((byte)len);
        }else if (len < 0x10000){
            emit(o,PUSHDATA2);
            o.writeChar(Short.reverseBytes((short) len));
        }else {
            emit(o,PUSHDATA4);
            o.writeInt(Integer.reverseBytes(len));
        }
        o.write(data);
    }

    public static void emitPushCall(DataOutputStream o, byte[] codeHash) throws IOException {
        emit(o,TAILCALL);
        o.write(codeHash);
    }

    public static void emitSysCall(DataOutputStream o, String api , Object ...args) throws IOException {
        // 智能合约在虚拟机是基于堆栈数据结构的，先进后出
        for (int i = args.length - 1; i >= 0; i--){
            if (args[i] instanceof Integer || args[i] instanceof Long || args[i] instanceof Short){
                emitPushInteger(o,(Long)args[i]);
            }else if (args[i] instanceof Byte[]){
                emitPushByteArray(o,(byte[])args[i]);
            }else if (args[i] instanceof String){
                emitPushByteArray(o, args[i].toString().getBytes());
            }
            // TODO dispose Uint168
        }
        emit(o,SYSCALL);
        emitPushByteArray(o, api.getBytes());
    }
}
