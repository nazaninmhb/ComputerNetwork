package com.project.TCP;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by nazanin on 11/17/17.
 */
public class TCPSocket {
    public int srcPort, desPort;
    public InetAddress srcIP, desIP;
    protected DatagramSocket udp;

    public TCPSocket(int srcPort, int desPort, InetAddress srcIP, InetAddress desIP, DatagramSocket udp) {
        this.srcPort = srcPort;
        this.desPort = desPort;
        this.srcIP = srcIP;
        this.desIP = desIP;
        this.udp = udp;
    }

    public void connect() {
        //state1: SEND SYN
        TCPflag tcPflag = new TCPflag();
        tcPflag.SYN = true;


    }
}
