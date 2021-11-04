package com.enjoy.tools.mapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * @author Lance
 * @date 2018/2/11
 */

public class Mapping {

    private File mappingFile;
    private FileOutputStream fos;
    private FileInputStream fis;
    private boolean isWriter;
    private boolean isReader;

    public Mapping(File mappingFile) {
        this.mappingFile = mappingFile;

    }

    public void openWriter() throws IOException {
        if (isReader && null != fis) {
            fis.close();
            return;
        }
        if (isWriter) {
            return;
        }
        fos = new FileOutputStream(mappingFile);
        isWriter = true;
    }

    public void openReader() throws IOException {
        if (isWriter && null != fos) {
            fos.close();
            return;
        }
        if (isReader) {
            return;
        }
        fis = new FileInputStream(mappingFile);
        isReader = true;
    }


    public void writeMapping(Map<String, String> typeMap,Map<String, String> keyMap, Map<String, String> fileMap) throws
            IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("res type mapping:\n");
        //按照ascii排序
        Set<String> typeKeys = typeMap.keySet();
        ArrayList<String> types = new ArrayList<>();
        types.addAll(typeKeys);
        types.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        for (String type : types) {
            sb.append("     " + type + " => " + typeMap.get(type) + "\n");
        }


        sb.append("res key mapping:\n");
        //按照ascii排序
        Set<String> keyKeys = keyMap.keySet();
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(keyKeys);
        keys.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        for (String key : keys) {
            sb.append("     " + key + " => " + typeMap.get(key) + "\n");
        }


        sb.append("res path mapping:\n");
        Set<String> fileKeys = fileMap.keySet();
        ArrayList<String> files = new ArrayList<>();
        files.addAll(fileKeys);
        files.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        for (String file : files) {
            sb.append("     " + file + " => " + fileMap.get(file) + "\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        fos.write(sb.toString().getBytes());
    }

    public void close() throws IOException {
        if (null != fis) {
            fis.close();
            fis = null;
        }
        if (null != fos) {
            fos.close();
            fos = null;
        }
    }

}
