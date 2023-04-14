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
 * 行业
 *
 * @author Graves
 * @since 2022-09-26
 */
@Data
@Accessors(chain = true)
public class IndustryDTO extends BaseDTO {

    //查询检验
    public interface QueryIndustryDTO extends Default {

    }

    //新增检验
    public interface AddIndustryDTO extends Default {

    }

    //新增检验
    public interface DeleteIndustryDTO extends Default {

    }

    //修改检验
    public interface UpdateIndustryDTO extends Default {

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
     * 父级行业ID
     */
    private String parentIndustryName;
    /**
     * excel行业下拉框名称
     */
    private String parentIndustryExcelName;

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
     * 启用类型（0-默认，1-自定义）
     */
    private Integer configValue;
    /**
     * 是否需要所有的(1-需要所有的行业)
     */
    private Integer isAll;
    /**
     * 组织子节点信息
     */
    private List<IndustryDTO> children;
}

