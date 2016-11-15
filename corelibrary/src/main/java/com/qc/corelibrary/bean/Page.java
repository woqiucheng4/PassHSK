package com.qc.corelibrary.bean;

/**
 * <ul>
 * <li>功能职责：数据分页类</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2016-10-31
 */
public class Page {
    private int pageSize;
    private int nowPage;
    private int totalPage;
    private int dataCount;

    /**
     * @return the pageSize
     */
    public final int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public final void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the nowPage
     */
    public final int getNowPage() {
        return nowPage;
    }

    /**
     * @param nowPage the nowPage to set
     */
    public final void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    /**
     * @return the totalPage
     */
    public final int getTotalPage() {
        return totalPage;
    }

    /**
     * @param totalPage the totalPage to set
     */
    public final void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * @return the dataCount
     */
    public final int getDataCount() {
        return dataCount;
    }

    /**
     * @param dataCount the dataCount to set
     */
    public final void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Page:");
        sb.append("每页数据量=" + pageSize);
        sb.append("; ");
        sb.append("当前页=" + nowPage);
        sb.append("; ");
        sb.append("总页数=" + totalPage);
        sb.append("; ");
        sb.append("总数据量=" + dataCount);
        return sb.toString();
    }
}