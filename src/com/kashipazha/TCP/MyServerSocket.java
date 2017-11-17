package com.kashipazha.TCP;

/**
 * Created by nazanin on 11/17/17.
 */
public abstract class MyServerSocket {
    public MyServerSocket(int portNumber) throws Exception{}

    public abstract MySocket accept() throws Exception;
}
