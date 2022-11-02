package net.qixiaowei.operate.cloud.api.dto.targetManager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 目标制定回款集合表
 *
 * @author Graves
 * @since 2022-11-01
 */
@Data
@Accessors(chain = true)
public class TargetSettingRecoveriesDTO {

    //查询检验
    public interface QueryTargetSettingRecoveriesDTO extends Default {

    }

    //新增检验
    public interface AddTargetSettingRecoveriesDTO extends Default {

    }

    //删除检验
    public interface DeleteTargetSettingRecoveriesDTO extends Default {

    }

    //修改检验
    public interface UpdateTargetSettingRecoveriesDTO extends Default {

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
     * 类型:1:应回尽回;2:逾期清理;3:提前还款;4:销售收入目标;5:期末应收账款余额
     */
    private Integer type;
    /**
     * 类型:1:应回尽回;2:逾期清理;3:提前还款;4:销售收入目标;5:期末应收账款余额
     */
    private String prefixType;
    /**
     * 上年实际值
     */
    @Value("0")
    private BigDecimal actualLastYear;
    /**
     * 挑战值
     */
    @Value("0")
    private BigDecimal challengeValue;
    /**
     * 目标值
     */
    @Value("0")
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    @Value("0")
    private BigDecimal guaranteedValue;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}

