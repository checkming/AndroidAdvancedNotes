package com.enjoy.tools.obfuscater;

import com.enjoy.tools.Main;
import com.enjoy.tools.arsc.ARSC;
import com.enjoy.tools.arsc.StringPoolRef;
import com.enjoy.tools.mapping.Mapping;
import com.enjoy.tools.utils.FileUtils;
import com.enjoy.tools.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

/**
 * @author Lance
 * @date 2018/2/3
 */
public class Obfuscater {

    private List<String> mForceCompress;
    private File mApkFile;

    public Map<String, String> keyMap = new HashMap<>();
    public Map<String, String> typeMap = new HashMap<>();
    public Map<String, String> fileMap = new HashMap<>();

    public Obfuscater(List<String> forceCompress, File apkFile) {
        mForceCompress = forceCompress;
        mApkFile = apkFile;
    }

    public void obfuscate() throws Exception {
        /**
         * 1、解压待处理apk
         */
        File outDir = new File(Main.OUTDIR);

        File oldApp = new File(outDir, "old");
        FileUtils.rmdir(oldApp);
        oldApp.mkdirs();

        // 解压apk 并且获得原来的APK中的文件 是否压缩表
        // ZipEntry.STORED 不压缩
        // ZipEntry.DEFLATED 压缩
        Map<String, Integer> compressData = ZipUtils.unZip(mApkFile, oldApp);
        for (String key : compressData.keySet()) {
            System.out.println(key + ":" + (compressData.get(key) == ZipEntry.STORED ? "不压缩" :
                    "压缩"));
        }
        // 结合强制压缩列表 mForceCompress，修改 compressData 中记录的value
        resetCompress(compressData);


        /**
         * 2、解析ARSC文件
         */
        ARSC arsc = ARSC.decode(new File(oldApp, "resources.arsc"));


        /**
         * 3、混淆字符,产生混淆名集合 (混淆全局字符串池中的res与资源名称字符串池，不能混淆资源类型字符串池)
         */
        obfuscate(arsc);

        //制作混淆后的字符串池
        StringPoolRef tableStrings = arsc.mTableStrings.encode(fileMap);
        StringPoolRef keyNames = arsc.mKeyNames.encode(keyMap);


        /**
         * 4、制作新的ARSC文件(混淆后的)
         */
        File newApkDir = new File(outDir, "app");
        FileUtils.rmdir(newApkDir);
        newApkDir.mkdirs();

        File arscFile = new File(newApkDir, "resources.arsc");
        arsc.createFile(tableStrings, keyNames, arscFile);

        /**
         * 5、将apk中其他文件拷贝到 app目录 并根据混淆修改 res/目录下文件名
         */
        obfuscate(oldApp, arscFile.getParentFile());

        /**
         * 6、打包、对齐、签名
         */
        sevenZipPacking(compressData, newApkDir, outDir);

        /**
         * 7、写出mapping
         */
        //写出mapping文件
        Mapping mapping = new Mapping(new File(outDir,"mappings.txt"));
        mapping.openWriter();
        mapping.writeMapping(typeMap, keyMap, fileMap);
        mapping.close();
    }

