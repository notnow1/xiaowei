package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 职级薪酬表
 *
 * @author Graves
 * @since 2022-11-30
 */
@Data
@Accessors(chain = true)
public class OfficialRankEmolumentDTO {

    //查询检验
    public interface QueryOfficialRankEmolumentDTO extends Default {

    }

    //新增检验
    public interface AddOfficialRankEmolumentDTO extends Default {

    }

    //删除检验
    public interface DeleteOfficialRankEmolumentDTO extends Default {

    }

    //修改检验
    public interface UpdateOfficialRankEmolumentDTO extends Default {

    }

    /**
     * ID
     */
    private Long officialRankEmolumentId;
    /**
     * 职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 职级
     */
    private Integer officialRank;
    /**
     * 职级名称
     */
    private String officialRankName;
    /**
     * 岗位
     */
    private List<Map<String, Object>> postList;
    /**
     * 工资上限
     */
    private BigDecimal salaryCap;
    /**
     * 工资下限
     */
    private BigDecimal salaryFloor;
    /**
     * 工资中位数
     */
    private BigDecimal salaryMedian;
    /**
     * 工资宽幅
     */
    private BigDecimal salaryWide;
    /**
     * 工资递增率
     */
    private BigDecimal increaseRate;
    /**
     * 职级确定薪酬
     */
    private List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList;
    /**
     * 职级确定薪酬
     */
    private List<Object> officialRankDecomposeDTOS;
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
