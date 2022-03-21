package com.sarbr.plugin.controller;

import com.sarbr.plugin.entity.BaseEntity;
import com.sarbr.plugin.utils.AlertHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.util.HashSet;
import java.util.Set;

public abstract class AbsController<E extends BaseEntity> implements ControllerFX<E> {
    private final Set<E> dataRemove = new HashSet<>();

    public TableColumn<E, Boolean> selectCol;

    public void initialize() {
        final TableView<E> table = getTable();
        selectCol.setCellFactory(param -> new CheckBoxTableCell<>(param1 -> {
            ObservableValue<Boolean> ref = new SimpleBooleanProperty();
            ref.addListener((observable, oldValue, newValue) -> {
                final E e = selectCol.getTableView().getItems().get(param1);
                if(newValue){
                    dataRemove.add(e) ;
                }else{
                    dataRemove.remove(e);
                }
            });
            return ref;
        }));
        tableColumn().forEach(this::editColumn);
        ObservableList<E> teamMembers = FXCollections.observableArrayList();
        teamMembers.addAll(getMapper().findAll());
        table.setItems(teamMembers);
        table.autosize();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void delete(ActionEvent actionEvent) {
        if(!dataRemove.isEmpty()){
            dataRemove.forEach(this::doDelete);
            dataRemove.clear();
            refresh();
        }else{
            AlertHelper.errAlter("请选择需要删除的数据!");
        }
    }

    public void save(ActionEvent actionEvent) {
        if(validate()){
            return;
        }
        getMapper().save(create());
        this.refresh();
    }
}
