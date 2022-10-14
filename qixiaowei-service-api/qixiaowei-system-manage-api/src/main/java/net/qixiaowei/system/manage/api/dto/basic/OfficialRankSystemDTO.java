package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 职级体系表
 *
 * @author Graves
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class OfficialRankSystemDTO {

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
     * 职级分解维度:1部门;2区域;3省份;4产品
     */
    private Integer rankDecomposeDimension;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
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
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 职级分解
     */
    private List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS;
    /**
     * 职级
     */
    private String officialRank;
    /**
     * 请求参数
     */
    private Map<String, Object> params;

}

