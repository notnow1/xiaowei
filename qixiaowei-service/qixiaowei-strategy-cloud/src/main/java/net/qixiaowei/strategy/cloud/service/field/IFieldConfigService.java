package net.qixiaowei.strategy.cloud.service.field;

import net.qixiaowei.strategy.cloud.api.dto.field.FieldConfigDTO;

import java.util.List;


/**
 * FieldConfigService接口
 *
 * @author hzk
 * @since 2023-02-08
 */
public interface IFieldConfigService {
    /**
     * 查询字段配置表
     *
     * @param fieldConfigId 字段配置表主键
     * @return 字段配置表
     */
    FieldConfigDTO selectFieldConfigByFieldConfigId(Long fieldConfigId);

    /**
     * 根据业务类型查询字段配置表列表
     *
     * @param businessType 业务类型
     * @return 字段配置表集合
     */
    List<FieldConfigDTO> selectFieldConfigListOfBusinessType(Integer businessType);

    /**
     * 查询字段配置表列表
     *
     * @param fieldConfigDTO 字段配置表
     * @return 字段配置表集合
     */
    List<FieldConfigDTO> selectFieldConfigList(FieldConfigDTO fieldConfigDTO);

    /**
     * 新增字段配置表
     *
     * @param fieldConfigDTO 字段配置表
     * @return 结果
     */
    FieldConfigDTO insertFieldConfig(FieldConfigDTO fieldConfigDTO);

    /**
     * 修改字段配置表
     *
     * @param fieldConfigDTO 字段配置表
     * @return 结果
     */
    int updateFieldConfig(FieldConfigDTO fieldConfigDTO);

    /**
     * 批量修改字段配置表
     *
     * @param fieldConfigDtos 字段配置表
     * @return 结果
     */
    int updateFieldConfigs(List<FieldConfigDTO> fieldConfigDtos);

    /**
     * 批量新增字段配置表
     *
     * @param fieldConfigDtos 字段配置表
     * @return 结果
     */
    int insertFieldConfigs(List<FieldConfigDTO> fieldConfigDtos);

    /**
     * 逻辑批量删除字段配置表
     *
     * @param fieldConfigIds 需要删除的字段配置表集合
     * @return 结果
     */
    int logicDeleteFieldConfigByFieldConfigIds(List<Long> fieldConfigIds);

    /**
     * 逻辑删除字段配置表信息
     *
     * @param fieldConfigDTO
     * @return 结果
     */
    int logicDeleteFieldConfigByFieldConfigId(FieldConfigDTO fieldConfigDTO);

    /**
     * 批量删除字段配置表
     *
     * @param FieldConfigDtos
     * @return 结果
     */
    int deleteFieldConfigByFieldConfigIds(List<FieldConfigDTO> FieldConfigDtos);

    /**
     * 逻辑删除字段配置表信息
     *
     * @param fieldConfigDTO
     * @return 结果
     */
    int deleteFieldConfigByFieldConfigId(FieldConfigDTO fieldConfigDTO);


    /**
     * 删除字段配置表信息
     *
     * @param fieldConfigId 字段配置表主键
     * @return 结果
     */
    int deleteFieldConfigByFieldConfigId(Long fieldConfigId);

}
