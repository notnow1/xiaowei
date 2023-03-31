package net.qixiaowei.integration.security.utils;

import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.SpringUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import net.qixiaowei.system.manage.api.vo.user.UserProfileVO;


import java.util.*;

/**
 * 用户工具类
 *
 * @author hzk
 */
public class UserUtils {


    /**
     * 设置用户缓存
     *
     * @param userId        用户ID
     * @param userProfileVO 用户资料
     */
    public static void setUserCache(Long userId, UserProfileVO userProfileVO) {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(userId), userProfileVO);
    }

    /**
     * 获取用户缓存
     * userId 用户ID
     *
     * @return UserProfileVO 用户资料
     */
    public static UserProfileVO getUserCache(Long userId) {
        if (StringUtils.isNull(userId) || 0L == userId) {
            return null;
        }
        UserProfileVO userProfileVO = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(userId));
        if (StringUtils.isNull(userProfileVO)) {
            //初始化缓存
            RemoteUserService remoteUserService = SpringUtils.getBean(RemoteUserService.class);
            R<UserProfileVO> userProfileVOR = remoteUserService.initUserCache(userId, SecurityConstants.INNER);
            if (R.SUCCESS == userProfileVOR.getCode()) {
                userProfileVO = userProfileVOR.getData();
            }
        }
        return userProfileVO;
    }

    /**
     * 获取用户集合缓存
     * userIds 用户ID集合
     *
     * @return List<UserProfileVO> 用户资料集合
     */
    public static List<UserProfileVO> getUsers(Set<Long> userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<UserProfileVO> userProfileVOS = new ArrayList<>(userIds.size());
        userIds.forEach(userId -> {
            UserProfileVO userProfileVO = getUserCache(userId);
            if (StringUtils.isNotNull(userProfileVO)) {
                userProfileVOS.add(userProfileVO);
            }
        });
        return userProfileVOS;
    }

    /**
     * 获取用户集合缓存
     * userIds 用户ID集合
     *
     * @return Map<Long, String> 用户ID为key、员工姓名为value的集合
     */
    public static Map<Long, String> getEmployeeNameMap(Set<Long> userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        Map<Long, String> userEmployeeNameMap = new HashMap<>(userIds.size());
        userIds.forEach(userId -> {
            UserProfileVO userProfileVO = getUserCache(userId);
            if (Objects.nonNull(userProfileVO)) {
                userEmployeeNameMap.put(userId, userProfileVO.getEmployeeName());
            } else {
                userEmployeeNameMap.put(userId, "");
            }
        });
        return userEmployeeNameMap;
    }

    /**
     * 获取用户员工姓名
     * userId 用户ID
     *
     * @return String 用户绑定的员工姓名
     */
    public static String getEmployeeName(Long userId) {
        if (StringUtils.isNull(userId)) {
            return "";
        }
        UserProfileVO userProfileVO = getUserCache(userId);
        if (StringUtils.isNotNull(userProfileVO)) {
            return userProfileVO.getEmployeeName();
        }
        return "";
    }

    /**
     * 删除指定用户缓存
     * userId 用户ID
     */
    public static void removeUserCache(Long userId) {
        SpringUtils.getBean(RedisService.class).deleteObject(getCacheKey(userId));
    }

    /**
     * 设置cache key
     *
     * @param userId 用户ID
     * @return 缓存键key
     */
    public static String getCacheKey(Long userId) {
        return CacheConstants.USER_KEY + userId;
    }
}
