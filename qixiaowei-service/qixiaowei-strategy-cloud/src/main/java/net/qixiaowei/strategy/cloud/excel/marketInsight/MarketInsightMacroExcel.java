package net.qixiaowei.strategy.cloud.excel.marketInsight;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 市场洞察宏观表
 *
 * @author TANGMICHI
 * @since 2023-02-28
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class MarketInsightMacroExcel {

    private static final long serialVersionUID = 1L;


    /**
     * 规划年度
     */

    @ExcelProperty("规划年度")
    private String planYear;
    /**
     * 视角
     */

    @ExcelProperty("视角")
    private String visualAngleName;
    /**
     * 企业相关因素
     */

    @ExcelProperty("企业相关因素")
    private String companyRelatedFactor;
    /**
     * 变化及趋势
     */

    @ExcelProperty("变化及趋势")
    private String changeTrend;
    /**
     * 影响描述
     */

    @ExcelProperty("影响描述")
    private String influenceDescription;
    /**
     * 建议措施
     */

    @ExcelProperty("建议措施")
    private String recommendedPractice;
    /**
     * 规划期
     */

    @ExcelProperty("规划期")
    private String planPeriod;
    /**
     * 预估机会点金额
     */

    @ExcelProperty("预估机会点金额")
    private String estimateOpportunityAmount;
    /**
     * 提出人员编码
     */
    @ExcelProperty("提出人员编码")
    private  String proposeEmployeeCode;
    /**
     * 提出人员姓名
     */

    @ExcelProperty("提出人员姓名")
    private String proposeEmployeeName;


}

