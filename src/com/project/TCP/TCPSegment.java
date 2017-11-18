package com.project.TCP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/**
 * Created by nazanin on 11/17/17.
 */
public class TCPSegment {
    public static final int headerSize = 25; //byte
    public static final int maxUDPload = 1447;

    private int rwnd; //reciever fills it
    private int srcPort, desPort; //4+4 byte = 8
    private InetAddress srcIP, desIP;
    private int seqNumber, ackNumber; // 4 + 4  = 8 byte
    protected TCPFlag TCPflags; //1byte
    private byte[] data = null;
    private TCPPacketStatus status;
    private long checkSum; //8 byte

    public enum TCPPacketStatus {
        ACKED, WAITING_ACK, WAITING_SND
    }

    public TCPSegment() {
    }

    public TCPSegment(int srcPort, int desPort, byte[] data, int seqNumber, int ackNumber) {
        this.srcPort = srcPort;
        this.desPort = desPort;
        this.data = data;
        this.TCPflags = new TCPFlag();
        this.seqNumber = seqNumber;
        this.ackNumber = ackNumber;
        this.status = TCPPacketStatus.WAITING_SND;
        this.checkSum = CheckSum();
    }
    public TCPSegment(int srcPort, int dstPort, int seqNumber, int ackNumber,
                      TCPFlag TCPFlag, byte[] data)  {
        this(srcPort, dstPort, data, seqNumber,ackNumber);
        this.TCPflags = TCPFlag;
        CheckSum(data);
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(headerToByteArray());
        outputStream.write(longToByteConvertor(checkSum));
        outputStream.write(TCPflags.toByte());
        outputStream.write(this.data);

        return outputStream.toByteArray();
    }

    public static TCPSegment createPacket(DatagramPacket recievedUDPpacket) throws Exception {
        byte[] recievedData = recievedUDPpacket.getData(); //length?
        if (recievedData.length < TCPSegment.headerSize || recievedData.length > TCPSegment.headerSize + TCPSegment.maxUDPload)
            throw new Exception();

        int srcPort, desPort, seqNum, ackNum;
        byte[] payload = null;
        long checkSum;

        srcPort = bytetoIntConvertor(recievedData,0,4);
        desPort = bytetoIntConvertor(recievedData,4,8);
        seqNum = bytetoIntConvertor(recievedData,8,12);
        ackNum = bytetoIntConvertor(recievedData,12,16);
        checkSum = byteToLongConvertor(recievedData, 16, 24);
        TCPFlag flag = TCPFlag.createControlFlag(recievedData[24]);

        if (recievedData.length > headerSize) {
            payload = Arrays.copyOfRange(recievedData, 25, recievedData.length);
        }

        TCPSegment recievedTCP = new TCPSegment(srcPort, desPort, seqNum, ackNum, flag, payload);
        recievedTCP.srcIP = recievedUDPpacket.getAddress();

        return recievedTCP;

    }



    public long CheckSum() throws IOException {
        Checksum checksumEngine = new Adler32();

        checksumEngine.update(headerToByteArray(),0, headerToByteArray().length);
        checksumEngine.update(new byte[]{this.TCPflags.toByte()},0, 1);

        if (this.data != null)
            checksumEngine.update(this.data, 0, this.data.length);
        long checksum = checksumEngine.getValue();
        return checksum;
    }

    public byte[] intToByteConvertor(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public byte[] longToByteConvertor(long value) {
        return ByteBuffer.allocate(Long.BYTES).putLong(value).array();
    }

    public static int bytetoIntConvertor(byte[] data, int from, int to) {
        byte[] range = Arrays.copyOfRange(data, from, to);
        return ByteBuffer.wrap(range).getInt();
    }

    public static long byteToLongConvertor(byte[] data, int from, int to) {
        byte[] range = Arrays.copyOfRange(data, from, to);
        return ByteBuffer.wrap(range).getLong();
    }
    public byte[] headerToByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(intToByteConvertor(srcPort));
        outputStream.write(intToByteConvertor(desPort));
        outputStream.write(intToByteConvertor(seqNumber));
        outputStream.write(intToByteConvertor(ackNumber));
        return outputStream.toByteArray();
    }


}
