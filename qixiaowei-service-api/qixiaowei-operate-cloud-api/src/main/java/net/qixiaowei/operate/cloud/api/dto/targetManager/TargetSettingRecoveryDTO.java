package net.qixiaowei.operate.cloud.api.dto.targetManager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 目标制定回款表
 *
 * @author Graves
 * @since 2022-11-01
 */
@Data
@Accessors(chain = true)
public class TargetSettingRecoveryDTO {

    //查询检验
    public interface QueryTargetSettingRecoveryDTO extends Default {

    }

    //新增检验
    public interface AddTargetSettingRecoveryDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetSettingRecoveryDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetSettingRecoveryDTO extends Default {

    }

    /**
     * ID
     */
    private Long targetSettingRecoveriesId;
    /**
     * 目标制定ID
     */
    private Long targetSettingId;
    /**
     * 上年年末应收账款余额
     */
    private BigDecimal balanceReceivables;
    /**
     * DSO(应收账款周转天数)基线
     */
    private Integer baselineValue;
    /**
     * DSO(应收账款周转天数)改进天数
     */
    private Integer improveDays;
    /**
     * 上年年末应收账款余额
     */
    private BigDecimal addRate;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}

