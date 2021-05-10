package com.arch.demo.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.arch.demo.core.preference.PreferencesUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Logger {
    private static boolean sEnabled = false;
    private static final String sDefaultTag = "Main";

    private static Context sContext;

    public static final String DB_CACHE_DATA_LOG = "db_cache.txt";

    private static final String FIRST_LOG_FILE_NAME_MAIN = "log1.txt";
    private static final String SECOND_LOG_FILE_NAME_MAIN = "log2.txt";
    private static final String SP_KEY_CUR_LOG_FILE_FLAG_MAIN = "cur_log_file_flag";

    private static final String FIRST_LOG_FILE_NAME_CHILD = "log1_child.txt";
    private static final String SECOND_LOG_FILE_NAME_CHILD = "log2_child.txt";
    private static final String SP_KEY_CUR_LOG_FILE_FLAG_CHILD = "cur_log_file_flag_child";

    private static boolean isMainProcess = true;
    private static RandomAccessFile mLogRandomAccessFile;
    private static final long MAX_FILE_LENGTH = 1 * 1024 * 1024; //单个文件限制大小
    private static final int CHECK_SIZE_TIMES = 10; //每写入10次，检查一下文件大小是否超出限制
    private static String FIRST_FILE_NAME = FIRST_LOG_FILE_NAME_MAIN; //第一个日志文件的名称
    private static String SECOND_FILE_NAME = SECOND_LOG_FILE_NAME_MAIN; //第二个日志文件的名称
    private static String SP_KEY_CUR_LOG_FILE_FLAG = SP_KEY_CUR_LOG_FILE_FLAG_MAIN; //存储当前操作的文件标识的key
    private static int curLogFileFlag = 0; //当前操作的文件标志位，0-log1.txt  1-log2.txt;
    private static long curWriteTime = 0; //当前已写入次数

    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    public static void setEnabled(boolean enabled) {
        sEnabled = enabled;
    }

    public static void v(String msg) {
        if (sEnabled) {
            Log.v(sDefaultTag, "" + msg);
        }
    }

    public static void v(String tag, String msg) {
        if (sEnabled) {
            Log.v(tag, "" + msg);
        }
    }

    public static void d(String msg) {
        if (sEnabled) {
            Log.d(sDefaultTag, "" + msg);
            writeDisk(sDefaultTag, "" + msg, "D");
        }
    }

    public static void d(String tag, String msg) {
        if (sEnabled) {
            Log.d(tag, "" + msg);
            writeDisk(tag, "" + msg, "D");
        }
    }

    public static void e(String tag, Throwable t) {
        if (sEnabled) {
            Log.e(tag, "exception", t);
        }
        writeDisk(tag, "exception:" + Log.getStackTraceString(t), "E");
    }

    public static void e(String tag, String msg, Throwable t) {
        if (sEnabled) {
            Log.e(tag, "" + msg, t);
        }
        writeDisk(tag, msg + ":" + Log.getStackTraceString(t), "E");
    }

    public static void e(String tag, String msg) {
        if (sEnabled) {
            Log.e(tag, "" + msg);
        }
        writeDisk(tag, msg, "E");
    }

    public static void i(String tag, String msg) {
        if (sEnabled) {
            Log.i(tag, "" + msg);
        }
        writeDisk(tag, msg, "I");
    }

    public static void i(String msg) {
        if (sEnabled) {
            Log.i(sDefaultTag, "" + msg);
        }
        writeDisk("Common", msg, "I");
    }

    public static void printInstance(Object obj, String msg) {
        if (obj != null) {
            i(String.format("   %s@%s: %s", obj.getClass().getSimpleName(),
                    Integer.toHexString(obj.hashCode()), msg));
        }
    }

    public static void w(String tag, String msg) {
        if (sEnabled) {
            Log.w(tag, "" + msg);
        }
        writeDisk(tag, msg, "W");
    }

    /**
     * 获取日志文件目录
     * 如果不存在，则创建目录
     */
    public static File getDirFile(Context context) {
        if (context == null) {
            return null;
        }
        String basePath = context.getFilesDir().getAbsolutePath();
        File file = new File(basePath + File.separator + "log");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除老的日志文件
     */
    private static void tryDeleteOldFile(Context context) {
        File oldFile = context.getExternalFilesDir("log");
        if (oldFile != null && oldFile.exists()) {
            oldFile.deleteOnExit();
        }
    }

    public static void init(Context context, boolean isDebug) {
        if (context != null) {
            sContext = context;
            setEnabled(isDebug);
            isMainProcess = TextUtils.equals(getProcessName(context), context.getPackageName());
            if (!isMainProcess) { //非主进程使用另外一套文件
                FIRST_FILE_NAME = FIRST_LOG_FILE_NAME_CHILD;
                SECOND_FILE_NAME = SECOND_LOG_FILE_NAME_CHILD;
                SP_KEY_CUR_LOG_FILE_FLAG = SP_KEY_CUR_LOG_FILE_FLAG_CHILD;
            }
            LoggerTaskThread loggerTaskThread = new LoggerTaskThread();
            loggerTaskThread.setName("WebullLogger");
            loggerTaskThread.start();
        }
        d("appinit", "app start");
    }


    /**
     * 获取进程名
     *
     * @param context
     * @return
     */
    private static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }


    private static String getTime() {
        return sDateFormat.format(new Date(System.currentTimeMillis()));
    }

    private static void writeDisk(String tag, String message, String level) {
        boolean ret = cacheList.offer(new StringBuilder().append("[pid:").append(android.os.Process.myPid()).append("][")
                .append(getTime()).append(": ")
                .append(level).append("/")
                .append(tag).append("]")
                .append("【mainThread:").append(isMainThread()).append("】")
                .append(message).toString());
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private static void doWriteDisk(String msg) {
        try {
            if (mLogRandomAccessFile != null) {
                byte[] msgByte = msg.getBytes("UTF-8");
                if (curWriteTime % CHECK_SIZE_TIMES == 0) {
                    if (mLogRandomAccessFile.length() + msgByte.length > MAX_FILE_LENGTH) {
                        //切换文件
                        curLogFileFlag = curLogFileFlag == 0 ? 1 : 0;
                        switchLogRandomAccessFile(curLogFileFlag == 0 ? FIRST_FILE_NAME : SECOND_FILE_NAME);
                        PreferencesUtil.getInstance().setInt(SP_KEY_CUR_LOG_FILE_FLAG, curLogFileFlag);
                    }
                    curWriteTime = 0;
                }
                mLogRandomAccessFile.write(msgByte);
                mLogRandomAccessFile.writeBytes("\r\n");
                curWriteTime++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mLogRandomAccessFile = null;
        }
    }

    private static void switchLogRandomAccessFile(String fileName) {
        if (sContext == null) {
            return;
        }
        File file = getDirFile(sContext);
        try {
            File newLogFile = new File(file.getAbsolutePath() + File.separator + fileName);
            if (newLogFile.exists()) {
                newLogFile.delete();
            }
            newLogFile.createNewFile();
            mLogRandomAccessFile = new RandomAccessFile(newLogFile, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     *
     * @param resFile  需要压缩的文件（夹）
     * @param zipout   压缩的目的文件
     * @param rootpath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException           当压缩过程出错时抛出
     */
    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
            throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte buffer[] = new byte[1024];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile), 1024);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }

    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile     生成的压缩文件
     * @param comment     压缩文件的注释
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment)
            throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), 1024));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.setComment(comment);
        zipout.close();
    }

    public static String getZipLoggerFileWithLogcat(String logcatFilePath, boolean isWebull) {
        try {
            File rootFile = getDirFile(sContext);
            if (rootFile != null && rootFile.exists() && rootFile.isDirectory()) {
                File zipFile = new File(rootFile.getAbsolutePath() + File.separator + "log.zip");
                if (zipFile.exists()) {
                    zipFile.delete();
                }
                ArrayList<File> files = new ArrayList<>();

                if (!TextUtils.isEmpty(logcatFilePath)) {
                    File logcatFile = new File(logcatFilePath);
                    if (logcatFile.exists()) {
                        files.add(logcatFile);
                        d("add logcat file to zipfile list");
                    }
                }

                File firstLogFile = new File(rootFile.getAbsolutePath() + File.separator + FIRST_LOG_FILE_NAME_MAIN);
                if (firstLogFile.exists()) {
                    files.add(firstLogFile);
                    d("add firstLogFile file to zipfile list");
                }

                File secondLogFile = new File(rootFile.getAbsolutePath() + File.separator + SECOND_LOG_FILE_NAME_MAIN);
                if (secondLogFile.exists()) {
                    files.add(secondLogFile);
                    d("add secondLogFile file to zipfile list");
                }


                File otherFirstLogFile = new File(rootFile.getAbsolutePath() + File.separator + FIRST_LOG_FILE_NAME_CHILD);
                if (otherFirstLogFile.exists()) {
                    files.add(otherFirstLogFile);
                    d("add firstLogFile file to zipfile list");
                }

                File otherSecondLogFile = new File(rootFile.getAbsolutePath() + File.separator + SECOND_LOG_FILE_NAME_CHILD);
                if (otherSecondLogFile.exists()) {
                    files.add(otherSecondLogFile);
                    d("add secondLogFile file to zipfile list");
                }

                File dbFile = sContext.getDatabasePath("stocks");
                if (dbFile != null && dbFile.exists()) {
                    files.add(dbFile);
                    d("add dbFile file to zipfile list");
                }

                ///data/data/org.dayup.stocks/shared_prefs/org.dayup.stocks_preferences.xml
                String spFilePath = "";
                if (isWebull) {
                    spFilePath = sContext.getFilesDir().getParent() + "/shared_prefs/com.webull.trade_preferences.xml";
                } else {
                    spFilePath = sContext.getFilesDir().getParent() + "/shared_prefs/org.dayup.stocks_preferences.xml";
                }

                File spFile = new File(spFilePath);
                if (spFile != null && spFile.exists()) {
                    files.add(spFile);
                    d("add spFile file to zipfile list");
                }

                File file = new File(rootFile.getAbsolutePath() + File.separator + DB_CACHE_DATA_LOG);
                if (file.exists()) {
                    files.add(file);
                }

                zipFiles(files, zipFile, "");
                return zipFile.getAbsolutePath();
            }
        } catch (Exception e) {
            Log.d("zipFile error:",  e.getMessage());
        }
        return "";
    }

    /**
     * 读取最后一段logger   最多300行
     */
    public static ArrayList<String> getLastLogger() {
        long MAX_SIZE = 50 * 1024;
        ArrayList<String> data = new ArrayList<>();
        File rootFile = getDirFile(sContext);
        RandomAccessFile randomAccessFile = null, lastRandomAccessFile = null;
        try {
            int curFileFlag = PreferencesUtil.getInstance().getInt(SP_KEY_CUR_LOG_FILE_FLAG, 0);
            String fileName = curFileFlag == 0 ? FIRST_FILE_NAME : SECOND_FILE_NAME;
            File curLogFile = new File(rootFile.getAbsolutePath() + File.separator + fileName);
            if (!curLogFile.exists()) {
                return data;
            }
            randomAccessFile = new RandomAccessFile(curLogFile, "rw");
            //是否需要读取上一个文件
            if (randomAccessFile.length() < MAX_SIZE) {
                String lastFileName = curFileFlag != 0 ? FIRST_FILE_NAME : SECOND_FILE_NAME;
                File lastLogFile = new File(rootFile.getAbsolutePath() + File.separator + lastFileName);
                if (lastLogFile.exists()) {
                    long lastFileReadSize = MAX_SIZE - randomAccessFile.length();
                    lastRandomAccessFile = new RandomAccessFile(lastLogFile, "rw");
                    lastRandomAccessFile.seek(Math.max(0, lastRandomAccessFile.length() - lastFileReadSize));
                    while (lastRandomAccessFile.getFilePointer() < lastRandomAccessFile.length()) {
                        data.add(lastRandomAccessFile.readLine());
                    }
                }
            }

            randomAccessFile.seek(Math.max(0, randomAccessFile.length() - MAX_SIZE));
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                data.add(randomAccessFile.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (randomAccessFile != null) {
                    lastRandomAccessFile.close();
                }
            } catch (Exception e) {

            }
        }
        return data;
    }

    private static LinkedBlockingQueue<String> cacheList = new LinkedBlockingQueue<>();

    private static class LoggerTaskThread extends Thread {
        @Override
        public void run() {
            try {
                tryDeleteOldFile(sContext);
                File rootDir = getDirFile(sContext);
                try {
                    curLogFileFlag = PreferencesUtil.getInstance().getInt(SP_KEY_CUR_LOG_FILE_FLAG, 0);
                    String fileName = curLogFileFlag == 0 ? FIRST_FILE_NAME : SECOND_FILE_NAME;
                    File newLogFile = new File(rootDir.getAbsolutePath() + File.separator + fileName);
                    if (!newLogFile.exists()) {
                        newLogFile.createNewFile();
                    }
                    mLogRandomAccessFile = new RandomAccessFile(newLogFile, "rw");
                    mLogRandomAccessFile.seek(mLogRandomAccessFile.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                while (!Thread.interrupted()) {
                    doWriteDisk(cacheList.take());
                }
            } catch (Exception e) {

            }
        }
    }
}
