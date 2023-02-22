package net.qixiaowei.strategy.cloud.mapper.field;

import net.qixiaowei.strategy.cloud.api.domain.field.FieldConfig;
import net.qixiaowei.strategy.cloud.api.dto.field.FieldConfigDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * FieldConfigMapper接口
 *
 * @author hzk
 * @since 2023-02-08
 */
public interface FieldConfigMapper {
    /**
     * 查询字段配置表
     *
     * @param fieldConfigId 字段配置表主键
     * @return 字段配置表
     */
    FieldConfigDTO selectFieldConfigByFieldConfigId(@Param("fieldConfigId") Long fieldConfigId);

    /**
     * 根据业务类型统计字段配置表
     *
     * @param businessType 业务类型
     * @return 字段配置表
     */
    Integer countFieldOfBusinessType(@Param("businessType") Integer businessType);

    /**
     * 根据业务类型查询字段配置表
     *
     * @param businessType 业务类型
     * @return 字段配置表
     */
    List<FieldConfigDTO> selectFieldConfigByBusinessType(@Param("businessType") Integer businessType);


    /**
     * 批量查询字段配置表
     *
     * @param fieldConfigIds 字段配置表主键集合
     * @return 字段配置表
     */
    List<FieldConfigDTO> selectFieldConfigByFieldConfigIds(@Param("fieldConfigIds") List<Long> fieldConfigIds);

    /**
     * 查询字段配置表列表
     *
     * @param fieldConfig 字段配置表
     * @return 字段配置表集合
     */
    List<FieldConfigDTO> selectFieldConfigList(@Param("fieldConfig") FieldConfig fieldConfig);

    /**
     * 新增字段配置表
     *
     * @param fieldConfig 字段配置表
     * @return 结果
     */
    int insertFieldConfig(@Param("fieldConfig") FieldConfig fieldConfig);

    /**
     * 修改字段配置表
     *
     * @param fieldConfig 字段配置表
     * @return 结果
     */
    int updateFieldConfig(@Param("fieldConfig") FieldConfig fieldConfig);

    /**
     * 批量修改字段配置表
     *
     * @param fieldConfigList 字段配置表
     * @return 结果
     */
    int updateFieldConfigs(@Param("fieldConfigList") List<FieldConfig> fieldConfigList);

    /**
     * 逻辑删除字段配置表
     *
     * @param fieldConfig
     * @return 结果
     */
    int logicDeleteFieldConfigByFieldConfigId(@Param("fieldConfig") FieldConfig fieldConfig);

    /**
     * 逻辑批量删除字段配置表
     *
     * @param fieldConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteFieldConfigByFieldConfigIds(@Param("fieldConfigIds") List<Long> fieldConfigIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除字段配置表
     *
     * @param fieldConfigId 字段配置表主键
     * @return 结果
     */
    int deleteFieldConfigByFieldConfigId(@Param("fieldConfigId") Long fieldConfigId);

    /**
     * 物理批量删除字段配置表
     *
     * @param fieldConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteFieldConfigByFieldConfigIds(@Param("fieldConfigIds") List<Long> fieldConfigIds);

    /**
     * 批量新增字段配置表
     *
     * @param fieldConfigs 字段配置表列表
     * @return 结果
     */
    int batchFieldConfig(@Param("fieldConfigs") List<FieldConfig> fieldConfigs);
}
