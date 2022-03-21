package com.sarbr.plugin.app.fastDownload.download;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.UUID;

public interface Download {

    RestTemplate getRestTemplate();

    void setRestTemplate(RestTemplate restTemplate);

    void doDownload(String decodeFileURL, String dir, String fileName, HttpHeaders headers) throws IOException;

    default String download(String fileURL, String dir) throws IOException {
        long start = System.currentTimeMillis();
        String decodeFileURL = URLDecoder.decode(fileURL, "UTF-8");
        //通过Http协议的Head方法获取到文件的总大小
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> entity = getRestTemplate().exchange(decodeFileURL, HttpMethod.HEAD, requestEntity, String.class);
        String fileName = this.getFileName(decodeFileURL, entity.getHeaders());
        doDownload(decodeFileURL, dir, fileName, entity.getHeaders());
        return "总共下载文件耗时:" + (System.currentTimeMillis() - start) / 1000 + "s";
    }


    /**
     * 获取文件的名称
     *
     * @return String
     */
    default String getFileName(String fileURL, HttpHeaders headers) {
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
        if (fileName.contains(".")) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (suffix.length() > 4 || suffix.contains("?")) {
                fileName = getFileNameFromHeader(headers);
            }
        } else {
            fileName = getFileNameFromHeader(headers);
        }
        return fileName;
    }

    default String getFileNameFromHeader(HttpHeaders headers) {
        String fileName = headers.getContentDisposition().getFilename();
        if (StringUtils.isEmpty(fileName)) {
            return UUID.randomUUID().toString();
        }
        return fileName;
    }
}
