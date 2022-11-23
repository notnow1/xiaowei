package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Config;
import net.qixiaowei.system.manage.api.dto.basic.ConfigDTO;
import net.qixiaowei.system.manage.mapper.basic.ConfigMapper;
import net.qixiaowei.system.manage.service.basic.IConfigService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * ConfigService业务层处理
 *
 * @author Graves
 * @since 2022-10-18
 */
@Service
public class ConfigServiceImpl implements IConfigService {
    @Autowired
    private ConfigMapper configMapper;

    /**
     * 查询配置表
     *
     * @param configCode 配置表CODE
     * @return 配置表
     */
    @Override
    public ConfigDTO selectConfigByConfigCode(String configCode) {
        return configMapper.selectConfigByConfigCode(configCode);
    }

    /**
     * 查询配置表列表
     *
     * @param configDTO 配置表
     * @return 配置表
     */
    @Override
    public List<ConfigDTO> selectConfigList(ConfigDTO configDTO) {
        Config config = new Config();
        BeanUtils.copyProperties(configDTO, config);
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增配置表
     *
     * @param configDTO 配置表
     * @return 结果
     */
    @Override
    public ConfigDTO insertConfig(ConfigDTO configDTO) {
        Config config = new Config();
        BeanUtils.copyProperties(configDTO, config);
        config.setCreateBy(SecurityUtils.getUserId());
        config.setCreateTime(DateUtils.getNowDate());
        config.setUpdateTime(DateUtils.getNowDate());
        config.setUpdateBy(SecurityUtils.getUserId());
        config.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        configMapper.insertConfig(config);
        configDTO.setConfigId(config.getConfigId());
        return configDTO;
    }

    /**
     * 修改配置表
     *
     * @param configDTO 配置表
     * @return 结果
     */
    @Override
    public int updateConfig(ConfigDTO configDTO) {
        Config config = new Config();
        BeanUtils.copyProperties(configDTO, config);
        config.setUpdateTime(DateUtils.getNowDate());
        config.setUpdateBy(SecurityUtils.getUserId());
        return configMapper.updateConfig(config);
    }

    /**
     * 逻辑批量删除配置表
     *
     * @param configIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteConfigByConfigIds(List<Long> configIds) {
        return configMapper.logicDeleteConfigByConfigIds(configIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除配置表信息
     *
     * @param configId 配置表主键
     * @return 结果
     */
    @Override
    public int deleteConfigByConfigId(Long configId) {
        return configMapper.deleteConfigByConfigId(configId);
    }

    /**
     * 根据CONFIG_CODE获取CONFIG_VALUE
     *
     * @param configCode
     * @return
     */
    @Override
    public Integer getValueByCode(String configCode) {
        return configMapper.selectConfigValueByCode(configCode);
    }

    /**
     * 逻辑删除配置表信息
     *
     * @param configDTO 配置表
     * @return 结果
     */
    @Override
    public int logicDeleteConfigByConfigId(ConfigDTO configDTO) {
        Config config = new Config();
        config.setConfigId(configDTO.getConfigId());
        config.setUpdateTime(DateUtils.getNowDate());
        config.setUpdateBy(SecurityUtils.getUserId());
        return configMapper.logicDeleteConfigByConfigId(config);
    }

    /**
     * 物理删除配置表信息
     *
     * @param configDTO 配置表
     * @return 结果
     */

    @Override
    public int deleteConfigByConfigId(ConfigDTO configDTO) {
        Config config = new Config();
        BeanUtils.copyProperties(configDTO, config);
        return configMapper.deleteConfigByConfigId(config.getConfigId());
    }

    /**
     * 物理批量删除配置表
     *
     * @param configDtos 需要删除的配置表主键
     * @return 结果
     */

    @Override
    public int deleteConfigByConfigIds(List<ConfigDTO> configDtos) {
        List<Long> stringList = new ArrayList();
        for (ConfigDTO configDTO : configDtos) {
            stringList.add(configDTO.getConfigId());
        }
        return configMapper.deleteConfigByConfigIds(stringList);
    }

    /**
     * 批量新增配置表信息
     *
     * @param configDtos 配置表对象
     */

    public int insertConfigs(List<ConfigDTO> configDtos) {
        List<Config> configList = new ArrayList();

        for (ConfigDTO configDTO : configDtos) {
            Config config = new Config();
            BeanUtils.copyProperties(configDTO, config);
            config.setCreateBy(SecurityUtils.getUserId());
            config.setCreateTime(DateUtils.getNowDate());
            config.setUpdateTime(DateUtils.getNowDate());
            config.setUpdateBy(SecurityUtils.getUserId());
            config.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            configList.add(config);
        }
        return configMapper.batchConfig(configList);
    }

    /**
     * 批量修改配置表信息
     *
     * @param configDtos 配置表对象
     */

    public int updateConfigs(List<ConfigDTO> configDtos) {
        List<Config> configList = new ArrayList();

        for (ConfigDTO configDTO : configDtos) {
            Config config = new Config();
            BeanUtils.copyProperties(configDTO, config);
            config.setCreateBy(SecurityUtils.getUserId());
            config.setCreateTime(DateUtils.getNowDate());
            config.setUpdateTime(DateUtils.getNowDate());
            config.setUpdateBy(SecurityUtils.getUserId());
            configList.add(config);
        }
        return configMapper.updateConfigs(configList);
    }
}