    private void sevenZipPacking(Map<String, Integer> compressData, File src, File outDir) throws
            Exception {
        /**
         * 1、7z打包 (需要先安装7z工具，压缩效果最好的压缩工具)
         */
        String name = mApkFile.getName();
        name = name.substring(0, name.lastIndexOf("."));
        //获得当前系统 windows执行cmd命令 需要加上 cmd /c
        //如7z打包a.txt到xx.7z压缩文件 :7z a xx.7z a.txt,在windows中java执行命令需要: cmd /c 7z a xx.7z a.txt
        String os = System.getProperty("os.name");
        String cmd = "";
        if (os.toLowerCase().startsWith("win")) {
            cmd = "cmd /c ";
        }

        //使用7z打包
        //todo（微信）实验证明，linux与mac的7z压缩效果更好
        //todo（微信）部分手机桌面快捷图标的实现有问题，务必将程序桌面icon加入白名单(不混淆)
        //todo 若想代码中通过getIdentifier方式获得资源，需要放置白名单中。

        //7z生成极限压缩zip包: 未对齐、未签名apk
        // -mx=9 :极限压缩
        File unsigned_unaligned_apk = new File(outDir, name + "-7z-unsigned-unaligned.apk");
        unsigned_unaligned_apk.delete();
        String code = cmd + " 7z a -tzip " + unsigned_unaligned_apk
                .getAbsolutePath() + " " + src.getAbsolutePath() + "/* -mx=9";
        Process process = Runtime.getRuntime().exec(code);
        process.waitFor();
        if (process.exitValue() != 0) {
            System.out.println("7z -mx=9 error: " + code);
        }
        process.destroy();

        //todo apk中有些文件不能压缩 比如需要FileDescriptor使用的资源 (getResources().openRawResourceFd)
        //还原 原apk中文件存储形式：STORED/DEFLATED
        //不压缩列表

        //将文件与存储方式集合的文件名改为混淆后
        Map<String, Integer> obfuscateCompressData = new HashMap<>();
        for (Map.Entry<String, Integer> entry : compressData.entrySet()) {
            //获得对应文件混淆后的名称
            String value = fileMap.get(entry.getKey());
            //没有混淆的会得到null
            if (value != null) {
                obfuscateCompressData.put(value, entry.getValue());
            } else {
                obfuscateCompressData.put(entry.getKey(), entry.getValue());
            }
        }

        //记录不需要压缩的文件列表
        List<String> storedFiles = new ArrayList<>();
        for (Map.Entry<String, Integer> data : obfuscateCompressData.entrySet()) {
            String key = data.getKey();
            Integer value = data.getValue();
            if (value == ZipEntry.STORED) {
                File file = new File(src, key);
                if (file.exists()) {
                    storedFiles.add(key);
                }
            }
        }
        System.out.println("不压缩列表:" + storedFiles);
        if (!storedFiles.isEmpty()) {
            //把不压缩的文件从原apk(old目录)拷贝到 stored 目录
            File storedDir = new File(outDir, "stored");
            FileUtils.rmdir(storedDir);
            storedDir.mkdirs();
            for (String stored : storedFiles) {
                File storedFile = new File(src, stored);
                FileUtils.cpFile(storedFile, new File(storedDir, stored));
            }

            //使用新命令: -mx=0 将soted中的文件存入上面打包的APK中(以不压缩替换已存在的被极限压缩了的同名文件)
            //把不压缩文件目录重新存入apk中 -mx=0
            code = cmd + " 7z a -tzip " + unsigned_unaligned_apk
                    .getAbsolutePath() + " " + storedDir.getAbsolutePath() + "/* -mx=0";
            process = Runtime.getRuntime().exec(code);
            process.waitFor();
            if (process.exitValue() != 0) {
                System.out.println("7z -mx=0 error: " + code);
            }
            process.destroy();
        }


        /**
         * 2、zipalign对齐 (在Android SDK/build-tools/xx/目录下,需要配置环境变量)
         *
         *  让Android系统能更高效的操作APK（如读取apk中的资源）
         */
        //对齐后的apk
        File unsigned_aligned_apk = new File(outDir, name + "-7z-unsigned-aligned.apk");
        unsigned_aligned_apk.delete();
        code = cmd + " zipalign -f 4 " + unsigned_unaligned_apk
                .getAbsolutePath() + " " + unsigned_aligned_apk.getAbsolutePath();
        process = Runtime.getRuntime().exec(code);
        process.waitFor();
        if (process.exitValue() != 0) {
            System.out.println("zipalign error!");
        }
        process.destroy();

        /**
         * 3、apksigner签名 (在Android SDK/build-tools/xx/目录下,需要配置环境变量)
         *  --v1-signing-enabled true/false 是否开启v1签名
         *  --v2-signing-enabled true/false 是否开启v2签名
         */
//        apksigner sign           //执行签名操作
//        --ks jks路径                                 //jks签名证书路径
//        --ks-key-alias alias           //生成jks时指定的alias
//        --ks-pass pass:密码          //KeyStore密码
//        --key-pass pass:密码   //签署者的密码，即生成jks时指定alias对应的密码
//        --out output.apk                         //输出路径
//        input.apk                                     //被签名的apk
        File keystore = new File(Main.CONFIG + "/debug.keystore");
        File signed_aligned_apk = new File(outDir, name + "-7z-signed-aligned.apk");
        signed_aligned_apk.delete();

        code = cmd + " apksigner sign --ks " + keystore
                .getAbsolutePath() + "" +
                " --ks-key-alias androiddebugkey --ks-pass pass:android --key-pass pass:android " +
                "--out " + signed_aligned_apk.getAbsolutePath() + " " + unsigned_aligned_apk
                .getAbsolutePath();
        process = Runtime.getRuntime().exec(code);
        process.waitFor();
        if (process.exitValue() != 0) {
            System.out.println("apksigner error:" + code);
        }
        process.destroy();
    }

