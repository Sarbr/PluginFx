package com.sarbr.plugin.app.fastDownload.extractor;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface DownloadProgressExtractor<T> extends ResponseExtractor<T> {

    /**
     * 计算下载进度
     *
     * @param contentLength 文件总大小
     */
    default void calculateDownloadProgress(long contentLength){
        CompletableFuture.runAsync(() -> {
            long tmp = 0;
            while (contentLength - tmp > 0) {
                tmp = getAlreadyDownloadLength();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).exceptionally(e->{
            e.printStackTrace();
            return null;
        });
    }

    /**
     * 返回已下载的字节数
     *
     * @return
     */
    long getAlreadyDownloadLength();

    @Override
    default T extractData(ClientHttpResponse response) throws IOException {
        long contentLength = response.getHeaders().getContentLength();
        this.calculateDownloadProgress(contentLength);
        return this.doExtractData(response);
    }

    T doExtractData(ClientHttpResponse response) throws IOException;

}
