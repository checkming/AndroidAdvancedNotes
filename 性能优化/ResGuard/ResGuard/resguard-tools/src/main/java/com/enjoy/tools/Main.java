package com.enjoy.tools;

import com.enjoy.tools.mapping.Mapping;
import com.enjoy.tools.obfuscater.Obfuscater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lance
 * @date 2018/2/3
 */
public class Main {

    public static String OUTDIR = "resguard-tools/build/andres";
    public static String CONFIG = "resguard-tools/config";

    public static void main(String[] args) throws Exception {
        // 7z a -tzip xxx.apk src/* -mx=9 极限压缩
        // 修改apk中某个文件极限压缩为不压缩，如res/raw/a.mp3、assets/a.mp3等文件 不能压缩 否则无法播放
        // 7z a -tzip xxx.apk src/* -mx=0 不压缩

        //强制压缩列表 使用7z压缩apk
        List<String> forceCompress = readForceCompress(CONFIG + "/compressData.txt");

        //待处理APK
        File apkFile = new File("app/build/outputs/apk/debug/app-debug.apk");
        System.out.println(apkFile.getAbsolutePath());

        //混淆并压缩apk
        Obfuscater obfuscater = new Obfuscater(forceCompress, apkFile);
        obfuscater.obfuscate();

    }


    static List<String> readForceCompress(String config) throws Exception {
        ArrayList<String> compressData = new ArrayList<>();
        File compress = new File(config);
        FileInputStream fis = new FileInputStream(compress);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String line;
        while ((line = reader.readLine()) != null) {
            compressData.add(line);
        }
        reader.close();
        return compressData;
    }
}
