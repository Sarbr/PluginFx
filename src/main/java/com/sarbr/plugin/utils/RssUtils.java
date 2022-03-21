package com.sarbr.plugin.utils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 利用jdom2和rome解析rss数据
 * @author  sarbr
 */
public final class RssUtils {

    public static Logger logger = LoggerFactory.getLogger(RssUtils.class);

    public static <T extends SyndEntry> List<T> parseXml(String url){
        try {
            final RestTemplate restTemplate = SpringContextUtil.getBean("restTemplate");
            final ResponseEntity<String> requestEntity = restTemplate.getForEntity(url, String.class);
            String xml = StringUtils.defaultString(requestEntity.getBody());
            SyndFeedInput input = new SyndFeedInput();
            Document document = new SAXBuilder().build(new InputSource(new StringReader(xml)));
            SyndFeedImpl feed = (SyndFeedImpl)input.build(document);
            return (List<T>)feed.getEntries();
        } catch (Exception e) {
            logger.error("链接:"+ url + ",失败:" + e.getMessage() +"!");
            return new ArrayList<>();
        }
    }
}