    private void obfuscate(File oldApp, File app) throws Exception {
        File[] files = oldApp.listFiles();
        //把不需要处理的文件拷贝到目的地
        for (File file : files) {
            if (file.getName().equals("resources.arsc")) {
                continue;
            }
            if (file.isFile() || !file.getName().equals("res")) {
                FileUtils.cpFiles(file, new File(app, file.getName()));
            }
        }

        //修改res下文件名
        File resDir = new File(oldApp, "res");
        for (File typeDir : resDir.listFiles()) {
            for (File resFile : typeDir.listFiles()) {
                String name = fileMap.get("res/" + typeDir.getName() + "/" + resFile.getName());
                FileUtils.cpFiles(resFile, new File(app, name));
            }
        }
    }


    private void obfuscate(ARSC arsc) throws UnsupportedEncodingException {
        /**
         * 1、混淆全局字符串池
         * res/layout/activity_main.xml => r/a/a.xml
         * res/mipmap-anydpi-v26/ic_launcher.xml => r/b/b.xml
         */
        //资源类型混淆器
        SimpleNameFactory typeNameFactory = new SimpleNameFactory();
        //资源名混淆器
        SimpleNameFactory keyNameFactory = new SimpleNameFactory();
        Map<String, SimpleNameFactory> keyNameFactorys = new HashMap<>();
        System.out.println("混淆文件路径:");
        for (int i = 0; i < arsc.mTableStrings.getStringCount(); i++) {
            //原字符串
            String fileName = arsc.mTableStrings.getString(i);
            if (fileName.startsWith("res")) {
                String[] names = fileName.split("/");
                //不符合
                if (names == null || names.length != 3) {
                    continue;
                }
                //新类型名称
                String newTypeName;
                //新资源名称
                String newKeyName;
                //获得混淆名
                newTypeName = typeMap.get(names[1]);
                //此类型还没有混淆后的名称
                if (newTypeName == null) {
                    //生成混淆名
                    newTypeName = typeNameFactory.nextName();
                    typeMap.put(names[1], newTypeName);
                }
                //获得文件名 (去掉后缀)
                int index = names[2].indexOf('.');
                String suffix = "";
                if (index > 0) {
                    suffix = names[2].substring(index, names[2].length());
                    names[2] = names[2].substring(0, index);
                }
                newKeyName = keyMap.get(names[2]);
                if (newKeyName == null) {
                    newKeyName = keyNameFactory.nextName();
                    keyMap.put(names[2], newKeyName);
                }
                String newFileName = "r/" + newTypeName + "/" + newKeyName + "" + suffix;
                System.out.println("    " + fileName + " => " + newFileName);
                fileMap.put(fileName, newFileName);
            }
        }

        /**
         * 2、混淆资源名称字符串池
         *  activity_main =》 a
         *  ic_launcher => b
         */
        //名称混淆名生成器
        System.out.println("混淆资源名:");
        for (int i = 0; i < arsc.mKeyNames.getStringCount(); i++) {
            //原字符串
            String keyName = arsc.mKeyNames.getString(i);
            String newKeyName;
            //如
            // 全局池中有：ResGuard,资源名称池中有：app_name
            //混淆 app_name
            // 全局池中有：res/layout/activity_main.xml,资源名称池中有：activity_main
            //使用全局中activity_main混淆的字符串作为资源名称池的混淆后名称
            if (keyMap.containsKey(keyName)) {
                newKeyName = keyMap.get(keyName);
            } else {
                newKeyName = keyNameFactory.nextName();
            }
            System.out.println("    " + keyName + " => " + newKeyName);
            keyMap.put(keyName, newKeyName);
        }
    }


    private void resetCompress(Map<String, Integer> compressData) {
        for (Map.Entry<String, Integer> entry : compressData.entrySet()) {
            String name = entry.getKey();
            //存在强制压缩表中 则设置为 ZipEntry.DEFLATED 压缩
            for (String data : mForceCompress) {
                if (name.endsWith(data) || name.equals(data)) {
                    compressData.put(name, ZipEntry.DEFLATED);
                    break;
                }
            }
        }
    }
}
