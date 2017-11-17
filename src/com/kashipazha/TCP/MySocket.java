package com.kashipazha.TCP;

import java.util.Map;

/**
 * Created by nazanin on 11/17/17.
 */
public interface MySocket {
    void send(String pathToFile) throws Exception;
    void read(String pathToFile) throws Exception;

    void send(byte[] array) throws Exception;
    void read(byte[] array) throws Exception;

    void close() throws Exception;

    Map<String,String> getHeaders() throws Exception;
}
