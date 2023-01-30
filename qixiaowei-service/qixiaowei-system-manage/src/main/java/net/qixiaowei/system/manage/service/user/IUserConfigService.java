package net.qixiaowei.system.manage.service.user;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.user.UserConfigDTO;
import net.qixiaowei.system.manage.api.vo.user.UserConfigVO;


/**
 * UserConfigService接口
 *
 * @author hzk
 * @since 2023-01-30
 */
public interface IUserConfigService {
    /**
     * 查询用户配置表
     *
     * @param userConfigId 用户配置表主键
     * @return 用户配置表
     */
    UserConfigDTO selectUserConfigByUserConfigId(Long userConfigId);

    /**
     * 查询用户配置表列表
     *
     * @param userConfigDTO 用户配置表
     * @return 用户配置表集合
     */
    List<UserConfigVO> selectUserConfigList(UserConfigDTO userConfigDTO);

    /**
     * 初始化用户配置
     *
     * @param userId 用户ID
     * @return
     */
    void initUserConfig(Long userId);

    /**
     * 新增用户配置表
     *
     * @param userConfigDTO 用户配置表
     * @return 结果
     */
    UserConfigDTO insertUserConfig(UserConfigDTO userConfigDTO);

    /**
     * 修改用户配置表
     *
     * @param userConfigDTO 用户配置表
     * @return 结果
     */
    int updateUserConfig(UserConfigDTO userConfigDTO);

    /**
     * 批量修改用户配置表
     *
     * @param userConfigDtos 用户配置表
     * @return 结果
     */
    int updateUserConfigs(List<UserConfigDTO> userConfigDtos);

    /**
     * 批量新增用户配置表
     *
     * @param userConfigDtos 用户配置表
     * @return 结果
     */
    int insertUserConfigs(List<UserConfigDTO> userConfigDtos);

    /**
     * 逻辑批量删除用户配置表
     *
     * @param userConfigIds 需要删除的用户配置表集合
     * @return 结果
     */
    int logicDeleteUserConfigByUserConfigIds(List<Long> userConfigIds);

    /**
     * 逻辑删除用户配置表信息
     *
     * @param userConfigDTO
     * @return 结果
     */
    int logicDeleteUserConfigByUserConfigId(UserConfigDTO userConfigDTO);

    /**
     * 批量删除用户配置表
     *
     * @param UserConfigDtos
     * @return 结果
     */
    int deleteUserConfigByUserConfigIds(List<UserConfigDTO> UserConfigDtos);

    /**
     * 逻辑删除用户配置表信息
     *
     * @param userConfigDTO
     * @return 结果
     */
    int deleteUserConfigByUserConfigId(UserConfigDTO userConfigDTO);


    /**
     * 删除用户配置表信息
     *
     * @param userConfigId 用户配置表主键
     * @return 结果
     */
    int deleteUserConfigByUserConfigId(Long userConfigId);
}
