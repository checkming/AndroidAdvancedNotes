package com.enjoy.tools.arsc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Lance
 * @date 2018/2/3
 */
public class ARSC {
    public Header mHeader;
    //全局字符串池
    public StringPoolRef mTableStrings;
    //包数据
    public Package mPackage;
    public StringPoolRef mTypeNames;
    public StringPoolRef mKeyNames;
    public int mIndex;
    public ByteBuffer remaing;

    public static ARSC decode(File file) throws Exception {
        //将文件全读出来，保存到 bytebuffer中
        FileInputStream is = new FileInputStream(file);
        // ByteOrder.LITTLE_ENDIAN 小端模式
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length()).order(ByteOrder.LITTLE_ENDIAN);
        int len;
        byte[] bytes = new byte[1024];
        while ((len = is.read(bytes)) != -1) {
            buffer.put(bytes, 0, len);
        }
        is.close();
        buffer.flip();
        return decode(buffer);
    }

    public static ARSC decode(ByteBuffer buffer) throws Exception {
        ARSC arsc = new ARSC();
        /**
         * 1、解析 RES_TABLE_TYPE
         */
        //解析头信息 type+头大小+块大小
        arsc.mHeader = Header.decode(buffer);
        //获得包数，正常都是：1
        int packageNum = buffer.getInt();
        if (packageNum != 1) {
            throw new RuntimeException("package number is:" + packageNum);
        }

        /**
         * 2、解析 RES_STRING_POOL_TYPE
         * 全局字符串资源池 包含了在string.xml中定义的值 与 res/xx/xx的路径地址
         */
        arsc.mTableStrings = StringPoolRef.decode(buffer);
        System.out.println("全局字符串资源池:");
        for (int i = 0; i < arsc.mTableStrings.getStringCount(); i++) {
            System.out.println("    " + arsc.mTableStrings.getString(i));
        }

        /**
         * 3、解析 RES_TABLE_PACKAGE_TYPE
         */
        arsc.mPackage = Package.decode(buffer);
        //包中的类型字符串池 (color、drawable等)
        arsc.mTypeNames = StringPoolRef.decode(buffer);
        System.out.println("类型字符串资源池:");
        for (int i = 0; i < arsc.mTypeNames.getStringCount(); i++) {
            System.out.println("    " + arsc.mTypeNames.getString(i));
        }
        //包中的关键字字符串池 (app_name、colorPrimary等)
        arsc.mKeyNames = StringPoolRef.decode(buffer);
        System.out.println("资源名称字符串资源池:");
        for (int i = 0; i < arsc.mKeyNames.getStringCount(); i++) {
            System.out.println("    " + arsc.mKeyNames.getString(i));
        }
        /**
         * 4、后面的不需要解析了
         */
        arsc.mIndex = buffer.position();
        arsc.remaing = buffer;
        return arsc;
    }

    public void createFile(StringPoolRef tableStrings,  StringPoolRef
            keyNames, File output) throws IOException {
        //获得新老字符串池长度差
        int tableStringsChange = mTableStrings.getChunkSize() - tableStrings.getChunkSize();
        int keyNamesChange = mKeyNames.getChunkSize() - keyNames.getChunkSize();

        System.out.println("原文件大小:" + mHeader.chunkSize);
        //新文件的大小
        mHeader.chunkSize -= (tableStringsChange  + keyNamesChange);
        System.out.println("新文件大小:" + mHeader.chunkSize);

        ByteBuffer buffer = ByteBuffer.allocate(mHeader.chunkSize).order(ByteOrder.LITTLE_ENDIAN);
        /**
         * 1、写入 RES_TABLE_TYPE
         */
        writeHead(buffer, mHeader);
        //packageNum
        buffer.putInt(1);
        /**
         * 2、写入 RES_STRING_POOL_TYPE 全局字符串池
         */
        writeStringPool(buffer, tableStrings);

        /**
         * 3、写入 RES_TABLE_PACKAGE_TYPE
         */
        mPackage.mHeader.chunkSize -=  keyNamesChange;
        writePackage(buffer, mPackage);
        //类型字符串池
        writeStringPool(buffer, mTypeNames);
        //资源名称字符串池
        writeStringPool(buffer, keyNames);

        System.out.println("已经写入:" + buffer.position());
        System.out.println("还需写入:" + (buffer.capacity() - buffer.position()));
        System.out.println("余下数据:" + (remaing.capacity() - remaing.position()));

        buffer.put(remaing);

        FileOutputStream fos = new FileOutputStream(output);
        fos.write(buffer.array());
        fos.flush();
        fos.close();
    }

    private void writePackage(ByteBuffer buffer, Package pkg) {
        writeHead(buffer, pkg.mHeader);
        buffer.putInt(pkg.mPackageId);
        buffer.put(pkg.mPackageName);
        buffer.putInt(pkg.mTypeNameOffset);
        buffer.putInt(pkg.mTypeNameCount);
        buffer.putInt(pkg.mKeyNameOffset);
        buffer.putInt(pkg.mKeyNameCount);
        buffer.putInt(pkg.mTypeIdOffset);
    }

    private void writeStringPool(ByteBuffer buffer, StringPoolRef stringPoolRef) {
        //必须被4整除 不足补0
        int add = stringPoolRef.mStrings.length % 4;
        if (add != 0) {
            add = 4 - add;
        }
        //写入类型
        buffer.putShort((short) 0x0001);
        //写入包头长 固定：28字节
        buffer.putShort((short) 0x001C);
        //写入块大小
        buffer.putInt(stringPoolRef.getChunkSize());
        //字符串与style数
        buffer.putInt(stringPoolRef.mStringOffsets.length);
        int styleCount = stringPoolRef.mStyleOffsets == null ? 0 : stringPoolRef
                .mStyleOffsets.length;
        buffer.putInt(styleCount);
        //编码
        buffer.putInt(stringPoolRef.isUTF8 ? StringPoolRef.UTF8_FLAG : 0);
        //字符串数据偏移 包头长加两个偏移int数组长
        buffer.putInt(28 + stringPoolRef.mStringOffsets.length * 4 + styleCount * 4);
        //style数据偏移 包头加两个偏移数组 加字符串数据(需要是4的倍数，可能需要补字节=add个)
        buffer.putInt(styleCount == 0 ? 0 : 28 + stringPoolRef
                .mStringOffsets.length * 4 +
                styleCount * 4 + stringPoolRef.mStrings.length
                + add);
        //字符串偏移数组
        writeIntArray(buffer, stringPoolRef.mStringOffsets);
        //style偏移数组
        if (stringPoolRef.mStyleOffsets != null && styleCount != 0) {
            writeIntArray(buffer, stringPoolRef.mStyleOffsets);
        }
        //字符串数据
        buffer.put(stringPoolRef.mStrings);
        //补字节
        for (int i = 0; i < add; i++) {
            buffer.put((byte) 0);
        }
        if (stringPoolRef.mStyles != null && stringPoolRef.mStyles.length != 0) {
            buffer.put(stringPoolRef.mStyles);
        }
    }


    private void writeIntArray(ByteBuffer buffer, int[] ints) {
        for (int i = 0; i < ints.length; i++) {
            buffer.putInt(ints[i]);
        }
    }

    private void writeHead(ByteBuffer buffer, Header header) {
        buffer.putShort(header.type);
        buffer.putShort(header.headerSize);
        buffer.putInt(header.chunkSize);
    }
}
