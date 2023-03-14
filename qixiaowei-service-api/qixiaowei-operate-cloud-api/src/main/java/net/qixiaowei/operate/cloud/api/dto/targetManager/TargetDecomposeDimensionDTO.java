package net.qixiaowei.operate.cloud.api.dto.targetManager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 目标分解维度配置
 *
 * @author Graves
 * @since 2022-09-26
 */
@Data
@Accessors(chain = true)
public class TargetDecomposeDimensionDTO extends BaseDTO {
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
     * 分解维度名称(区域，销售员，部门，产品，省份，行业)
     */
    private String decompositionDimensionName;
    /**
     * 字段名称
     */
    private List<Map<String, String>> fileNameList;
}

