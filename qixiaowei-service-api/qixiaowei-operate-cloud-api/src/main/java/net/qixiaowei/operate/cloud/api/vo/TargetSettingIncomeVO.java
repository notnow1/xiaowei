package net.qixiaowei.operate.cloud.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Graves
 * @Date 2022-10-31 14:21
 **/
@Data
@Accessors(chain = true)
public class TargetSettingIncomeVO {
    /**
     * 订单金额
     */
    private BigDecimal money;
    /**
     * 订单转化率List
     */
    private BigDecimal conversion;
    /**
     * 销售收入List
     */
    private BigDecimal income;
    /**
     * 年限List
     */
    private String yearName;

    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
