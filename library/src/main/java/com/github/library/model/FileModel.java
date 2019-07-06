package com.github.library.model;

import android.content.Context;
import android.net.Uri;

import com.github.library.RestClientHelper;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;

public class FileModel {

    byte[] file;
    String fileName;
    MediaType mimeType;

    public FileModel(Context context, File file) throws IOException {
        WeakReference<Context> weakContext = new WeakReference<>(context);
        setFileName(RestClientHelper.getFileName(file.getAbsolutePath()));
        setFile(RestClientHelper.ImageToBytes(file));
        setMimeType(MediaType.parse(RestClientHelper.getMimeType(weakContext, Uri.fromFile(file))));
    }

    public FileModel(byte[] file, MediaType mimeType, String fileName) {
        this.file = file;
        this.mimeType = mimeType;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public MediaType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MediaType mimeType) {
        this.mimeType = mimeType;
    }
}
