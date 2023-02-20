package net.qixiaowei.system.manage.service.impl.user;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.enums.user.UserConfigType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import net.qixiaowei.system.manage.api.vo.user.UserConfigVO;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.user.UserConfig;
import net.qixiaowei.system.manage.api.dto.user.UserConfigDTO;
import net.qixiaowei.system.manage.mapper.user.UserConfigMapper;
import net.qixiaowei.system.manage.service.user.IUserConfigService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;

import javax.annotation.PostConstruct;


/**
 * UserConfigService业务层处理
 *
 * @author hzk
 * @since 2023-01-30
 */
@Service
public class UserConfigServiceImpl implements IUserConfigService {

    private static final List<Integer> statusList = Arrays.asList(0, 1);

    @Autowired
    private UserConfigMapper userConfigMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ITenantService tenantService;


    /**
     * 项目启动时，初始化用户配置到缓存
     */
    @PostConstruct
    public void init() {
        //处理待办消息
        UserConfig userConfig = new UserConfig();
        userConfig.setUserConfigType(UserConfigType.BACKUP_LOG_NOTICE.getCode());
        userConfig.setStatus(0);
        List<Long> tenantIds = tenantService.getTenantIds();
        if (StringUtils.isEmpty(tenantIds)) {
            tenantIds = new ArrayList<>();
        }
        tenantIds.add(0L);
        tenantIds.forEach(tenantId -> TenantUtils.execute(tenantId, () -> {
            List<UserConfigDTO> UserConfigDTOS = userConfigMapper.selectUserConfigs(userConfig);
            if (StringUtils.isNotEmpty(UserConfigDTOS)) {
                for (UserConfigDTO UserConfigDTO : UserConfigDTOS) {
                    Long userId = UserConfigDTO.getUserId();
                    this.setUserConfigCache(userId);
                }
            }
        }));
    }

    /**
     * 设置用户配置缓存
     */
    private void setUserConfigCache(Long userId) {
        String cacheKey = CacheConstants.USER_CONFIG_KEY + userId;
        redisService.setCacheObject(cacheKey, "");
    }

    /**
     * 清除用户配置缓存
     */
    private void clearUserConfigCache(Long userId) {
        String cacheKey = CacheConstants.USER_CONFIG_KEY + userId;
        Boolean hasKey = redisService.hasKey(cacheKey);
        if (hasKey) {
            redisService.deleteObject(cacheKey);
        }
    }

    /**
     * 查询用户配置表
     *
     * @param userConfigId 用户配置表主键
     * @return 用户配置表
     */
    @Override
    public UserConfigDTO selectUserConfigByUserConfigId(Long userConfigId) {
        return userConfigMapper.selectUserConfigByUserConfigId(userConfigId);
    }

    /**
     * 查询用户配置表列表
     *
     * @param userConfigDTO 用户配置表
     * @return 用户配置表
     */
    @Override
    public List<UserConfigVO> selectUserConfigList(UserConfigDTO userConfigDTO) {
        UserConfig userConfig = new UserConfig();
        BeanUtils.copyProperties(userConfigDTO, userConfig);
        Long userId = SecurityUtils.getUserId();
        userConfig.setUserId(userId);
        return userConfigMapper.selectUserConfigList(userConfig);
    }

    /**
     * 初始化用户配置
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public void initUserConfig(Long userId) {
        UserConfig userConfig = new UserConfig();
        userConfig.setUserId(userId);
        userConfig.setUserConfigType(UserConfigType.BACKUP_LOG_NOTICE.getCode());
        userConfig.setStatus(BusinessConstants.NORMAL);
        userConfig.setCreateBy(SecurityUtils.getUserId());
        userConfig.setCreateTime(DateUtils.getNowDate());
        userConfig.setUpdateTime(DateUtils.getNowDate());
        userConfig.setUpdateBy(SecurityUtils.getUserId());
        userConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        userConfig.setRemark(UserConfigType.BACKUP_LOG_NOTICE.getInfo());
        userConfigMapper.insertUserConfig(userConfig);
    }

    /**
     * 新增用户配置表
     *
     * @param userConfigDTO 用户配置表
     * @return 结果
     */
    @Override
    public UserConfigDTO insertUserConfig(UserConfigDTO userConfigDTO) {
        UserConfig userConfig = new UserConfig();
        BeanUtils.copyProperties(userConfigDTO, userConfig);
        userConfig.setCreateBy(SecurityUtils.getUserId());
        userConfig.setCreateTime(DateUtils.getNowDate());
        userConfig.setUpdateTime(DateUtils.getNowDate());
        userConfig.setUpdateBy(SecurityUtils.getUserId());
        userConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        userConfigMapper.insertUserConfig(userConfig);
        userConfigDTO.setUserConfigId(userConfig.getUserConfigId());
        return userConfigDTO;
    }

