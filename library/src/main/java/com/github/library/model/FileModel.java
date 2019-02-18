package com.github.library.model;

import android.provider.MediaStore;

import okhttp3.MediaType;

public class FileModel {

    byte[] file;

    MediaType mimeType;

    String filenName;

    public FileModel(byte[] file, MediaType mimeType, String filenName) {
        this.file = file;
        this.mimeType = mimeType;
        this.filenName = filenName;
    }

    public String getFilenName() {
        return filenName;
    }

    public void setFilenName(String filenName) {
        this.filenName = filenName;
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
