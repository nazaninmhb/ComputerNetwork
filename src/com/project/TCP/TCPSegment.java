package com.project.TCP;

import java.net.InetAddress;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/**
 * Created by nazanin on 11/17/17.
 */
public class TCPSegment {
    public static final int windowSize = 4; //byte
    public static final int headerSize = 20;//byte

    public int rwnd; //reciever fills it
    public int srcPort, desPort;
    public InetAddress srcIP, desIP;
    public int seqNumber, ackNumber;
    public TCPflag TCPflags;
    public byte[] data = null;
    public TCPPacketStatus status;
    public long checkSum;

    public enum TCPPacketStatus {
        ACKED, WAITING_ACK, WAITING_SND
    }


    public TCPSegment(int srcPort, int desPort, byte[] data, int seqNumber, int ackNumber) {
        this.srcPort = srcPort;
        this.desPort = desPort;
        this.data = data;
        this.TCPflags = new TCPflag();
        this.seqNumber = seqNumber;
        this.ackNumber = ackNumber;
        this.status = TCPPacketStatus.WAITING_SND;
        this.checkSum = CheckSum(data);

    }

    public long CheckSum(byte[] bytes) {
        Checksum checksumEngine = new Adler32();
        checksumEngine.update(bytes, 0, bytes.length);
        long checksum = checksumEngine.getValue();
        return checksum;
    }



}
