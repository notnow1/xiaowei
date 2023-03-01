package net.qixiaowei.strategy.cloud.mapper.businessDesign;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.businessDesign.BusinessDesignAxisConfig;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignAxisConfigDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * BusinessDesignAxisConfigMapper接口
 *
 * @author Graves
 * @since 2023-02-28
 */
public interface BusinessDesignAxisConfigMapper {
    /**
     * 查询业务设计轴配置表
     *
     * @param businessDesignAxisConfigId 业务设计轴配置表主键
     * @return 业务设计轴配置表
     */
    BusinessDesignAxisConfigDTO selectBusinessDesignAxisConfigByBusinessDesignAxisConfigId(@Param("businessDesignAxisConfigId") Long businessDesignAxisConfigId);


    /**
     * 批量查询业务设计轴配置表
     *
     * @param businessDesignAxisConfigIds 业务设计轴配置表主键集合
     * @return 业务设计轴配置表
     */
    List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(@Param("businessDesignAxisConfigIds") List<Long> businessDesignAxisConfigIds);

    /**
     * 查询业务设计轴配置表列表
     *
     * @param businessDesignAxisConfig 业务设计轴配置表
     * @return 业务设计轴配置表集合
     */
    List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigList(@Param("businessDesignAxisConfig") BusinessDesignAxisConfig businessDesignAxisConfig);

    /**
     * 新增业务设计轴配置表
     *
     * @param businessDesignAxisConfig 业务设计轴配置表
     * @return 结果
     */
    int insertBusinessDesignAxisConfig(@Param("businessDesignAxisConfig") BusinessDesignAxisConfig businessDesignAxisConfig);

    /**
     * 修改业务设计轴配置表
     *
     * @param businessDesignAxisConfig 业务设计轴配置表
     * @return 结果
     */
    int updateBusinessDesignAxisConfig(@Param("businessDesignAxisConfig") BusinessDesignAxisConfig businessDesignAxisConfig);

    /**
     * 批量修改业务设计轴配置表
     *
     * @param businessDesignAxisConfigList 业务设计轴配置表
     * @return 结果
     */
    int updateBusinessDesignAxisConfigs(@Param("businessDesignAxisConfigList") List<BusinessDesignAxisConfig> businessDesignAxisConfigList);

    /**
     * 逻辑删除业务设计轴配置表
     *
     * @param businessDesignAxisConfig
     * @return 结果
     */
    int logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(@Param("businessDesignAxisConfig") BusinessDesignAxisConfig businessDesignAxisConfig);

    /**
     * 逻辑批量删除业务设计轴配置表
     *
     * @param businessDesignAxisConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(@Param("businessDesignAxisConfigIds") List<Long> businessDesignAxisConfigIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除业务设计轴配置表
     *
     * @param businessDesignAxisConfigId 业务设计轴配置表主键
     * @return 结果
     */
    int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(@Param("businessDesignAxisConfigId") Long businessDesignAxisConfigId);

    /**
     * 物理批量删除业务设计轴配置表
     *
     * @param businessDesignAxisConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(@Param("businessDesignAxisConfigIds") List<Long> businessDesignAxisConfigIds);

    /**
     * 批量新增业务设计轴配置表
     *
     * @param businessDesignAxisConfigs 业务设计轴配置表列表
     * @return 结果
     */
    int batchBusinessDesignAxisConfig(@Param("businessDesignAxisConfigs") List<BusinessDesignAxisConfig> businessDesignAxisConfigs);

    /**
     * 根据业务设计ID查找业务设计轴配置表信息
     *
     * @param businessDesignId 业务设计ID
     * @return List
     */
    List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigByBusinessDesignId(@Param("businessDesignId") Long businessDesignId);

    /**
     * 根据业务设计ID查找业务设计轴配置表信息
     *
     * @param businessDesignIds 业务设计ID
     * @return List
     */
    List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigByBusinessDesignIds(@Param("businessDesignIds") List<Long> businessDesignIds);
}
