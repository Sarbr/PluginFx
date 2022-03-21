package com.sarbr.plugin.controller;

import com.sarbr.plugin.entity.SysParam;
import com.sarbr.plugin.mapper.SysParamMapper;
import com.sarbr.plugin.utils.AlertHelper;
import com.sarbr.plugin.utils.SpringContextUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FXMLController
public class SysParamController extends AbsController<SysParam> {

    @FXML
    public TextField title;
    @FXML
    public TextField type;
    @FXML
    public TextField key;
    @FXML
    public TextField val;
    @FXML
    public TextArea note;

    @FXML
    public TableView<SysParam> table;

    @FXML
    public TableColumn<SysParam, String> titleCol;
    @FXML
    public TableColumn<SysParam, String> typeCol;
    @FXML
    public TableColumn<SysParam, String> keyCol;
    @FXML
    public TableColumn<SysParam, String> valCol;
    @FXML
    public TableColumn<SysParam, String> noteCol;

    @Override
    public TableView<SysParam> getTable() {
        return table;
    }

    @Override
    public List<TableColumn<SysParam, String>> tableColumn() {
        return new ArrayList<>(Arrays.asList(titleCol, titleCol, keyCol, valCol, noteCol));
    }

    @Override
    public boolean validate(){
        if(title.getText().isEmpty()) {
            AlertHelper.errAlter("请输入标题");
            return true;
        }
        if(type.getText().isEmpty()) {
            AlertHelper.errAlter("请输入分类");
            return true;
        }
        if(key.getText().isEmpty()) {
            AlertHelper.errAlter("请输入键");
            return true;
        }
        if(val.getText().isEmpty()) {
            AlertHelper.errAlter("请输入值");
            return true;
        }
        return false;
    }

    @Override
    public SysParam create() {
        return new SysParam(title.getText(), type.getText(), key.getText(), val.getText(), note.getText());
    }

    @Override
    public JpaRepository<SysParam, Long> getMapper() {
        return SpringContextUtil.getBean(SysParamMapper.class);
    }

    @Override
    public void refresh() {
        final MainController mainController = SpringContextUtil.getBean(MainController.class);
        mainController.refreshPage("sysParam");
    }
}
