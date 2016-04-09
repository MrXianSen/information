package com.albery.entity;

/**
 * ����ʵ��ListView��ҳ��Bean��
 *
 * @author Administrator
 */
public class PageBean {

    static final int PAGE_SIZE = 10;

    private int currPage;    //��ǰҳ��

    private int totalRecord;    //��������

    private int totalPage;    //��ҳ��

    private int pageSize; //ÿһҳ����������

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
