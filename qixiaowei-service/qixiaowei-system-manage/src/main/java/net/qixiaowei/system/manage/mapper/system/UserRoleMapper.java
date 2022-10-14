package net.qixiaowei.system.manage.mapper.system;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.system.UserRole;
import net.qixiaowei.system.manage.api.dto.system.UserRoleDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* UserRoleMapper接口
* @author hzk
* @since 2022-10-07
*/
public interface UserRoleMapper{
    /**
    * 查询用户角色表
    *
    * @param userRoleId 用户角色表主键
    * @return 用户角色表
    */
    UserRoleDTO selectUserRoleByUserRoleId(@Param("userRoleId")Long userRoleId);

    /**
     * 查询用户角色表列表
     *
     * @param userId 用户ID
     * @return 用户角色表集合
     */
    List<UserRoleDTO> selectUserRoleListByUserId(@Param("userId")Long userId);

    /**
    * 查询用户角色表列表
    *
    * @param userRole 用户角色表
    * @return 用户角色表集合
    */
    List<UserRoleDTO> selectUserRoleList(@Param("userRole")UserRole userRole);

    /**
    * 新增用户角色表
    *
    * @param userRole 用户角色表
    * @return 结果
    */
    int insertUserRole(@Param("userRole")UserRole userRole);

    /**
    * 修改用户角色表
    *
    * @param userRole 用户角色表
    * @return 结果
    */
    int updateUserRole(@Param("userRole")UserRole userRole);

    /**
    * 批量修改用户角色表
    *
    * @param userRoleList 用户角色表
    * @return 结果
    */
    int updateUserRoles(@Param("userRoleList")List<UserRole> userRoleList);
    /**
    * 逻辑删除用户角色表
    *
    * @param userRole
    * @return 结果
    */
    int logicDeleteUserRoleByUserRoleId(@Param("userRole")UserRole userRole,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除用户角色表
    *
    * @param userRoleIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteUserRoleByUserRoleIds(@Param("userRoleIds")List<Long> userRoleIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
     * 根据用户ID集合逻辑批量删除用户角色表
     *
     * @param userIds 需要删除的用户ID数据主键集合
     * @return 结果
     */
    int logicDeleteUserRoleByUserIds(@Param("userIds")List<Long> userIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除用户角色表
    *
    * @param userRoleId 用户角色表主键
    * @return 结果
    */
    int deleteUserRoleByUserRoleId(@Param("userRoleId")Long userRoleId);

    /**
    * 物理批量删除用户角色表
    *
    * @param userRoleIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteUserRoleByUserRoleIds(@Param("userRoleIds")List<Long> userRoleIds);

    /**
    * 批量新增用户角色表
    *
    * @param UserRoles 用户角色表列表
    * @return 结果
    */
    int batchUserRole(@Param("userRoles")List<UserRole> UserRoles);
}
