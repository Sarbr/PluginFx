package com.sarbr.plugin.entity;

import javax.persistence.*;

/**
 *  rss配置类
 * @author  sarbr
 */
@Entity
@Table(name="Rss")
public class Rss extends BaseEntity{

    private Integer orderNo;

    private String name;

    private String url;

    private String state;

    public Rss() {
    }

    public Rss(Integer orderNo, String name, String url, String state) {
        this.orderNo = orderNo;
        this.name = name;
        this.url = url;
        this.state = state;
    }

    @Column(name= "order_no")
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Rss{" +
                "orderNo=" + orderNo +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
