package com.sarbr.plugin.app.fastDownload.extractor;

import org.springframework.http.client.ClientHttpResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileProgressExtractor implements DownloadProgressExtractor<File> {
    /**
     * 已下载的字节数
     */
    private long byteCount;
    /**
     * 文件的路径
     */
    private final String filePath;

    public FileProgressExtractor(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public File doExtractData(ClientHttpResponse response) throws IOException {
        InputStream in = response.getBody();
        File file = new File(filePath);
        FileOutputStream out = new FileOutputStream(file);
        int bytesRead;
        for (byte[] buffer = new byte[4096]; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();
        out.close();
        return file;
    }

    @Override
    public long getAlreadyDownloadLength() {
        return byteCount;
    }
}
