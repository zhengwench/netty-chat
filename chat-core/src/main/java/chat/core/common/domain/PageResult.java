package chat.core.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/6 17:04
 */
public class PageResult<T> implements Serializable {
    private List<T> data;
    /**
     * 总条数
     */
    private long iTotalRecords;
    /**
     * 过滤条件后查询的总数
     */
    private long iTotalDisplayRecords;
    /**
     *
     */
    private int iDisplayLength;

    // 开始显示行
    private int iDisplayStart;


    /**
     * 前台分页条件*
     */

    //总页数
    private int totalPageCount;
    //页码
    private int pageNo;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(long iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
