package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.Config;
import net.qixiaowei.system.manage.api.dto.basic.ConfigDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * ConfigMapper接口
 *
 * @author Graves
 * @since 2022-10-18
 */
public interface ConfigMapper {
    /**
     * 查询配置表
     *
     * @param configCode 配置表CODE
     * @return 配置表
     */
    ConfigDTO selectConfigByConfigCode(@Param("configCode") String configCode);

    /**
     * 查询配置表列表
     *
     * @param config 配置表
     * @return 配置表集合
     */
    List<ConfigDTO> selectConfigList(@Param("config") Config config);

    /**
     * 新增配置表
     *
     * @param config 配置表
     * @return 结果
     */
    int insertConfig(@Param("config") Config config);

    /**
     * 修改配置表
     *
     * @param config 配置表
     * @return 结果
     */
    int updateConfig(@Param("config") Config config);

    /**
     * 批量修改配置表
     *
     * @param configList 配置表
     * @return 结果
     */
    int updateConfigs(@Param("configList") List<Config> configList);

    /**
     * 逻辑删除配置表
     *
     * @param config
     * @return 结果
     */
    int logicDeleteConfigByConfigId(@Param("config") Config config);

    /**
     * 逻辑批量删除配置表
     *
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteConfigByConfigIds(@Param("configIds") List<Long> configIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除配置表
     *
     * @param configId 配置表主键
     * @return 结果
     */
    int deleteConfigByConfigId(@Param("configId") Long configId);

    /**
     * 物理批量删除配置表
     *
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteConfigByConfigIds(@Param("configIds") List<Long> configIds);

    /**
     * 批量新增配置表
     *
     * @param Configs 配置表列表
     * @return 结果
     */
    int batchConfig(@Param("configs") List<Config> Configs);

    /**
     * 根据CONFIG_CODE获取CONFIG_VALUE
     *
     * @param configCode
     * @return
     */
    Integer selectConfigValueByCode(@Param("configCode") String configCode);
}
