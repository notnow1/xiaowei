package net.qixiaowei.system.manage.mapper.user;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.user.User;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* UserMapper接口
* @author hzk
* @since 2022-10-05
*/
public interface UserMapper{

    /**
     * 查询用户表
     *
     * @param userAccount 用户帐号
     * @return 用户表
     */
    UserDTO selectUserByUserAccount(@Param("userAccount") String userAccount);

    /**
    * 查询用户表
    *
    * @param userId 用户表主键
    * @return 用户表
    */
    UserDTO selectUserByUserId(@Param("userId")Long userId);

    /**
    * 查询用户表列表
    *
    * @param user 用户表
    * @return 用户表集合
    */
    List<UserDTO> selectUserList(@Param("user")User user);

    /**
    * 新增用户表
    *
    * @param user 用户表
    * @return 结果
    */
    int insertUser(@Param("user")User user);

    /**
    * 修改用户表
    *
    * @param user 用户表
    * @return 结果
    */
    int updateUser(@Param("user")User user);

    /**
    * 批量修改用户表
    *
    * @param userList 用户表
    * @return 结果
    */
    int updateUsers(@Param("userList")List<User> userList);
    /**
    * 逻辑删除用户表
    *
    * @param user
    * @return 结果
    */
    int logicDeleteUserByUserId(@Param("user")User user,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除用户表
    *
    * @param userIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteUserByUserIds(@Param("userIds")List<Long> userIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除用户表
    *
    * @param userId 用户表主键
    * @return 结果
    */
    int deleteUserByUserId(@Param("userId")Long userId);

    /**
    * 物理批量删除用户表
    *
    * @param userIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteUserByUserIds(@Param("userIds")List<Long> userIds);

    /**
    * 批量新增用户表
    *
    * @param Users 用户表列表
    * @return 结果
    */
    int batchUser(@Param("users")List<User> Users);
}
