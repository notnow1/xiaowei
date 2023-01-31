package net.qixiaowei.system.manage.mapper.user;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.user.UserConfig;
import net.qixiaowei.system.manage.api.dto.user.UserConfigDTO;
import net.qixiaowei.system.manage.api.vo.user.UserConfigVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * UserConfigMapper接口
 *
 * @author hzk
 * @since 2023-01-30
 */
public interface UserConfigMapper {
    /**
     * 查询用户配置表
     *
     * @param userConfigId 用户配置表主键
     * @return 用户配置表
     */
    UserConfigDTO selectUserConfigByUserConfigId(@Param("userConfigId") Long userConfigId);


    /**
     * 批量查询用户配置表
     *
     * @param userConfigIds 用户配置表主键集合
     * @return 用户配置表
     */
    List<UserConfigDTO> selectUserConfigByUserConfigIds(@Param("userConfigIds") List<Long> userConfigIds);

    /**
     * 查询用户配置表列表
     *
     * @param userConfig 用户配置表
     * @return 用户配置表集合
     */
    List<UserConfigVO> selectUserConfigList(@Param("userConfig") UserConfig userConfig);

    /**
     * 新增用户配置表
     *
     * @param userConfig 用户配置表
     * @return 结果
     */
    int insertUserConfig(@Param("userConfig") UserConfig userConfig);

    /**
     * 修改用户配置表
     *
     * @param userConfig 用户配置表
     * @return 结果
     */
    int updateUserConfig(@Param("userConfig") UserConfig userConfig);

    /**
     * 批量修改用户配置表
     *
     * @param userConfigList 用户配置表
     * @return 结果
     */
    int updateUserConfigs(@Param("userConfigList") List<UserConfig> userConfigList);

    /**
     * 逻辑删除用户配置表
     *
     * @param userConfig
     * @return 结果
     */
    int logicDeleteUserConfigByUserConfigId(@Param("userConfig") UserConfig userConfig);

    /**
     * 逻辑批量删除用户配置表
     *
     * @param userConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteUserConfigByUserConfigIds(@Param("userConfigIds") List<Long> userConfigIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除用户配置表
     *
     * @param userConfigId 用户配置表主键
     * @return 结果
     */
    int deleteUserConfigByUserConfigId(@Param("userConfigId") Long userConfigId);

    /**
     * 物理批量删除用户配置表
     *
     * @param userConfigIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteUserConfigByUserConfigIds(@Param("userConfigIds") List<Long> userConfigIds);

    /**
     * 批量新增用户配置表
     *
     * @param UserConfigs 用户配置表列表
     * @return 结果
     */
    int batchUserConfig(@Param("userConfigs") List<UserConfig> UserConfigs);
}
