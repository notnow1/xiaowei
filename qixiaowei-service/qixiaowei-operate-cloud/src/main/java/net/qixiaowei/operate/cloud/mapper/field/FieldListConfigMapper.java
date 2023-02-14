package net.qixiaowei.operate.cloud.mapper.field;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.field.FieldListConfig;
import net.qixiaowei.operate.cloud.api.dto.field.FieldListConfigDTO;
import net.qixiaowei.operate.cloud.api.vo.field.FieldListConfigVO;
import net.qixiaowei.operate.cloud.api.vo.field.FieldListHeaderVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * FieldListConfigMapper接口
 *
 * @author hzk
 * @since 2023-02-08
 */
public interface FieldListConfigMapper {

    /**
     * 根据业务类型统计用户字段列表配置数量
     *
     * @param businessType 业务类型
     * @param userId 用户ID
     * @return 字段列表配置表集合
     */
    Integer countFieldHeaderListOfBusinessTypeAndUserId(@Param("businessType") Integer businessType, @Param("userId") Long userId);

    /**
     * 查询字段列表配置表列表
     *
     * @param businessType 业务类型
     * @param userId       用户ID
     * @return 字段列表配置表集合
     */
    List<FieldListHeaderVO> selectFieldHeaderListOfBusinessTypeAndUserId(@Param("businessType") Integer businessType, @Param("userId") Long userId);

    /**
     * 查询字段列表配置表列表
     *
     * @param businessType 业务类型
     * @param userId       用户ID
     * @return 字段列表配置表集合
     */
    List<FieldListConfigVO> selectFieldListConfigListOfBusinessTypeAndUserId(@Param("businessType") Integer businessType, @Param("userId") Long userId);

    /**
     * 查询字段列表配置表
     *
     * @param fieldListConfigId 字段列表配置表主键
     * @return 字段列表配置表
     */
    FieldListConfigDTO selectFieldListConfigByFieldListConfigId(@Param("fieldListConfigId") Long fieldListConfigId);


    /**
     * 批量查询字段列表配置表
     *
     * @param fieldListConfigIds 字段列表配置表主键集合
     * @return 字段列表配置表
     */
    List<FieldListConfigDTO> selectFieldListConfigByFieldListConfigIds(@Param("fieldListConfigIds") List<Long> fieldListConfigIds);

    /**
     * 查询字段列表配置表列表
     *
     * @param fieldListConfig 字段列表配置表
     * @return 字段列表配置表集合
     */
    List<FieldListConfigDTO> selectFieldListConfigList(@Param("fieldListConfig") FieldListConfig fieldListConfig);

    /**
     * 新增字段列表配置表
     *
     * @param fieldListConfig 字段列表配置表
     * @return 结果
     */
    int insertFieldListConfig(@Param("fieldListConfig") FieldListConfig fieldListConfig);

    /**
     * 修改字段列表配置表
     *
     * @param fieldListConfig 字段列表配置表
     * @return 结果
     */
    int updateFieldListConfig(@Param("fieldListConfig") FieldListConfig fieldListConfig);

    /**
     * 批量修改字段列表配置表
     *
     * @param fieldListConfigList 字段列表配置表
     * @return 结果
     */
    int updateFieldListConfigs(@Param("fieldListConfigList") List<FieldListConfig> fieldListConfigList);

    /**
     * 逻辑删除字段列表配置表
     *
     * @param fieldListConfig
     * @return 结果
     */
    int logicDeleteFieldListConfigByFieldListConfigId(@Param("fieldListConfig") FieldListConfig fieldListConfig);

    /**
     * 逻辑批量删除字段列表配置表
     *
     * @param fieldListConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteFieldListConfigByFieldListConfigIds(@Param("fieldListConfigIds") List<Long> fieldListConfigIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除字段列表配置表
     *
     * @param fieldListConfigId 字段列表配置表主键
     * @return 结果
     */
    int deleteFieldListConfigByFieldListConfigId(@Param("fieldListConfigId") Long fieldListConfigId);

    /**
     * 物理批量删除字段列表配置表
     *
     * @param fieldListConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteFieldListConfigByFieldListConfigIds(@Param("fieldListConfigIds") List<Long> fieldListConfigIds);

    /**
     * 批量新增字段列表配置表
     *
     * @param FieldListConfigs 字段列表配置表列表
     * @return 结果
     */
    int batchFieldListConfig(@Param("fieldListConfigs") List<FieldListConfig> FieldListConfigs);
}
