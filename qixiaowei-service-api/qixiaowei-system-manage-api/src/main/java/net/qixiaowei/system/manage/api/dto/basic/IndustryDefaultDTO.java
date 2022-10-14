package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 默认行业
 *
 * @author Graves
 * @since 2022-09-26
 */
@Data
@Accessors(chain = true)
public class IndustryDefaultDTO {

    //查询检验
    public interface QueryIndustryDefaultDTO extends Default {

    }

    //新增检验
    public interface AddIndustryDefaultDTO extends Default {

    }

    //新增检验
    public interface DeleteIndustryDefaultDTO extends Default {

    }

    //修改检验
    public interface UpdateIndustryDefaultDTO extends Default {

    }

    /**
     * ID
     */
    private Long industryId;
    /**
     * 父级行业ID
     */
    private Long parentIndustryId;
    /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
    private String ancestors;
    /**
     * 行业编码
     */
    private String industryCode;
    /**
     * 行业名称
     */
    private String industryName;
    /**
     * 层级
     */
    private Integer level;
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
     * 树子节点
     */
    private List<IndustryDefaultDTO> childDefaultIndustry;

}

