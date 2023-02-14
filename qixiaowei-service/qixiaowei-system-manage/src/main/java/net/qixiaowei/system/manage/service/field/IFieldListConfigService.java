package net.qixiaowei.system.manage.service.field;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.field.FieldListConfigDTO;
import net.qixiaowei.system.manage.api.vo.field.FieldListConfigVO;
import net.qixiaowei.system.manage.api.vo.field.FieldListHeaderVO;


/**
* FieldListConfigService接口
* @author hzk
* @since 2023-02-08
*/
public interface IFieldListConfigService{
    /**
    * 查询字段列表配置表
    *
    * @param fieldListConfigId 字段列表配置表主键
    * @return 字段列表配置表
    */
    FieldListConfigDTO selectFieldListConfigByFieldListConfigId(Long fieldListConfigId);

    /**
     * 查询字段列表配置表列表
     *
     * @param businessType 业务类型
     * @return 字段列表配置表集合
     */
    List<FieldListHeaderVO> selectHeaderFieldListConfigList(Integer businessType);

    /**
    * 查询字段列表配置表列表
    *
    * @param businessType 业务类型
    * @return 字段列表配置表集合
    */
    List<FieldListConfigVO> selectFieldListConfigList(Integer businessType);


    /**
    * 新增字段列表配置表
    *
    * @param fieldListConfigDTO 字段列表配置表
    * @return 结果
    */
    FieldListConfigDTO insertFieldListConfig(FieldListConfigDTO fieldListConfigDTO);

    /**
    * 修改字段列表配置表
    *
    * @param fieldListConfigDTO 字段列表配置表
    * @return 结果
    */
    int updateFieldListConfig(FieldListConfigDTO fieldListConfigDTO);

    /**
    * 批量修改字段列表配置表
    *
    * @param fieldListConfigDtos 字段列表配置表
    * @return 结果
    */
    int updateFieldListConfigs(List<FieldListConfigDTO> fieldListConfigDtos);

    /**
    * 批量新增字段列表配置表
    *
    * @param fieldListConfigDtos 字段列表配置表
    * @return 结果
    */
    int insertFieldListConfigs(List<FieldListConfigDTO> fieldListConfigDtos);

    /**
    * 逻辑批量删除字段列表配置表
    *
    * @param fieldListConfigIds 需要删除的字段列表配置表集合
    * @return 结果
    */
    int logicDeleteFieldListConfigByFieldListConfigIds(List<Long> fieldListConfigIds);

    /**
    * 逻辑删除字段列表配置表信息
    *
    * @param fieldListConfigDTO
    * @return 结果
    */
    int logicDeleteFieldListConfigByFieldListConfigId(FieldListConfigDTO fieldListConfigDTO);
    /**
    * 批量删除字段列表配置表
    *
    * @param FieldListConfigDtos
    * @return 结果
    */
    int deleteFieldListConfigByFieldListConfigIds(List<FieldListConfigDTO> FieldListConfigDtos);

    /**
    * 逻辑删除字段列表配置表信息
    *
    * @param fieldListConfigDTO
    * @return 结果
    */
    int deleteFieldListConfigByFieldListConfigId(FieldListConfigDTO fieldListConfigDTO);


    /**
    * 删除字段列表配置表信息
    *
    * @param fieldListConfigId 字段列表配置表主键
    * @return 结果
    */
    int deleteFieldListConfigByFieldListConfigId(Long fieldListConfigId);
}