    /**
     * 修改用户配置表
     *
     * @param userConfigDTO 用户配置表
     * @return 结果
     */
    @Override
    public int updateUserConfig(UserConfigDTO userConfigDTO) {
        Long userConfigId = userConfigDTO.getUserConfigId();
        Integer status = userConfigDTO.getStatus();
        if (!statusList.contains(status)) {
            throw new ServiceException("状态值异常。");
        }
        UserConfigDTO userConfigByUserConfigId = userConfigMapper.selectUserConfigByUserConfigId(userConfigId);
        if (StringUtils.isNull(userConfigByUserConfigId)) {
            throw new ServiceException("用户配置不存在。");
        }
        Long userId = SecurityUtils.getUserId();
        if (!userId.equals(userConfigByUserConfigId.getUserId())) {
            throw new ServiceException("只能修改自己的配置。");
        }
        Date nowDate = DateUtils.getNowDate();
        UserConfig userConfig = new UserConfig();
        userConfig.setUserConfigId(userConfigId);
        userConfig.setUserConfigValue(userConfigDTO.getUserConfigValue());
        userConfig.setStatus(status);
        userConfig.setUpdateTime(nowDate);
        userConfig.setUpdateBy(userId);
        int i = userConfigMapper.updateUserConfig(userConfig);
        if (i > 0) {
            if (UserConfigType.BACKUP_LOG_NOTICE.getCode().equals(userConfigByUserConfigId.getUserConfigType())) {
                if (BusinessConstants.DISABLE.equals(status)) {
                    this.setUserConfigCache(userId);
                } else {
                    this.clearUserConfigCache(userId);
                }
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除用户配置表
     *
     * @param userConfigIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteUserConfigByUserConfigIds(List<Long> userConfigIds) {
        return userConfigMapper.logicDeleteUserConfigByUserConfigIds(userConfigIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除用户配置表信息
     *
     * @param userConfigId 用户配置表主键
     * @return 结果
     */
    @Override
    public int deleteUserConfigByUserConfigId(Long userConfigId) {
        return userConfigMapper.deleteUserConfigByUserConfigId(userConfigId);
    }

    /**
     * 逻辑删除用户配置表信息
     *
     * @param userConfigDTO 用户配置表
     * @return 结果
     */
    @Override
    public int logicDeleteUserConfigByUserConfigId(UserConfigDTO userConfigDTO) {
        UserConfig userConfig = new UserConfig();
        userConfig.setUserConfigId(userConfigDTO.getUserConfigId());
        userConfig.setUpdateTime(DateUtils.getNowDate());
        userConfig.setUpdateBy(SecurityUtils.getUserId());
        return userConfigMapper.logicDeleteUserConfigByUserConfigId(userConfig);
    }

    /**
     * 物理删除用户配置表信息
     *
     * @param userConfigDTO 用户配置表
     * @return 结果
     */

    @Override
    public int deleteUserConfigByUserConfigId(UserConfigDTO userConfigDTO) {
        UserConfig userConfig = new UserConfig();
        BeanUtils.copyProperties(userConfigDTO, userConfig);
        return userConfigMapper.deleteUserConfigByUserConfigId(userConfig.getUserConfigId());
    }

    /**
     * 物理批量删除用户配置表
     *
     * @param userConfigDtos 需要删除的用户配置表主键
     * @return 结果
     */

    @Override
    public int deleteUserConfigByUserConfigIds(List<UserConfigDTO> userConfigDtos) {
        List<Long> stringList = new ArrayList();
        for (UserConfigDTO userConfigDTO : userConfigDtos) {
            stringList.add(userConfigDTO.getUserConfigId());
        }
        return userConfigMapper.deleteUserConfigByUserConfigIds(stringList);
    }

    /**
     * 批量新增用户配置表信息
     *
     * @param userConfigDtos 用户配置表对象
     */

    @Override
    public int insertUserConfigs(List<UserConfigDTO> userConfigDtos) {
        List<UserConfig> userConfigList = new ArrayList();

        for (UserConfigDTO userConfigDTO : userConfigDtos) {
            UserConfig userConfig = new UserConfig();
            BeanUtils.copyProperties(userConfigDTO, userConfig);
            userConfig.setCreateBy(SecurityUtils.getUserId());
            userConfig.setCreateTime(DateUtils.getNowDate());
            userConfig.setUpdateTime(DateUtils.getNowDate());
            userConfig.setUpdateBy(SecurityUtils.getUserId());
            userConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            userConfigList.add(userConfig);
        }
        return userConfigMapper.batchUserConfig(userConfigList);
    }

    /**
     * 批量修改用户配置表信息
     *
     * @param userConfigDtos 用户配置表对象
     */

    @Override
    public int updateUserConfigs(List<UserConfigDTO> userConfigDtos) {
        List<UserConfig> userConfigList = new ArrayList();

        for (UserConfigDTO userConfigDTO : userConfigDtos) {
            UserConfig userConfig = new UserConfig();
            BeanUtils.copyProperties(userConfigDTO, userConfig);
            userConfig.setCreateBy(SecurityUtils.getUserId());
            userConfig.setCreateTime(DateUtils.getNowDate());
            userConfig.setUpdateTime(DateUtils.getNowDate());
            userConfig.setUpdateBy(SecurityUtils.getUserId());
            userConfigList.add(userConfig);
        }
        return userConfigMapper.updateUserConfigs(userConfigList);
    }

}

