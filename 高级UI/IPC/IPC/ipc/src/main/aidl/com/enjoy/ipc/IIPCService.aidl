package com.enjoy.ipc;

// Declare any non-default types here with import statements
import com.enjoy.ipc.model.Request;
import com.enjoy.ipc.model.Response;
interface IIPCService {

    Response send(in Request request);
}
