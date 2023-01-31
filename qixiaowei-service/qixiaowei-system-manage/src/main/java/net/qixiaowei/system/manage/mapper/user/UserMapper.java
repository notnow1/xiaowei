package net.qixiaowei.system.manage.mapper.user;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Set;


/**
 * UserMapper接口
 *
 * @author hzk
 * @since 2022-10-05
 */
public interface UserMapper {
    /**
     * 校验用户帐号是否唯一
     *
     * @param userAccount 用户帐号
     * @return 结果
     */
    int checkUserAccountUnique(@Param("userAccount") String userAccount);

    /**
     * 校验用户是否唯一
     *
     * @param userDTO 用户帐号
     * @return 结果
     */
    List<UserDTO> checkUserUnique(@Param("user") UserDTO userDTO);

    /**
     * 校验手机号码是否唯一
     *
     * @param mobilePhone 手机号码
     * @return 结果
     */
    UserDTO checkMobilePhoneUnique(@Param("mobilePhone") String mobilePhone);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    UserDTO checkEmailUnique(@Param("email") String email);

    /**
     * 查询用户表
     *
     * @param userAccount 用户帐号
     * @param tenantId 租户ID
     * @return 用户表
     */
    UserDTO selectUserByUserAccountAndTenantId(@Param("userAccount") String userAccount, @Param("tenantId") Long tenantId);

    /**
     * 查询用户表
     *
     * @param userAccount 用户帐号
     * @return 用户表
     */
    UserDTO selectUserByUserAccount(@Param("userAccount") String userAccount);

    /**
     * 根据人员查询用户表
     *
     * @param employeeId 用户表主键
     * @return 用户表
     */
    UserDTO selectUserByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据人员id集合批量查询用户表
     *
     * @param employeeIds 人员id集合
     * @return 用户表
     */
    List<UserDTO> selectUserByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

    /**
     * 根据人员查询用户表
     *
     * @param userId 用户表主键
     * @return 用户表
     */
    UserDTO selectUserByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID集合查询用户表
     *
     * @param userIds 需要查询的数据主键集合
     * @return 结果
     */
    List<UserDTO> selectUserListByUserIds(@Param("userIds") Set<Long> userIds);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<UserDTO> selectAllocatedList(@Param("user") User user);

    /**
     * 查询用户表列表
     *
     * @param user 用户表
     * @return 用户表集合
     */
    List<UserDTO> selectUserList(@Param("user") User user);

    /**
     * 新增用户表
     *
     * @param user 用户表
     * @return 结果
     */
    int insertUser(@Param("user") User user);

    /**
     * 修改用户表
     *
     * @param user 用户表
     * @return 结果
     */
    int updateUser(@Param("user") User user);

    /**
     * 批量修改用户表
     *
     * @param userList 用户表
     * @return 结果
     */
    int updateUsers(@Param("userList") List<User> userList);

    /**
     * 逻辑删除用户表
     *
     * @param user
     * @return 结果
     */
    int logicDeleteUserByUserId(@Param("user") User user);

    /**
     * 逻辑批量删除用户表
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteUserByUserIds(@Param("userIds") Set<Long> userIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除用户表
     *
     * @param userId 用户表主键
     * @return 结果
     */
    int deleteUserByUserId(@Param("userId") Long userId);

    /**
     * 物理批量删除用户表
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteUserByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 批量新增用户表
     *
     * @param Users 用户表列表
     * @return 结果
     */
    int batchUser(@Param("users") List<User> Users);
}
