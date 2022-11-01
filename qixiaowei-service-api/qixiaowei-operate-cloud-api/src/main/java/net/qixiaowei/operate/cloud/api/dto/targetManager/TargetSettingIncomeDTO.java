package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 目标制定收入表
 *
 * @author Graves
 * @since 2022-10-31
 */
@Data
@Accessors(chain = true)
public class TargetSettingIncomeDTO {

    //查询检验
    public interface QueryTargetSettingIncomeDTO extends Default {

    }

    //新增检验
    public interface AddTargetSettingIncomeDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetSettingIncomeDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetSettingIncomeDTO extends Default {

    }

    /**
     * ID
     */
    private Long targetSettingIncomeId;
    /**
     * 目标制定ID
     */
    private Long targetSettingId;
    /**
     * 一年前订单金额
     */
    private BigDecimal moneyBeforeOne;
    /**
     * 两年前订单金额
     */
    private BigDecimal moneyBeforeTwo;
    /**
     * 三年前订单金额
     */
    private BigDecimal moneyBeforeThree;
    /**
     * 一年前订单转化率
     */
    private BigDecimal conversionBeforeOne;
    /**
     * 两年前订单转化率
     */
    private BigDecimal conversionBeforeTwo;
    /**
     * 三年前订单转化率
     */
    private BigDecimal conversionBeforeThree;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
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

