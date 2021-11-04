package com.enjoy.tools.obfuscater;

import java.util.ArrayList;
import java.util.List;


/**
 * 生成混淆后的名
 * proguard 源码
 * https://github.com/facebook/proguard
 * #
 * src/proguard/obfuscate/SimpleNameFactory.java
 * @author Lance
 */
public class SimpleNameFactory {
    //char最大个数 26个小写字母
    private static final int CHARACTER_COUNT = 26;

    //缓存使用过的字符串
    private static final List cachedCaseNames = new ArrayList();

    private int index = 0;


    public void reset() {
        index = 0;
    }


    /**
     * 获得下一个名字
     *
     * @return
     */
    public String nextName() {
        return name(index++);
    }


    /**
     * 根据下标返回名称
     */
    private String name(int index) {
        // 如果对应下标的名称在内存
        if (index < cachedCaseNames.size()) {
            return (String) cachedCaseNames.get(index);
        }

        //否则就生成新名称 然后缓存它
        String name = newName(index);
        cachedCaseNames.add(index, name);
        return name;
    }


    /**
     * 根据下标创建新名称
     * index < 26（0-25） 一个字符就可以
     * 否则多个
     * ......
     */
    private String newName(int index) {
        int baseIndex = index / CHARACTER_COUNT;
        int offset = index % CHARACTER_COUNT;

        //获得新字符
        char newChar = charAt(offset);

        String newName;
        if (baseIndex == 0) {
            newName = new String(new char[]{newChar});
        } else {
            //需要多个字符表示这一下标
            newName = name(baseIndex - 1) + newChar;
        }
        return newName;
    }


    private char charAt(int index) {
        //a-z = 97-122
        //index 是求余的结果不可能大于 CHARACTER_COUNT
        return (char) ('a' + index);
    }


    public static void main(String[] args) {
        System.out.println("Some mixed-case names:");
        printNameSamples(new SimpleNameFactory(), 26);
        System.out.println("=============================");
        printNameSamples(new SimpleNameFactory(), 26 * 27 + 1);
    }


    private static void printNameSamples(SimpleNameFactory factory, int count) {
        for (int counter = 0; counter < count; counter++) {
            System.out.println("  [" + factory.nextName() + "]");
        }
    }
}
