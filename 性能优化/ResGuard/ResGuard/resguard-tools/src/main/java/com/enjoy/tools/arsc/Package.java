package com.enjoy.tools.arsc;

import java.nio.ByteBuffer;

/**
 * @author Lance
 * @date 2019/5/7
 */
public class Package {

    public Header mHeader;
    public int mPackageId;
    public byte[] mPackageName = new byte[256];
    public int mTypeNameOffset;
    public int mTypeNameCount;
    public int mKeyNameOffset;
    public int mKeyNameCount;
    public int mTypeIdOffset;

    public static Package decode(ByteBuffer buffer) {
        Package pck = new Package();
        pck.mHeader = Header.decode(buffer);
        //包id
        pck.mPackageId = buffer.getInt();
        //包名
        buffer.get(pck.mPackageName);
        //类型字符串池偏移 (color layout drawable等)
        pck.mTypeNameOffset = buffer.getInt();
        //lastpublictype 类型字符串资源池的个数
        pck.mTypeNameCount = buffer.getInt();
        //资源项名称字符串池 (app_name activity_main 等)
        pck.mKeyNameOffset = buffer.getInt();
        //lastpublickey 资源名称字符串资源池的个数
        pck.mKeyNameCount = buffer.getInt();
        //源码中 ResTable_package 还有个 uint32_t typeIdOffset;
        pck.mTypeIdOffset = buffer.getInt();
        return pck;
    }
}
