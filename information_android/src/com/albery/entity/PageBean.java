package com.albery.entity;

/**
 * 用来实现ListView分页的Bean类
 *
 * @author Administrator
 */
public class PageBean {

    static final int PAGE_SIZE = 10;

    private int currPage;    //当前页码

    private int totalRecord;    //数据总数

    private int totalPage;    //总页数

    private int pageSize; //每一页的数据条数

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {

        totalPage = totalRecord % PAGE_SIZE;

        if (totalPage != 0) {
            totalPage = totalRecord / PAGE_SIZE + 1;
        } else {
            totalPage = totalRecord / PAGE_SIZE;
        }
        return totalPage;
    }

    public int getPageSize() {
        this.pageSize = PAGE_SIZE;
        return this.pageSize;
    }

}
