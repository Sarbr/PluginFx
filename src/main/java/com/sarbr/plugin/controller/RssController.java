package com.sarbr.plugin.controller;

import com.sarbr.plugin.entity.Rss;
import com.sarbr.plugin.exception.PluginException;
import com.sarbr.plugin.mapper.RssMapper;
import com.sarbr.plugin.utils.AlertHelper;
import com.sarbr.plugin.utils.SpringContextUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FXMLController
public class RssController extends AbsController<Rss> {

    @FXML
    public TextField name;
    @FXML
    public TextField link;
    @FXML
    public ComboBox<String> stateBox;
    @FXML
    public TextField orderNo;
    @FXML
    public TableView<Rss> table;
    @FXML
    public TableColumn<Rss,String> orderCol;
    @FXML
    public TableColumn<Rss, String> nameCol;
    @FXML
    public TableColumn<Rss, String> urlCol;
    @FXML
    public TableColumn<Rss, String> stateCol;

    @Override
    public TableView<Rss> getTable() {
        return table;
    }

    @Override
    public List<TableColumn<Rss, String>> tableColumn() {
        return new ArrayList<>(Arrays.asList(nameCol, urlCol, stateCol));
    }

    @Override
    public Rss create() {
        final String state = stateBox.getSelectionModel().getSelectedItem();
        return new Rss(Integer.parseInt(orderNo.getText()), name.getText(), link.getText(), state);
    }

    @Override
    public boolean validate(){
        if(name.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"错误", "请输入rss名称");
            return true;
        }
        if(link.getText().isEmpty() || !link.getText().startsWith("http")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"错误", "请输入正确的rss链接");
            return true;
        }
        if(stateBox == null || StringUtils.isEmpty(stateBox.getSelectionModel().getSelectedItem())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"错误", "请选择启用状态");
            return true;
        }
        if(orderNo.getText().isEmpty() || !NumberUtils.isCreatable(orderNo.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"错误", "请输入序号");
            return true;
        }
        return false;
    }

    @Override
    public void doDelete(Rss entity) {
        if("启用".equals(entity.getState())){
            AlertHelper.errAlter("启用中的数据不能删除!");
        }else{
            getMapper().delete(entity);
        }
    }

    @Override
    public JpaRepository<Rss, Long> getMapper() {
        return SpringContextUtil.getBean(RssMapper.class);
    }

    @Override
    public void refresh(){
        final MainController mainController = SpringContextUtil.getBean(MainController.class);
        mainController.refreshPage("rss");
    }

    @Override
    public void preSave(String propName, Object newValue) {
        if("state".equals(propName) && (!StringUtils.equalsAny(newValue.toString(), "启用", "禁用") )){
            AlertHelper.errAlter("状态栏只能填写启用或禁用!");
            throw new PluginException("状态栏只能填写启用或禁用");
        }
    }
}
