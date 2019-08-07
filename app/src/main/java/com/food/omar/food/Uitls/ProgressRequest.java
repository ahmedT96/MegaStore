package com.food.omar.food.Uitls;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.food.omar.food.Home;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogRecord;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequest extends RequestBody{
    File file;
    public static final int Duffult_PUFFER_SIZE=4096;
private  UploadCallBack listener;

    public ProgressRequest(File file, UploadCallBack listener) {
        this.file = file;
        this.listener = listener;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long filelength=file.length();
        byte[] buffer =new byte[Duffult_PUFFER_SIZE];
        FileInputStream in =new FileInputStream(file);
        long uploaded=0;
        try {
            int read;
            Handler handler=new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer))!=-1)
            {
                handler.post(new ProgressUpdater(uploaded,filelength));
                uploaded+=read;
                sink.write(buffer,0,read);
            }
            }finally {
            in.close();
        }
        }

    private class ProgressUpdater implements Runnable {
        private long uploaded,fileLength;
        public ProgressUpdater(long uploaded, long filelength) {
            this.fileLength=filelength;
            this.uploaded=uploaded;
        }

        @Override
        public void run() {
            listener.onProgressUpdate((int)(100*uploaded/fileLength));
        }
    }
}
