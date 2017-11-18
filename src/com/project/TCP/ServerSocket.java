package com.project.TCP;

import com.kashipazha.TCP.MyServerSocket;
import com.kashipazha.TCP.MySocket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by nazanin on 11/17/17.
 */
public class ServerSocket extends MyServerSocket{

    public int portNum;
    public InetAddress ip;
    public State state = State.LISTEN;

    private TCPSocket serverSocket;
    public ServerSocket(int portNum) throws Exception {
        super(portNum);
        this.portNum = portNum;
        DatagramSocket udp = new DatagramSocket(portNum);
        serverSocket = new TCPSocket(udp);
    }

    public enum State {
        LISTEN, SYN_RCVD, ESTABLISHED, CLOSE_WAIT, LAST_ACK, CLOSED;
    }

    public MySocket accept() throws Exception {
        //get SYN
        TCPSegment receivePacket = serverSocket.receiveTCPpacket();
        while (this.state == State.LISTEN) {
            if (!receivePacket.TCPflags.SYN) {
                throw new IOException("SYN packet is corrupted");
            }
            //send SYN & ACK
            TCPFlag headerControl = new TCPFlag();
            headerControl.SYN = true;
            headerControl.ACK = true;
            TCPSegment packet = serverSocket.createSegmentPacket(headerControl);
            serverSocket.sendTCPpacket(packet);
            this.state = State.SYN_RCVD;
        }

        while (this.state == State.SYN_RCVD) {
            receivePacket = serverSocket.receiveTCPpacket();
            if (!receivePacket.TCPflags.ACK)
                throw new IOException("ACK packet is corrupted");
            //do nothing
            this.state = State.ESTABLISHED;
        }
        return serverSocket.createNewSocket();
    }
    public void closeConnection() throws Exception {
        //receive FIN, send ACK
       // TCPSegment receivePacket = serverSocket.receiveTCPpacket();
        //if (!receivePacket.TCPflags.FIN)
         //   throw new IOException("Expected FIN packet");
//tu ye tabe dg
        TCPFlag header = new TCPFlag();
        header.ACK = true;
        TCPSegment sendPacket = serverSocket.createSegmentPacket(header);
        serverSocket.sendTCPpacket(sendPacket);
        state = State.CLOSE_WAIT;

        header = new TCPFlag();
        header.FIN = true;
        sendPacket = serverSocket.createSegmentPacket(header);
        serverSocket.sendTCPpacket(sendPacket);
        state = State.LAST_ACK;

        TCPSegment receivePacket = serverSocket.receiveTCPpacket();
            if (!receivePacket.TCPflags.ACK)
                throw new IOException("Expected ACK packet");
            this.state = State.CLOSED;
    }

}

