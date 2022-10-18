package net.qixiaowei.system.manage.service.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.basic.ConfigDTO;


/**
 * ConfigService接口
 *
 * @author Graves
 * @since 2022-10-18
 */
public interface IConfigService {
    /**
     * 查询配置表
     *
     * @param configCode 配置表CODE
     * @return 配置表
     */
    ConfigDTO selectConfigByConfigCode(String configCode);

    /**
     * 查询配置表列表
     *
     * @param configDTO 配置表
     * @return 配置表集合
     */
    List<ConfigDTO> selectConfigList(ConfigDTO configDTO);

    /**
     * 新增配置表
     *
     * @param configDTO 配置表
     * @return 结果
     */
    ConfigDTO insertConfig(ConfigDTO configDTO);

    /**
     * 修改配置表
     *
     * @param configDTO 配置表
     * @return 结果
     */
    int updateConfig(ConfigDTO configDTO);

    /**
     * 批量修改配置表
     *
     * @param configDtos 配置表
     * @return 结果
     */
    int updateConfigs(List<ConfigDTO> configDtos);

    /**
     * 批量新增配置表
     *
     * @param configDtos 配置表
     * @return 结果
     */
    int insertConfigs(List<ConfigDTO> configDtos);

    /**
     * 逻辑批量删除配置表
     *
     * @param configIds 需要删除的配置表集合
     * @return 结果
     */
    int logicDeleteConfigByConfigIds(List<Long> configIds);

    /**
     * 逻辑删除配置表信息
     *
     * @param configDTO
     * @return 结果
     */
    int logicDeleteConfigByConfigId(ConfigDTO configDTO);

    /**
     * 批量删除配置表
     *
     * @param ConfigDtos
     * @return 结果
     */
    int deleteConfigByConfigIds(List<ConfigDTO> ConfigDtos);

    /**
     * 逻辑删除配置表信息
     *
     * @param configDTO
     * @return 结果
     */
    int deleteConfigByConfigId(ConfigDTO configDTO);


    /**
     * 删除配置表信息
     *
     * @param configId 配置表主键
     * @return 结果
     */
    int deleteConfigByConfigId(Long configId);

    /**
     * 根据CONFIG_CODE获取CONFIG_VALUE
     *
     * @param configCode
     * @return
     */
    int getValueByCode(String configCode);
}
