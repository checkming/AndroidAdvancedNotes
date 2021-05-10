

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;


public class MyMain {

    public static void main(String[] args) throws Exception {
    	
    	byte[] mainDexData; //存储源apk中的源dex文件 
    	byte[] aarData;     // 存储壳中的壳dex文件
    	byte[] mergeDex;    // 存储壳dex 和源dex 的合并的新dex文件
    	
    	
    	File tempFileApk = new File("source/apk/temp");
    	if (tempFileApk.exists()) {
			File[]files = tempFileApk.listFiles();
			for(File file: files){
				if (file.isFile()) {
					file.delete();
				}
			}
		}
    	
    	File tempFileAar = new File("source/aar/temp");
    	if (tempFileAar.exists()) {
    		File[]files = tempFileAar.listFiles();
			for(File file: files){
				if (file.isFile()) {
					file.delete();
				}
			}
		}
    	
        /**
         * 第一步 处理原始apk 加密dex
         *
         */
        AES.init(AES.DEFAULT_PWD);
        //解压apk
        File apkFile = new File("source/apk/app-debug.apk");
        File newApkFile = new File(apkFile.getParent() + File.separator + "temp");
        if(!newApkFile.exists()) {
        	newApkFile.mkdirs();
        }
        File mainDexFile = AES.encryptAPKFile(apkFile,newApkFile);
        if (newApkFile.isDirectory()) {
			File[] listFiles = newApkFile.listFiles();
			for (File file : listFiles) {
				if (file.isFile()) {
					if (file.getName().endsWith(".dex")) {
						String name = file.getName();
						System.out.println("rename step1:"+name);
						int cursor = name.indexOf(".dex");
						String newName = file.getParent()+ File.separator + name.substring(0, cursor) + "_" + ".dex";
						System.out.println("rename step2:"+newName);
						file.renameTo(new File(newName));
					}
				}
			}
		}
        
        
    	 /**
         * 第二步 处理aar 获得壳dex
         */
    	File aarFile = new File("source/aar/mylibrary-debug.aar");
        File aarDex  = Dx.jar2Dex(aarFile);
//        aarData = Utils.getBytes(aarDex);   //将dex文件读到byte 数组
        
        
        File tempMainDex = new File(newApkFile.getPath() + File.separator + "classes.dex");
        if (!tempMainDex.exists()) {
			tempMainDex.createNewFile();
		}
//        System.out.println("MyMain" + tempMainDex.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(tempMainDex);
        byte[] fbytes = Utils.getBytes(aarDex);
        fos.write(fbytes);
        fos.flush();
        fos.close();


        /**
         * 第3步 打包签名
         */
        File unsignedApk = new File("result/apk-unsigned.apk");
        unsignedApk.getParentFile().mkdirs();
//        File disFile = new File(apkFile.getAbsolutePath() + File.separator+ "temp");
        Zip.zip(newApkFile, unsignedApk);
        //不用插件就不能自动使用原apk的签名...
        File signedApk = new File("result/apk-signed.apk");
        Signature.signature(unsignedApk, signedApk);
    }

    
	private static File getMainDexFile(File apkFile) {
		// TODO Auto-generated method stub
		File disFile = new File(apkFile.getAbsolutePath() + "unzip");
		Zip.unZip(apkFile, disFile);
		File[] files = disFile.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".dex")) {
					return true;
				}
				return false;
			}
		});
		for (File file: files) {
			if (file.getName().endsWith("classes.dex")) {
				return file;
			}
		}
		return null;
	}
}
