package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
 * 默认行业
 *
 * @author Graves
 * @since 2022-09-26
 */
@Data
@Accessors(chain = true)
public class IndustryDefaultDTO extends BaseDTO {

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
     * 父级行业名称
     */
    private String parentIndustryName;
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
     * 树子节点
     */
    private List<IndustryDefaultDTO> childDefaultIndustry;

}

