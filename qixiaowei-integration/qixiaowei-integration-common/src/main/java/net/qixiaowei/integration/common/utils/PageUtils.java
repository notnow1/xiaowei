package net.qixiaowei.integration.common.utils;

import com.github.pagehelper.PageHelper;
import net.qixiaowei.integration.common.utils.sql.SqlUtil;
import net.qixiaowei.integration.common.web.page.PageDomain;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.page.TableSupport;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }

    /**
     * 返回分页
     *
     * @param code 状态码
     * @param rows 数据
     * @param size 大小
     * @return TableDataInfo
     */
    public static TableDataInfo tableDataInfo(int code, List<?> rows, int size) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(code);
        rspData.setRows(rows);
        rspData.setMsg("查询成功");
        rspData.setTotal(size);
        return rspData;
    }
}
