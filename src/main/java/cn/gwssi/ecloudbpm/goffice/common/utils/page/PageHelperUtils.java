package cn.gwssi.ecloudbpm.goffice.common.utils.page;

import cn.gwssi.ecloudbpm.goffice.common.model.PageQuery;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.util.StringUtils;

/**
 * Title: PageUtils<br>
 * Company:GWSSI</br>
 * CreateDate:2020/09/03 9:47
 *
 * @author Lyric1st
 */


public class PageHelperUtils {

    /**
     * Title: 开启分页和排序<br>
     * Description: startPageAndOrderBy<br>
     * CreateDate: 2020/9/3 11:14<br>
     * @param pageQuery
     * @param defaultOrder 传入""或者null 则不需要排序
     * @return void
     * @category 开启分页和排序
     * @author Lyric1st
     */
    public static void startPageAndOrderBy(PageQuery pageQuery, String defaultOrder) {

        if (ObjectUtil.isNull(pageQuery)) pageQuery = new PageQuery();

        if (!Boolean.TRUE.equals(pageQuery.getNoPage())) {
            // 前端传入需要分页
            PageHelper.startPage(pageQuery.getPageNo(), pageQuery.getPageSize());
        }
        orderBy(pageQuery, defaultOrder);

    }

    private static void orderBy(PageQuery pageQuery, String defaultOrder) {
        String orderBy = "";
        if (!StringUtils.isEmpty(pageQuery.getOrderBy())) {
            orderBy += pageQuery.getOrderBy();
            // 判断前端传入排序规则
            if (!StringUtils.isEmpty(pageQuery.getSort())) {
                orderBy += " " + pageQuery.getSort();
            }
            PageHelper.orderBy(orderBy);
        } else if (!StringUtils.isEmpty(defaultOrder)) {
            // 前端没有传排序规则 并且有默认排序规则
            PageHelper.orderBy(defaultOrder);
        }
    }
}
