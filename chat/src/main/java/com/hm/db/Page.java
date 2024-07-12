package com.hm.db;

import java.util.List;

public class Page<T> {
    private List<?> records;
    private int total;
    private int current;
    private int pages;
    private int size;

    public Page(int countNum, int pageSize, int currentPage) {
        this.total = countNum < 0 ? 0 : countNum;
        this.size = pageSize < 0 ? 0 : pageSize;
        this.current = currentPage < 0 ? 0 : currentPage;
        if (pageSize == 0) {
            this.pages = 0;
        } else {
            this.pages = total % size > 0 ? total / size + 1 : total / size;
        }
    }

    public List<?> getRecords() {
        return records;
    }

    public void setRecords(List<?> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}







