package com.enjoy.tools.arsc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lance
 * @date 2018/2/7
 */
public class StringPoolRef {

    public Header mHeader;
    public boolean isUTF8;
    public int[] mStringOffsets;
    public int[] mStyleOffsets;
    public byte[] mStrings;
    public byte[] mStyles;

    public static final int UTF8_FLAG = 0x00000100;

    public StringPoolRef() {
    }

    private StringPoolRef(int[] stringOffsets, byte[] strings, int[] styleOffsets, byte[] styles,
                          boolean isUTF8) {
        mStringOffsets = stringOffsets;
        mStrings = strings;
        mStyleOffsets = styleOffsets;
        mStyles = styles;
        this.isUTF8 = isUTF8;
    }

    /**
     * github google 开源arsc解析工程: https://github.com/google/android-arscblamer
     * 与android源码中的
     * https://www.androidos.net.cn/android/8.0.0_r4/xref/frameworks/base/libs/androidfw/include
     * /androidfw/ResourceTypes.h
     *
     * @param buffer
     * @return
     */
    public static StringPoolRef decode(ByteBuffer buffer) {
        StringPoolRef pools = new StringPoolRef();

        pools.mHeader = Header.decode(buffer);
        //字符串数
        int stringCount = buffer.getInt();
        //字符串样式数
        int styleCount = buffer.getInt();
        //字符串标记
        int flags = buffer.getInt();
        //字符串和字符串样式偏移
        int stringsOffset = buffer.getInt();
        int stylesOffset = buffer.getInt();
        // 字符串编码:utf8/utf16
        pools.isUTF8 = flags == 256;
        //字符串偏移数组 每一个条目的值 指向字符串数据(byte数组)下标
        pools.mStringOffsets = new int[stringCount];
        for (int i = 0; i < stringCount; i++) {
            pools.mStringOffsets[i] = buffer.getInt();
        }

        //如果存在 style  style偏移数组
        if (styleCount != 0) {
            pools.mStyleOffsets = new int[stringCount];
            for (int i = 0; i < stringCount; i++) {
                pools.mStyleOffsets[i] = buffer.getInt();
            }
        }

        /**
         * 读取字符串
         */
        //字符串数据的总长度
        int size = pools.mHeader.chunkSize - stylesOffset - stringsOffset;
        //规定arsc文件 字符串池中的 字符串字节长度必须能被4整除(长度不能被4整除会在编码时补0)
        if ((size % 4) != 0) {
            throw new RuntimeException("String data size must not multiple of 4");
        }
        pools.mStrings = new byte[size];
        buffer.get(pools.mStrings);

        //如果有style 读取style
        if (stylesOffset != 0) {
            size = (pools.mHeader.chunkSize - stylesOffset);
            //style数据一样要被4整除
            if ((size % 4) != 0) {
                throw new RuntimeException("Style data size is not multiple of 4");
            }
            pools.mStyles = new byte[size];
            buffer.get(pools.mStyles);
        }

        return pools;
    }

    public int getStringCount() {
        return mStringOffsets.length;
    }


    public String getString(int index)  {
        int offset = mStringOffsets[index];
        //对于utf-8编码 有两个长度 一个字符长度与一个编码长度
        //对于utf-16 只有一个字符长度
        if (isUTF8) {
            //字符串长度
            int strlen = mStrings[offset++] & 0xFF;
            //如果大于 0x80(最高位为1) 需要两个字节
            if ((strlen & 0x80) != 0) {
                strlen = ((strlen & 0x7F) << 8) | (mStrings[offset++] & 0xFF);
            }
            //编码长度
            int encodelen = mStrings[offset++] & 0xFF;
            if ((encodelen & 0x80) != 0) {
                encodelen = ((encodelen & 0x7F) << 8) | (mStrings[offset++] & 0xFF);
            }
            return new String(mStrings, offset, encodelen, StandardCharsets.UTF_8);
        } else {
            // 两个字节组成一个 short
            int strlen1 = mStrings[offset++] & 0xFF;
            int strlen2 = mStrings[offset++] & 0xFF;
            int strlen = strlen2 << 8 | strlen1;
            // 大于0x8000 还需要第二个short
            if ((strlen & 0x8000) != 0) {
                strlen1 = mStrings[offset++] & 0xFF;
                strlen2 = mStrings[offset++] & 0xFF;
                int temp = strlen2 << 8 | strlen1;
                //两个short 组成一个int
                strlen = ((strlen & 0x7FFF) << 16) | (temp & 0xFFFF);
            }

            //字符串编码长度 *2
            int encodelen = strlen << 1;
            return new String(mStrings, offset, encodelen, StandardCharsets.UTF_16LE);
        }
    }


