package com.enjoy.ipc2;

// Declare any non-default types here with import statements
import com.enjoy.ipc2.model.Request;
import com.enjoy.ipc2.model.Response;

interface IIPCService {

    Response send(in Request request);
}
