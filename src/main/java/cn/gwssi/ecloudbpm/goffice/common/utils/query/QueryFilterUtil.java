package cn.gwssi.ecloudbpm.goffice.common.utils.query;

import cn.gwssi.ecloudbpm.goffice.common.model.PageQuery;
import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
import org.springframework.util.ObjectUtils;

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

        boolean page = false;

        if (!ObjectUtils.isEmpty(pageQuery)
                && !ObjectUtils.isEmpty(pageQuery.getNoPage())) {
            page = pageQuery.getNoPage();
        }

        QueryFilter queryFilter = new DefaultQueryFilter(page);
        queryFilter.setPage(pageQuery);
        return queryFilter;
    }

}
