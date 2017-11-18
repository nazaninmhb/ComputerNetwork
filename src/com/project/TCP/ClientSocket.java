package com.project.TCP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by nazanin on 11/17/17.
 */
public class ClientSocket {
    public int srcPort, desPort;
    public InetAddress srcIP, desIP;
    protected DatagramSocket udp;

    public enum State {
        CLOSED, SYN_SENT, ESTABLISHED;
    }
    protected State state = State.CLOSED;

    public ClientSocket(int srcPort, int desPort, InetAddress srcIP, InetAddress desIP) throws SocketException {
        this.srcPort = srcPort;
        this.desPort = desPort;
        this.srcIP = srcIP;
        this.desIP = desIP;
        udp = new DatagramSocket(srcPort, srcIP);
    }
    public ClientSocket(DatagramSocket udp) {
        this.udp = udp;
    }

    public void connect() throws Exception {

        //SEND SYN (handshake)
        TCPFlag headerControl = new TCPFlag();
        headerControl.SYN = true;
        TCPSegment packet = createSegmentPacket(headerControl);
        sendTCPpacket(packet);
        this.state = State.SYN_SENT;

        //timer implementation. send again SYN
        while (this.state == State.SYN_SENT) {
            TCPSegment receivePacket = receiveTCPpacket();
            if (receivePacket.TCPflags.ACK && receivePacket.TCPflags.SYN) {
                headerControl.init();
                headerControl.ACK = true; //ack for receiving
                packet = createSegmentPacket(headerControl);
                sendTCPpacket(packet);
                this.state = State.ESTABLISHED;
            }
            else
                throw new IOException("received SYN ACK is corrupted!");
        }
    }

    TCPSegment receiveTCPpacket() throws Exception {
        byte[] UDPbuff = new byte[TCPSegment.headerSize + TCPSegment.maxUDPload];
        DatagramPacket receivedUDPPacket = new DatagramPacket(UDPbuff, UDPbuff.length);
        udp.receive(receivedUDPPacket);
        TCPSegment recievedTCPpacket = TCPSegment.createPacket(receivedUDPPacket);
        return recievedTCPpacket;
    }

    public TCPSegment createSegmentPacket(TCPFlag headerControl) {
        TCPSegment sendPacket = new TCPSegment(srcPort, desPort,0, 0,headerControl ,null);
        return sendPacket;
    }

    public void sendTCPpacket(TCPSegment TcpPacket) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(TcpPacket.toBytes(), TcpPacket.toBytes().length, desIP, desPort);
        udp.send(datagramPacket);
    }

}
