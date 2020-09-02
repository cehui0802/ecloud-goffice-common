package cn.gwssi.ecloudbpm.module.base.utils.query;

import cn.gwssi.ecloudbpm.module.base.model.PageQuery;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;

/**
 * @ClassName PageUtil
 * @Description 分页工具类
 * @Author Pong
 **/

public class QueryFilterUtil {

    /**
     * 构造分页查询过滤器
     * @param pageQuery
     * @return QueryFilter
     */
    public static QueryFilter buildQueryFilter(PageQuery pageQuery) {
        QueryFilter queryFilter = new DefaultQueryFilter(pageQuery.isNoPage());
        queryFilter.setPage(pageQuery);
        return queryFilter;
    }

}
