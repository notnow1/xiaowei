package net.qixiaowei.system.manage.api.dto.basic;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankDecompose;

/**
 * 职级体系表
 *
 * @author Graves
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class OfficialRankSystemDTO extends BaseDTO {

    //查询检验
    public interface QueryOfficialRankSystemDTO extends Default {

    }

    //新增检验
    public interface AddOfficialRankSystemDTO extends Default {

    }

    //新增检验
    public interface DeleteOfficialRankSystemDTO extends Default {

    }

    //修改检验
    public interface UpdateOfficialRankSystemDTO extends Default {

    }

    /**
     * ID
     */
    private Long officialRankSystemId;
    /**
     * 职级体系名称
     */
    private String officialRankSystemName;

    /**
     * 级别前缀编码
     */
    private String rankPrefixCode;
    /**
     * 起始级别
     */
    private Integer rankStart;
    /**
     * 终止级别
     */
    private Integer rankEnd;
    /**
     * 岗位职级下限
     */
    private Integer postRankLower;
    /**
     * 岗位职级上限
     */
    private Integer postRankUpper;
    /**
     * 职级分解维度:1部门;2区域;3省份;4产品
     */
    private Integer rankDecomposeDimension;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * ID集合
     */
    private Map<String, String> idMap;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 职级分解
     */
    private List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS;
    /**
     * 职级分解
     */
    private List<OfficialRankDecompose> officialRankDecomposes;
    /**
     * 职级
     */
    private String officialRank;

    /**
     * 上年期末数
     */
    private Integer amountLastYear;
    /**
     * 年度平均人数
     */
    private BigDecimal annualAverageNum;
    /**
     * 年末总计
     */
    private Integer endYearSum;
    /**
     * 职级名称
     */
    private String rankCodeName;
    /**
     * 职级级别
     */
    private Integer rankCode;

}

