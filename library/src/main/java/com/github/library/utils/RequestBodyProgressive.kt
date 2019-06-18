package com.github.library.utils


import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Okio
import java.io.IOException


class RequestBodyProgressive(private val delegate: RequestBody, private val listener: (bytesWritten: Long, contentLength: Long) -> Unit) : RequestBody() {

    private var bytesWritten: Long = 0

    private val sendWindowsByteSize = Math.pow(2.0, 16.0).toInt() // 64 KiB

    override fun contentType(): MediaType? {
        return delegate.contentType()
    }

    override fun contentLength(): Long {
        try {
            return delegate.contentLength()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return -1
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val streamingSink = object : ForwardingSink(sink) {
            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                var written = 0L
                while (written < byteCount) {
                    val writeLength = Math.min(sendWindowsByteSize.toLong(), byteCount - written)
                    sink.write(source.readByteArray(writeLength))
                    written += writeLength
                    bytesWritten += writeLength
                    listener(bytesWritten, contentLength())
                }
            }
        }
        val bufferedSink = Okio.buffer(streamingSink)
        delegate.writeTo(bufferedSink)
        bufferedSink.flush()
    }
}