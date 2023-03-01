package net.qixiaowei.strategy.cloud.service.impl.businessDesign;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.businessDesign.BusinessDesignAxisConfig;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignAxisConfigDTO;
import net.qixiaowei.strategy.cloud.mapper.businessDesign.BusinessDesignAxisConfigMapper;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignAxisConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * BusinessDesignAxisConfigService业务层处理
 *
 * @author Graves
 * @since 2023-02-28
 */
@Service
public class BusinessDesignAxisConfigServiceImpl implements IBusinessDesignAxisConfigService {
    @Autowired
    private BusinessDesignAxisConfigMapper businessDesignAxisConfigMapper;

    /**
     * 查询业务设计轴配置表
     *
     * @param businessDesignAxisConfigId 业务设计轴配置表主键
     * @return 业务设计轴配置表
     */
    @Override
    public BusinessDesignAxisConfigDTO selectBusinessDesignAxisConfigByBusinessDesignAxisConfigId(Long businessDesignAxisConfigId) {
        return businessDesignAxisConfigMapper.selectBusinessDesignAxisConfigByBusinessDesignAxisConfigId(businessDesignAxisConfigId);
    }

    /**
     * 查询业务设计轴配置表列表
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 业务设计轴配置表
     */
    @Override
    public List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigList(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO) {
        BusinessDesignAxisConfig businessDesignAxisConfig = new BusinessDesignAxisConfig();
        BeanUtils.copyProperties(businessDesignAxisConfigDTO, businessDesignAxisConfig);
        return businessDesignAxisConfigMapper.selectBusinessDesignAxisConfigList(businessDesignAxisConfig);
    }

    /**
     * 新增业务设计轴配置表
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 结果
     */
    @Override
    public BusinessDesignAxisConfigDTO insertBusinessDesignAxisConfig(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO) {
        BusinessDesignAxisConfig businessDesignAxisConfig = new BusinessDesignAxisConfig();
        BeanUtils.copyProperties(businessDesignAxisConfigDTO, businessDesignAxisConfig);
        businessDesignAxisConfig.setCreateBy(SecurityUtils.getUserId());
        businessDesignAxisConfig.setCreateTime(DateUtils.getNowDate());
        businessDesignAxisConfig.setUpdateTime(DateUtils.getNowDate());
        businessDesignAxisConfig.setUpdateBy(SecurityUtils.getUserId());
        businessDesignAxisConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        businessDesignAxisConfigMapper.insertBusinessDesignAxisConfig(businessDesignAxisConfig);
        businessDesignAxisConfigDTO.setBusinessDesignAxisConfigId(businessDesignAxisConfig.getBusinessDesignAxisConfigId());
        return businessDesignAxisConfigDTO;
    }

    /**
     * 修改业务设计轴配置表
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 结果
     */
    @Override
    public int updateBusinessDesignAxisConfig(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO) {
        BusinessDesignAxisConfig businessDesignAxisConfig = new BusinessDesignAxisConfig();
        BeanUtils.copyProperties(businessDesignAxisConfigDTO, businessDesignAxisConfig);
        businessDesignAxisConfig.setUpdateTime(DateUtils.getNowDate());
        businessDesignAxisConfig.setUpdateBy(SecurityUtils.getUserId());
        return businessDesignAxisConfigMapper.updateBusinessDesignAxisConfig(businessDesignAxisConfig);
    }

    /**
     * 逻辑批量删除业务设计轴配置表
     *
     * @param businessDesignAxisConfigIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(List<Long> businessDesignAxisConfigIds) {
        return businessDesignAxisConfigMapper.logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(businessDesignAxisConfigIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigId 业务设计轴配置表主键
     * @return 结果
     */
    @Override
    public int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(Long businessDesignAxisConfigId) {
        return businessDesignAxisConfigMapper.deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(businessDesignAxisConfigId);
    }

    /**
     * 根据业务设计ID查找业务设计轴配置表信息
     *
     * @param businessDesignId 业务设计ID
     * @return List
     */
    @Override
    public List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigByBusinessDesignId(Long businessDesignId) {
        return businessDesignAxisConfigMapper.selectBusinessDesignAxisConfigByBusinessDesignId(businessDesignId);
    }

