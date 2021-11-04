package com.enjoy.tools.arsc;

import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * @author Lance
 * @date 2018/2/7
 */

public class Header {

    public short type;
    public short headerSize;
    public int chunkSize;

    public Header(short type, short headSize, int size) {
        this.type = type;
        this.headerSize = headSize;
        this.chunkSize = size;
    }

    public static Header decode(ByteBuffer buffer) {
        short type = buffer.getShort();
        short headSize = buffer.getShort();
        int chunkSize = buffer.getInt();
        return new Header(type, headSize, chunkSize);
    }

}
