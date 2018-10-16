package ir.vasl.library.utils;


import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;


public class RequestBodyProgressive extends RequestBody {

    private long bytesWritten = 0;

    private Listener listener;

    private RequestBody delegate;

    private int SEND_WINDOW_SIZE_BYTES = (int) Math.pow(2, 16); // 64 KiB

    public RequestBodyProgressive(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull final BufferedSink sink) throws IOException {
        Sink streamingSink = new ForwardingSink(sink) {
            @Override
            public void write(@NonNull Buffer source, long byteCount) throws IOException {
                long written = 0L;
                while (written < byteCount) {
                    long writeLength = Math.min(SEND_WINDOW_SIZE_BYTES, byteCount - written);
                    sink.write(source.readByteArray(writeLength));
                    written      += writeLength;
                    bytesWritten += writeLength;
                    listener.onRequestProgress(bytesWritten, contentLength());
                }
            }
        };
        BufferedSink bufferedSink = Okio.buffer(streamingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    public interface Listener {
        void onRequestProgress(long bytesWritten, long contentLength);
    }
}