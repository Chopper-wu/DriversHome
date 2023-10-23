package com.blue.corelib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chopper on 2021/3/25
 * desc : 列表分页返回的实体
 */
public class PageInfoBean<T> implements Serializable {
    private List<T> records;
    private Integer total;
    private Integer size;
    private Integer current;
    private Boolean searchCount;
    private Integer pages;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Boolean getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Boolean searchCount) {
        this.searchCount = searchCount;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
