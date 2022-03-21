package com.sarbr.plugin.controller;

import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.sarbr.plugin.Main;
import com.sarbr.plugin.entity.Rss;
import com.sarbr.plugin.entity.SysParam;
import com.sarbr.plugin.mapper.RssMapper;
import com.sarbr.plugin.mapper.SysParamMapper;
import com.sarbr.plugin.support.InitializeDB;
import com.sarbr.plugin.support.RssTask;
import com.sarbr.plugin.support.TableRss;
import com.sarbr.plugin.utils.AlertHelper;
import com.sarbr.plugin.utils.SpringContextUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@FXMLController
public class MainController {
    public static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Resource
    private InitializeDB initializeDB;

    private RssMapper rssMapper;

    private SysParamMapper sysParamMapper;

    @FXML
    private Menu urlMenu;
    @FXML
    private AnchorPane content;
    @FXML
    public VBox mainPane;

    @FXML
    public TableView<SyndEntryImpl> tableRss1;
    @FXML
    public TableView<SyndEntryImpl> tableRss2;
    @FXML
    public TableView<SyndEntryImpl> tableRss3;
    @FXML
    public TableView<SyndEntryImpl> tableRss4;

    @FXML
    public Label label1;
    @FXML
    public Label label2;
    @FXML
    public Label label3;
    @FXML
    public Label label4;

    private final List<TableRss> tableRssList = new ArrayList<>();

    private void initMapper() {
        if(rssMapper == null){
            rssMapper = SpringContextUtil.getBean(RssMapper.class);
        }
        if(sysParamMapper == null){
            sysParamMapper = SpringContextUtil.getBean(SysParamMapper.class);
        }
    }

    public void initialize() {
        this.initMapper();
        this.initUrlMenu();
        this.tableRssList.add(new TableRss(label1, tableRss1));
        this.tableRssList.add(new TableRss(label2, tableRss2));
        this.tableRssList.add(new TableRss(label3, tableRss3));
        this.tableRssList.add(new TableRss(label4, tableRss4));
        this.initRss();
    }

    private void initUrlMenu() {
        List<SysParam> sysParamList = new ArrayList<>();
        try {
            sysParamList.addAll(sysParamMapper.getByType("链接"));
        } catch (Exception e) {
            initializeDB.createTable();
        }
        final int size = sysParamList.size();
        if(0 == size){
            sysParamList = initializeDB.createByDataJson("sysParam", SysParam.class);
            sysParamMapper.saveAll(sysParamList);
        }
        sysParamList.forEach(sysParam -> {
            final MenuItem menuItem = new MenuItem(sysParam.getTitle());
            menuItem.setId(sysParam.getKey());
            menuItem.setOnAction(this::toUrl);
            urlMenu.getItems().add(menuItem);
        });
    }

    private void initRss() {
        long startTime = System.currentTimeMillis();
        final List<Rss> rssList = initializeDB() ;
        final int taskSize = rssList.size();
        List<RssTask> allTasks = new ArrayList<>(taskSize);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(taskSize,()->
                logger.info("初始化完成,执行时间 ==> {}ms,线程:{}.",
                        (System.currentTimeMillis() - startTime), Thread.currentThread().getName()));
        for(int i=0; i < rssList.size(); i++){
            final Label label = getLabel(i);
            final TableView<SyndEntryImpl> table = getTable(i);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            Rss rss = rssList.get(i);
            label.setText((i+1) + "." + rss.getName());
            //多线程处理加快访问速度
            allTasks.add(new RssTask(rss, table, cyclicBarrier));
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(taskSize);
        allTasks.forEach(threadPool::submit);
        threadPool.shutdown();
    }

    /**
     * 如果为查询到rss数据则进行初始化
     */
    private List<Rss> initializeDB() {
        List<Rss> rssList = new ArrayList<>();
        try {
            rssList.addAll(rssMapper.getSelectedRss("启用"));
        } catch (Exception e) {
            initializeDB.createTable();
        }
        final int size = rssList.size();
        if(0 == size){
            rssList = initializeDB.createByDataJson("rss", Rss.class);
            rssMapper.saveAll(rssList);
            return rssList.stream().filter(rss -> "启用".equals(rss.getState())).collect(Collectors.toList());
        }
        return rssList;
    }

    private Label getLabel(int index){
        return tableRssList.get(index).getLabel();
    }

    private TableView<SyndEntryImpl> getTable(int index){
        return tableRssList.get(index).getTableRss();
    }

    public void toPage(ActionEvent actionEvent) {
        final MenuItem source = (MenuItem)actionEvent.getSource();
        final String page = source.getId();
        if("main".equals(page))
            refreshPage(page, mainPane);
        else
            refreshPage(page, content);
    }

    public void about(ActionEvent actionEvent) {
        final MenuItem source = (MenuItem)actionEvent.getSource();
        final String title = source.getId();
        AlertHelper.defAlter(title, "https://github.com/Sarbr/PluginFX");
    }

    public void refreshPage(String page){
        try {
            content.getChildren().clear();
            final Parent parent = Main.loadFXML("fxml/" + page + ".fxml");
            content.getChildren().add(parent);
        } catch (Exception e) {
            logger.error("切换功能出错", e);
        }
    }

    private void refreshPage(String page, Pane pane){
        try {
            pane.getChildren().clear();
            final Parent parent = Main.loadFXML("fxml/" + page + ".fxml");
            pane.getChildren().add(parent);
        } catch (Exception e) {
            logger.error("切换功能出错", e);
        }
    }

    public void toUrl(ActionEvent actionEvent) {
        final MenuItem source = (MenuItem)actionEvent.getSource();
        final String code = source.getId();
        String url = sysParamMapper.getByCode(code);
        try {
            Assert.isTrue(StringUtils.isNotEmpty(url),"未找到参数对应的链接信息");
            content.getChildren().clear();
            WebView webView = new WebView();
            webView.setPrefWidth(1358.0);
            webView.setPrefHeight(750.0);
            webView.getEngine().load(url);
            content.getChildren().add(webView);
        } catch (Exception e) {
            AlertHelper.errAlter(e.getMessage());
        }
    }
}