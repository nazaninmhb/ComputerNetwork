package com.project.TCP;

import com.kashipazha.TCP.MySocket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;

/**
 * Created by nazanin on 11/17/17.
 */
public class TCPSocket extends ClientSocket implements MySocket {
    public static final int windowSize = 8; //byte
    public TCPBuffer buffer = new TCPBuffer();
    public State state;
    public enum State {
        CLOSED, ESTABLISHED, FIN_WAIT1, FIN_WAIT2, TIME_WAIT;
    }
    public TCPSocket(int srcPort, int desPort, InetAddress srcIP, InetAddress desIP) throws SocketException {
        super(srcPort, desPort, srcIP, desIP);
    }
    public TCPSocket(DatagramSocket udp) {
        super(udp);
    }

    @Override
    public void send(String pathToFile) throws Exception {

    }

    @Override
    public void read(String pathToFile) throws Exception {

    }

    @Override
    public void send(byte[] array) throws Exception {

    }

    @Override
    public void read(byte[] array) throws Exception {

    }

    public void close() throws Exception {
        //send ACK
        TCPFlag header = new TCPFlag();
        header.ACK = true;
        TCPSegment sendPacket = createSegmentPacket(header);
        sendTCPpacket(sendPacket);
        state = State.FIN_WAIT1;
        //recive ACK
        while (this.state == State.FIN_WAIT1) {
            TCPSegment receivePacket = receiveTCPpacket();
            if (!receivePacket.TCPflags.ACK)
                throw new IOException("Expected ACK packet");
            this.state = State.FIN_WAIT2;
        }
        //send FIN
        TCPSegment receivePacket = receiveTCPpacket();
        if (!receivePacket.TCPflags.FIN)
            throw new IOException("Expected FIN packet");
        header = new TCPFlag();
        header.ACK = true;
        sendPacket = createSegmentPacket(header);
        sendTCPpacket(sendPacket);
        state = State.TIME_WAIT;
        //after 30 seconds
        udp.close();
        state = State.CLOSED;

    }

    @Override
    public Map<String, String> getHeaders() throws Exception {
        return null;
    }

    public TCPSocket createNewSocket() {
        return new TCPSocket(this.udp);
    }
}
