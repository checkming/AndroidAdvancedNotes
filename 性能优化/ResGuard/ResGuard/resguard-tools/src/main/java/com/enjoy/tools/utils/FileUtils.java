/**
 * Copyright 2010 Ryszard Wiśniewski <brut.alll@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.enjoy.tools.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;


/**
 * @author Lance
 * @date 2018/2/4
 */
public class FileUtils {

    public static void rmdir(File dir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                rmdir(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

    public static void cpFile(File src, File dst) throws Exception {
        if (!src.exists()) {
            return;
        }
        dst.delete();
        dst.getParentFile().mkdirs();
        RandomAccessFile fis = new RandomAccessFile(src, "r");
        byte[] bytes = new byte[(int) fis.length()];
        fis.readFully(bytes);
        fis.close();
        FileOutputStream fos = new FileOutputStream(dst);
        fos.write(bytes);
        fos.close();
        fos.flush();
    }

    public static void cpFiles(File src, File dst) throws Exception {
        if (src.isFile()) {
            cpFile(src, dst);
            return;
        }
        File[] files = src.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                cpFiles(file, new File(dst, file.getName()));
                continue;
            }
            cpFile(file, new File(dst, file.getName()));
        }
    }

}
