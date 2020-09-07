package cn.gwssi.ecloudbpm.goffice.common.model;

import cn.gwssi.ecloudframework.base.api.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PageQuery
 * @Description 分页参数
 * @Author Pong
 **/
@Data
public class PageQuery implements Page, Serializable {

    private static final long serialVersionUID = -3977360984566680486L;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NO = 1;

    private Integer pageSize = DEFAULT_PAGE_SIZE;

    private Integer pageNo = DEFAULT_PAGE_NO;

    private String sort;

    private String orderBy;

    private Boolean noPage;

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public Integer getTotal() {
        return null;
    }

    @Override
    public Integer getPageNo() {
        return pageNo;
    }

    @Override
    public boolean isShowTotal() {
        return false;
    }

    @Override
    public Integer getStartIndex() {
        return null;
    }
}
