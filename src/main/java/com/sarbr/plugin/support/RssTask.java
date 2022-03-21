package com.sarbr.plugin.support;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.sarbr.plugin.entity.Rss;
import com.sarbr.plugin.utils.AlertHelper;
import com.sarbr.plugin.utils.RssUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * rss多线程处理组装过程
 * @author Sarbr
 */
public class RssTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(RssTask.class);
    private final Rss rss;
    private final TableView<SyndEntryImpl> table;

    private final CyclicBarrier cyclicBarrier;

    public RssTask(Rss rss, TableView<SyndEntryImpl> table, CyclicBarrier cyclicBarrier) {
        this.rss = rss;
        this.table = table;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            ObservableList<SyndEntryImpl> teamMembers = FXCollections.observableArrayList();
            final List<SyndEntryImpl> entries = RssUtils.parseXml(rss.getUrl());
            for (int i = 0; i < entries.size(); i++) {
                SyndEntryImpl e = entries.get(i);
                if(!e.getLink().startsWith("http")){
                    //原文链接不规范的不予展示
                    continue;
                }
                e.setComments((i+1)+"");
                teamMembers.add(e);
            }
            table.setItems(teamMembers);
            table.setRowFactory(param -> {
                TableRow<SyndEntryImpl> row = new TableRow<>();
                row.applyCss();
                row.autosize();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! row.isEmpty()) ){
                        SyndEntry data = row.getItem();
                        WebView webView = new WebView();
                        webView.getEngine().loadContent(data.getDescription().getValue() + "<p><a href='" + data.getLink()+ "' target='_self'>原文链接</a></p>");
                        AlertHelper.openWindow(data.getTitle(), 1000, 800, webView);
                    }
                });
                return row;
            });
            cyclicBarrier.await();
        } catch (Exception e) {
            logger.error(rss.getName() + "：初始化出错", e.getCause());
        }
    }
}
