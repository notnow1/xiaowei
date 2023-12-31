package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
 * 指标分类表
 *
 * @author Graves
 * @since 2022-09-28
 */
@Data
@Accessors(chain = true)
public class IndicatorCategoryDTO extends BaseDTO {

    //查询检验
    public interface QueryIndicatorCategoryDTO extends Default {

    }

    //新增检验
    public interface AddIndicatorCategoryDTO extends Default {

    }

    //新增检验
    public interface DeleteIndicatorCategoryDTO extends Default {

    }

    //修改检验
    public interface UpdateIndicatorCategoryDTO extends Default {

    }

    /**
     * ID
     */
    private Long indicatorCategoryId;
    /**
     * 指标类型:1财务指标；2业务指标
     */
    private Integer indicatorType;
    /**
     * 指标分类编码
     */
    private String indicatorCategoryCode;
    /**
     * 指标分类名称
     */
    private String indicatorCategoryName;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;

}

