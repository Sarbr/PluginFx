package com.sarbr.plugin.support;

import com.rometools.rome.feed.synd.SyndEntryImpl;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * 描述label和tableView
 * create by : sarbr
 */
public class TableRss {

    public Label label;
    public TableView<SyndEntryImpl> tableRss;

    public TableRss(Label label, TableView<SyndEntryImpl> tableRss) {
        this.label = label;
        this.tableRss = tableRss;
    }

    public Label getLabel() {
        return label;
    }

    public TableView<SyndEntryImpl> getTableRss() {
        return tableRss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableRss)) return false;

        TableRss tableRss1 = (TableRss) o;

        if (!label.equals(tableRss1.label)) return false;
        return tableRss.getId().equals(tableRss1.tableRss.getId());
    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + tableRss.hashCode();
        return result;
    }
}