    public StringPoolRef encode(Map<String, String> obfuscaters) throws IOException {
        List<ByteBuffer> strings = new ArrayList<>();

        int stringCount = getStringCount();
        int offset = 0;
        int[] stringOffsets = new int[stringCount];
        //字符编码的头信息(utf8的字符长度与编码长度) 最长有4个字节
        byte[] stringheads = new byte[4];
        for (int i = 0; i < stringCount; i++) {
            String string = getString(i);
            String newString = string;
            //取出混淆后的字符串
            if (obfuscaters.containsKey(string)) {
                newString = obfuscaters.get(string);
            }
            byte[] stringData = newString.getBytes(StandardCharsets.UTF_8);
            //偏移数组
            stringOffsets[i] = offset;
            //记录编码使用的长度
            int offsetLen = 0;
            //字符长度
            if (newString.length() < 0x80) {
                stringheads[offsetLen++] = (byte) (0x7f & (newString.length()));
            } else {
                //大于0x80就要两个字节记录
                int len = newString.length();
                //第一个字节是 高位字节数据
                stringheads[offsetLen++] = (byte) ((byte) ((len & 0xff00) >> 8) | 0x80);
                //& 0xff 获得低8位数据
                stringheads[offsetLen++] = (byte) (len & 0xff);
            }
            //编码后长度
            if (stringData.length < 0x80) {
                stringheads[offsetLen++] = (byte) (0x7f & (stringData.length));
            } else {
                int len = stringData.length;
                stringheads[offsetLen++] = (byte) ((byte) ((len & 0xff00) >> 8) | 0x80);
                stringheads[offsetLen++] = (byte) (len & 0xff);
            }
            ByteBuffer buffer = ByteBuffer.allocate(offsetLen + stringData.length + 1).order
                    (ByteOrder.LITTLE_ENDIAN);
            buffer.put(stringheads, 0, offsetLen);
            buffer.put(stringData);
            //utf-8 一个字节0结尾
            buffer.put((byte) 0);
            buffer.flip();
            strings.add(buffer);
            //下一个字符串数据开始位置 偏移
            offset += (offsetLen + stringData.length + 1);
        }
        //获得字符串池数据 (编码完成的)
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (ByteBuffer buffer : strings) {
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            bos.write(data);
        }
        byte[] bytes = bos.toByteArray();
        bos.close();

        return new StringPoolRef(stringOffsets, bytes, mStyleOffsets,
                mStyles, true);
    }

    public int getChunkSize() {
        //字符串池块 头长 28
        int size = 28;
        //字符串偏移数组长 int 4字节
        size += (mStringOffsets.length * 4);
        //style偏移数组长
        if (mStyleOffsets != null && mStyleOffsets.length != 0) {
            size += (mStyleOffsets.length * 4);
        }
        //字符串数据长
        size += mStrings.length;
        //字符串数据长必须能被4整除 否则需要补0
        int i = mStrings.length % 4;
        if (i != 0) {
            i = 4 - i;
            size += i;
        }
        //style数据长 同样要被4整除(我们没动过它，所以不用管)
        if (mStyles != null && mStyles.length != 0) {
            size += mStyles.length;
        }
        return size;
    }
}
