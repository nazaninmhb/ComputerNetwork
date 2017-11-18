package com.project.TCP;

/**
 * Created by nazanin on 11/17/17.
 */
public class TCPFlag {
    public boolean ACK, RST, SYN, FIN;

    public TCPFlag(boolean ACK, boolean RST, boolean SYN, boolean FIN) {
        this.ACK = ACK;
        this.RST = RST;
        this.SYN = SYN;
        this.FIN = FIN;
    }
    public TCPFlag() { init(); }

    public void init () { ACK = RST = SYN = FIN = false; }

    public static TCPFlag createControlFlag(byte b) {
        TCPFlag f = new TCPFlag();
        if (((1 << 0) & b) != 0) f.ACK = true;
        if (((1 << 1) & b) != 0) f.RST = true;
        if (((1 << 2) & b) != 0) f.SYN = true;
        if (((1 << 3) & b) != 0) f.FIN = true;
        return f;
    }
    public byte toByte() {
        return (byte) ((ACK? 1:0) + (RST? 1<<1:0) + (SYN? 1<<2:0) + (FIN? 1<<3:0));
    }

}
