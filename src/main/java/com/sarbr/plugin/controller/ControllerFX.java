package com.sarbr.plugin.controller;

import com.sarbr.plugin.entity.BaseEntity;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ControllerFX<E extends BaseEntity> {

    TableView<E> getTable();

    List<TableColumn<E, String>> tableColumn();

    JpaRepository<E, ?> getMapper();

    void refresh();

    boolean validate();

    E create();

    default void preSave(String propName, Object newValue) {

    }

    default void doDelete(E entity){
        getMapper().delete(entity);
    }

     default void editColumn(TableColumn<E, String> column) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setEditable(true);
        final String propName = column.getId();
        column.setOnEditCommit((TableColumn.CellEditEvent<E, String> t) -> {
            final E rss = t.getTableView().getItems().get(
                    t.getTablePosition().getRow());
            try {
                preSave(propName, t.getNewValue());
                PropertyUtils.setProperty(rss, propName, t.getNewValue());
                getMapper().save(rss);
                refresh();
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
            }
        });
    }

}
