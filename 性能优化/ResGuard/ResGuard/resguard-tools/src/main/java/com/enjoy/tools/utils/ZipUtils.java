package com.enjoy.tools.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Lance
 * @date 2018/2/4
 */
public class ZipUtils {


    public static Map<String, Integer> unZip(File zip, File dir) {
        Map<String, Integer> compress = new HashMap<>();
        try {
            ZipFile zipFile = new ZipFile(zip);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                if (name.equals("META-INF/CERT.RSA") || name.equals("META-INF/CERT.SF") || name
                        .equals("META-INF/MANIFEST.MF")) {
                    continue;
                }
                if (!zipEntry.isDirectory()) {
                    File file = new File(dir, name);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    InputStream is = zipFile.getInputStream(zipEntry);
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    is.close();
                    fos.close();
                    compress.put(name, zipEntry.getMethod());
                }
            }
            zipFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compress;
    }

    public static void zip(HashMap<String, Integer> compressData, File dir, File zip) throws
            Exception {
        zip.delete();
        // 对输出文件做CRC32校验
        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
                zip), new CRC32());
        ZipOutputStream zos = new ZipOutputStream(cos);
        compress(compressData, dir, zos, "");
        zos.flush();
        zos.close();
    }

    private static void compress(HashMap<String, Integer> compressData, File srcFile,
                                 ZipOutputStream zos,
                                 String basePath) throws Exception {
        if (srcFile.isDirectory()) {
            compressDir(compressData, srcFile, zos, basePath);
        } else {
            compressFile(compressData, srcFile, zos, basePath);
        }
    }

    private static void compressDir(HashMap<String, Integer> compressData, File dir,
                                    ZipOutputStream zos,
                                    String basePath) throws Exception {
        File[] files = dir.listFiles();
        // 构建空目录
        if (files.length < 1) {
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + "/");
            zos.putNextEntry(entry);
            zos.closeEntry();
        }
        for (File file : files) {
            // 递归压缩
            compress(compressData, file, zos, basePath + dir.getName() + "/");
        }
    }

    private static void compressFile(HashMap<String, Integer> compressData, File file,
                                     ZipOutputStream zos, String dir)
            throws Exception {
        String dirName = dir + file.getName();
        String[] dirNameNew = dirName.split("/");
        StringBuffer buffer = new StringBuffer();
        if (dirNameNew.length > 1) {
            for (int i = 1; i < dirNameNew.length; i++) {
                buffer.append("/");
                buffer.append(dirNameNew[i]);
            }
        } else {
            buffer.append("/");
        }

        byte[] fileContents = readContents(file);

        ZipEntry entry = new ZipEntry(buffer.toString().substring(1));
        Integer method = compressData.get(entry.getName());
        method = method == null ? ZipEntry.STORED : method;
        entry.setMethod(method == null ? 0 : method);
        if (method == ZipEntry.DEFLATED) {
            entry.setMethod(ZipEntry.DEFLATED);
        } else {
            //Exception in thread "main" java.util.zip.ZipException: STORED entry missing size,
            // compressed size, or crc-32
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(fileContents.length);
            CRC32 checksumCalculator = new CRC32();
            checksumCalculator.update(fileContents);
            entry.setCrc(checksumCalculator.getValue());
        }

        zos.putNextEntry(entry);
        zos.write(fileContents);
        zos.closeEntry();
    }

    private static byte[] readContents(File file) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final int bufferSize = 4096;
        try {
            final FileInputStream in = new FileInputStream(file);
            final BufferedInputStream bIn = new BufferedInputStream(in);
            int length;
            byte[] buffer = new byte[bufferSize];
            byte[] bufferCopy;
            while ((length = bIn.read(buffer, 0, bufferSize)) != -1) {
                bufferCopy = new byte[length];
                System.arraycopy(buffer, 0, bufferCopy, 0, length);
                output.write(bufferCopy);
            }
            bIn.close();
        } finally {
            output.close();
        }
        return output.toByteArray();
    }

}
