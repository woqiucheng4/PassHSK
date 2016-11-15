package com.qc.corelibrary.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CatchCrash implements UncaughtExceptionHandler {

    public void init(Context context) {
        mDefUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // if(!handleException(ex) && (mDefUncaughtExceptionHandler != null)){
        // mDefUncaughtExceptionHandler.uncaughtException(thread, ex);
        // }else{
        //
        // }
        Log.e("CatchCrash", "uncaughtException");
        handleException(ex);
        mDefUncaughtExceptionHandler.uncaughtException(thread, ex);

        // exit process
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static CatchCrash getInstance() {
        return sInstance;
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            Log.e("CatchCrash", "ex==true");
            return true;
        }
//        saveCrashInfo2File(ex);
        return true;
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        Log.e("CatchCrash", "result==" + sb.toString());
        try {
            long timeStamp = System.currentTimeMillis();
            String time = mFormatter.format(new Date());
            String fileName = "creditease-crash-" + time + "-" + timeStamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + "/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error ocuued while writing....", e);
        } finally {

        }
        return null;
    }

    private static final String TAG = "CatchCrash";
    private UncaughtExceptionHandler mDefUncaughtExceptionHandler;
    private static CatchCrash sInstance = new CatchCrash();
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
}
