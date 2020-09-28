package cn.gwssi.ecloudbpm.goffice.common.utils.page;

import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
import com.github.pagehelper.PageInfo;

/**
 * @author Pong weipengxiang@gwssi.com.cn
 * @date 2020/9/28 14:56
 **/

public class PageResultUtil {

    /**
     * 使用PageHelper分页插件查询后,由于对查询数据进行处理,会导致分页信息丢失,
     * 因此查询完毕后先保存PageInfo信息,再对数据进行处理,处理完成后调用此方法
     * 返回PageResult,保证分页数据的完整性
     */
    public static <T> PageResult<T> pageResultHandler(PageInfo pageInfo, PageResult<T> pageResult) {
        pageResult.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setPage(pageInfo.getPageNum());
        return pageResult;
    }
}
