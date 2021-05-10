

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LK on 2017/9/5.
 * 
 */

public class Dx {

    public static File jar2Dex(File aarFile) throws IOException, InterruptedException {
        File fakeDex = new File(aarFile.getParent() + File.separator + "temp");
        System.out.println("jar2Dex: aarFile.getParent(): " + aarFile.getParent());
      //解压aar到 fakeDex 目录下
        Zip.unZip(aarFile, fakeDex);
      //过滤找到对应的fakeDex 下的classes.jar
        File[] files = fakeDex.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equals("classes.jar");
            }
        });
        if (files == null || files.length <= 0) {
            throw new RuntimeException("the aar is invalidate");
        }
        File classes_jar = files[0];
       // 将classes.jar 变成classes.dex
        File aarDex = new File(classes_jar.getParentFile(), "classes.dex");

      //我们要将jar 转变成为dex 需要使用android tools 里面的dx.bat
        //使用java 调用windows 下的命令
        Dx.dxCommand(aarDex, classes_jar);
        return aarDex;
    }

    public static void dxCommand(File aarDex, File classes_jar) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd.exe /C dx --dex --output=" + aarDex.getAbsolutePath() + " " +
                classes_jar.getAbsolutePath());

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
        if (process.exitValue() != 0) {
        	InputStream inputStream = process.getErrorStream();
        	int len;
        	byte[] buffer = new byte[2048];
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	while((len=inputStream.read(buffer)) != -1){
        		bos.write(buffer,0,len);
        	}
        	System.out.println(new String(bos.toByteArray(),"GBK"));
            throw new RuntimeException("dx run failed");
        }
        process.destroy();
    }
}
