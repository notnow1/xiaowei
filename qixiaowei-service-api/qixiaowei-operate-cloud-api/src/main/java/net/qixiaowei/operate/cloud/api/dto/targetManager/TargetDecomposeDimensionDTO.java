package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 目标分解维度配置
 *
 * @author Graves
 * @since 2022-09-26
 */
@Data
@Accessors(chain = true)
public class TargetDecomposeDimensionDTO {
    /**
     * ID
     */
    private Long targetDecomposeDimensionId;
    /**
     * 分解维度(region,salesman,department,product,province,industry)
     */
    private String decompositionDimension;
    /**
     * 排序
     */
    private Integer sort;
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
    /**
     * 分解维度名称(区域，销售员，部门，产品，省份，行业)
     */
    private String decompositionDimensionName;
}

