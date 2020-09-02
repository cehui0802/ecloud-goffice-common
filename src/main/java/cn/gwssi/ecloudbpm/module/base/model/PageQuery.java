package cn.gwssi.ecloudbpm.module.base.model;

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

    private Integer pageSize;

    private Integer pageNo;

    private String sort;

    private String orderBy;

    private boolean noPage;

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
