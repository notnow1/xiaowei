package net.qixiaowei.strategy.cloud.service.businessDesign;

import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignAxisConfigDTO;

import java.util.List;


/**
 * BusinessDesignAxisConfigService接口
 *
 * @author Graves
 * @since 2023-02-28
 */
public interface IBusinessDesignAxisConfigService {
    /**
     * 查询业务设计轴配置表
     *
     * @param businessDesignAxisConfigId 业务设计轴配置表主键
     * @return 业务设计轴配置表
     */
    BusinessDesignAxisConfigDTO selectBusinessDesignAxisConfigByBusinessDesignAxisConfigId(Long businessDesignAxisConfigId);

    /**
     * 查询业务设计轴配置表列表
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 业务设计轴配置表集合
     */
    List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigList(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO);

    /**
     * 新增业务设计轴配置表
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 结果
     */
    BusinessDesignAxisConfigDTO insertBusinessDesignAxisConfig(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO);

    /**
     * 修改业务设计轴配置表
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 结果
     */
    int updateBusinessDesignAxisConfig(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO);

    /**
     * 批量修改业务设计轴配置表
     *
     * @param businessDesignAxisConfigDtos 业务设计轴配置表
     * @return 结果
     */
    int updateBusinessDesignAxisConfigs(List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDtos);

    /**
     * 批量新增业务设计轴配置表
     *
     * @param businessDesignAxisConfigDtos 业务设计轴配置表
     * @return 结果
     */
    int insertBusinessDesignAxisConfigs(List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDtos);

    /**
     * 逻辑批量删除业务设计轴配置表
     *
     * @param businessDesignAxisConfigIds 需要删除的业务设计轴配置表集合
     * @return 结果
     */
    int logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(List<Long> businessDesignAxisConfigIds);

    /**
     * 逻辑删除业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigDTO
     * @return 结果
     */
    int logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO);

    /**
     * 批量删除业务设计轴配置表
     *
     * @param BusinessDesignAxisConfigDtos
     * @return 结果
     */
    int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(List<BusinessDesignAxisConfigDTO> BusinessDesignAxisConfigDtos);

    /**
     * 逻辑删除业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigDTO
     * @return 结果
     */
    int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO);


    /**
     * 删除业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigId 业务设计轴配置表主键
     * @return 结果
     */
    int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(Long businessDesignAxisConfigId);

    /**
     * 根据业务设计ID集合查找业务设计轴配置表信息
     *
     * @param businessDesignId 业务设计ID集
     * @return List
     */
    List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigByBusinessDesignId(Long businessDesignId);

    /**
     * 根据业务设计ID集合查找业务设计轴配置表信息
     *
     * @param businessDesignIds 业务设计ID集合
     * @return List
     */
    List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigByBusinessDesignIds(List<Long> businessDesignIds);
}