    /**
     * 根据业务设计ID集合查找业务设计轴配置表信息
     *
     * @param businessDesignIds 业务设计ID集合
     * @return List
     */
    @Override
    public List<BusinessDesignAxisConfigDTO> selectBusinessDesignAxisConfigByBusinessDesignIds(List<Long> businessDesignIds) {
        return businessDesignAxisConfigMapper.selectBusinessDesignAxisConfigByBusinessDesignIds(businessDesignIds);
    }

    /**
     * 逻辑删除业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 结果
     */
    @Override
    public int logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO) {
        BusinessDesignAxisConfig businessDesignAxisConfig = new BusinessDesignAxisConfig();
        businessDesignAxisConfig.setBusinessDesignAxisConfigId(businessDesignAxisConfigDTO.getBusinessDesignAxisConfigId());
        businessDesignAxisConfig.setUpdateTime(DateUtils.getNowDate());
        businessDesignAxisConfig.setUpdateBy(SecurityUtils.getUserId());
        return businessDesignAxisConfigMapper.logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(businessDesignAxisConfig);
    }

    /**
     * 物理删除业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigDTO 业务设计轴配置表
     * @return 结果
     */

    @Override
    public int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO) {
        BusinessDesignAxisConfig businessDesignAxisConfig = new BusinessDesignAxisConfig();
        BeanUtils.copyProperties(businessDesignAxisConfigDTO, businessDesignAxisConfig);
        return businessDesignAxisConfigMapper.deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigId(businessDesignAxisConfig.getBusinessDesignAxisConfigId());
    }

    /**
     * 物理批量删除业务设计轴配置表
     *
     * @param businessDesignAxisConfigDtos 需要删除的业务设计轴配置表主键
     * @return 结果
     */

    @Override
    public int deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDtos) {
        List<Long> stringList = new ArrayList<>();
        for (BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO : businessDesignAxisConfigDtos) {
            stringList.add(businessDesignAxisConfigDTO.getBusinessDesignAxisConfigId());
        }
        return businessDesignAxisConfigMapper.deleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(stringList);
    }

    /**
     * 批量新增业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigDtos 业务设计轴配置表对象
     */

    public int insertBusinessDesignAxisConfigs(List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDtos) {
        List<BusinessDesignAxisConfig> businessDesignAxisConfigList = new ArrayList<>();

        for (BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO : businessDesignAxisConfigDtos) {
            BusinessDesignAxisConfig businessDesignAxisConfig = new BusinessDesignAxisConfig();
            BeanUtils.copyProperties(businessDesignAxisConfigDTO, businessDesignAxisConfig);
            businessDesignAxisConfig.setCreateBy(SecurityUtils.getUserId());
            businessDesignAxisConfig.setCreateTime(DateUtils.getNowDate());
            businessDesignAxisConfig.setUpdateTime(DateUtils.getNowDate());
            businessDesignAxisConfig.setUpdateBy(SecurityUtils.getUserId());
            businessDesignAxisConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            businessDesignAxisConfigList.add(businessDesignAxisConfig);
        }
        return businessDesignAxisConfigMapper.batchBusinessDesignAxisConfig(businessDesignAxisConfigList);
    }

    /**
     * 批量修改业务设计轴配置表信息
     *
     * @param businessDesignAxisConfigDtos 业务设计轴配置表对象
     */

    public int updateBusinessDesignAxisConfigs(List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDtos) {
        List<BusinessDesignAxisConfig> businessDesignAxisConfigList = new ArrayList<>();

        for (BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO : businessDesignAxisConfigDtos) {
            BusinessDesignAxisConfig businessDesignAxisConfig = new BusinessDesignAxisConfig();
            BeanUtils.copyProperties(businessDesignAxisConfigDTO, businessDesignAxisConfig);
            businessDesignAxisConfig.setCreateBy(SecurityUtils.getUserId());
            businessDesignAxisConfig.setCreateTime(DateUtils.getNowDate());
            businessDesignAxisConfig.setUpdateTime(DateUtils.getNowDate());
            businessDesignAxisConfig.setUpdateBy(SecurityUtils.getUserId());
            businessDesignAxisConfigList.add(businessDesignAxisConfig);
        }
        return businessDesignAxisConfigMapper.updateBusinessDesignAxisConfigs(businessDesignAxisConfigList);
    }

}

