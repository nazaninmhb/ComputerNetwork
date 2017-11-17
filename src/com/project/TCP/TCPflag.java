package com.project.TCP;

/**
 * Created by nazanin on 11/17/17.
 */
public class TCPflag {
    public boolean ACK, RST, SYN, FIN, SIZE;

    public TCPflag(boolean ACK, boolean RST, boolean SYN, boolean FIN) {
        this.ACK = ACK;
        this.RST = RST;
        this.SYN = SYN;
        this.FIN = FIN;
    }
    public TCPflag() { init(); }

    public void init () { ACK = RST = SYN = FIN = false; }

    public TCPflag createControlFlag(byte b) {
        TCPflag f = new TCPflag();
        if (((1 << 0) & b) != 0) f.ACK = true;
        if (((1 << 1) & b) != 0) f.RST = true;
        if (((1 << 2) & b) != 0) f.SYN = true;
        if (((1 << 3) & b) != 0) f.FIN = true;
        if (((1 << 4) & b) != 0) f.SIZE = true;
        return f;
    }
}
