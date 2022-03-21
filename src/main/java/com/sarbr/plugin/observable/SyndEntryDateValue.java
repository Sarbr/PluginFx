package com.sarbr.plugin.observable;

import com.rometools.rome.feed.synd.SyndEntry;
import com.sarbr.plugin.utils.DateUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Timestamp;

/**
 * 简单处理时间类型展示(date->string)
 * create by : sarbr
 */
public class SyndEntryDateValue<S extends SyndEntry, T> implements Callback<TableColumn.CellDataFeatures<S,T>, ObservableValue<String>> {

    @Override
    public ObservableValue<String> call(TableColumn.CellDataFeatures<S, T> param) {
        return getCellDataReflectively(param.getValue());
    }

    private ObservableValue<String> getCellDataReflectively(S rowData) {
        if (rowData == null || rowData.getPublishedDate() == null) return null;
        return new ReadOnlyObjectWrapper<>(DateUtils.convertTimestampToString(new Timestamp(rowData.getPublishedDate().getTime())));
    }
}
