package com.sarbr.plugin.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *  系统参数设置
 * @author  sarbr
 */
@Entity
@Table(name="sys_param")
public class SysParam extends BaseEntity {

    private String type;

    private String title;

    private String key;

    private String val;

    private String note;

    public SysParam() {
        super();
    }

    public SysParam(String title, String key) {
        this.title = title;
        this.key = key;
    }

    public SysParam(String title, String type, String key, String val, String note) {
        this.title = title;
        this.type = type;
        this.key = key;
        this.val = val;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
