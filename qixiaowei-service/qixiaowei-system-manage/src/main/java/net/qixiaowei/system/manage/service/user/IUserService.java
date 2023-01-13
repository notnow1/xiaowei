package net.qixiaowei.system.manage.service.user;

import java.util.List;
import java.util.Set;

import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.api.dto.user.AuthRolesDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.dto.user.UserUpdatePasswordDTO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.api.vo.user.UserInfoVO;
import net.qixiaowei.system.manage.api.vo.user.UserProfileVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * UserService接口
 *
 * @author hzk
 * @since 2022-10-05
 */
public interface IUserService {

    LoginUserVO getUserByUserAccount(String userAccount, String domain);

    UserInfoVO getInfo();

    /**
     * 查询用户资料
     *
     * @return 用户个人信息对象
     */
    UserProfileVO getProfile();

    /**
     * 修改用户资料
     *
     * @param avatarFile 头像文件
     * @param userDTO    用户修改对象
     * @return 结果
     */
    UserProfileVO editProfile(MultipartFile avatarFile, UserDTO userDTO);

    /**
     * 查询用户表
     *
     * @param userId 用户表主键
     * @return 用户表
     */
    UserDTO selectUserByUserId(Long userId);

    /**
     * 根据用户ID查询用户角色列表
     *
     * @param userId 用户表主键
     * @return 用户表
     */
    List<RoleDTO> selectUserRolesByUserId(Long userId);

    /**
     * 查询用户表列表
     *
     * @param userDTO 用户表
     * @return 用户表集合
     */
    List<UserDTO> selectUserList(UserDTO userDTO);

    /**
     * 查询用户表列表
     *
     * @param userIds 用户ids
     * @return 用户表集合
     */
    List<UserDTO> getUsersByUserIds(Set<Long> userIds);

    /**
     * 新增用户表
     *
     * @param userDTO 用户表
     * @return 结果
     */
    UserDTO insertUser(UserDTO userDTO);

    /**
     * 修改用户表
     *
     * @param userDTO 用户表
     * @return 结果
     */
    int updateUser(UserDTO userDTO);

    /**
     * 批量修改用户表
     *
     * @param userDtos 用户表
     * @return 结果
     */
    int updateUsers(List<UserDTO> userDtos);

    /**
     * 批量新增用户表
     *
     * @param userDtos 用户表
     * @return 结果
     */
    int insertUsers(List<UserDTO> userDtos);

    /**
     * 逻辑批量删除用户表
     *
     * @param userIds 需要删除的用户ID集合
     * @return 结果
     */
    int logicDeleteUserByUserIds(Set<Long> userIds);

    /**
     * 逻辑删除用户表信息
     *
     * @param userDTO
     * @return 结果
     */
    int logicDeleteUserByUserId(UserDTO userDTO);

    /**
     * 重置密码
     *
     * @param userDTO
     * @return 结果
     */
    int resetPwd(UserDTO userDTO);

    /**
     * 重置用户密码
     *
     * @param userUpdatePasswordDTO 用户更新密码实体
     * @return 结果
     */
    int resetUserPwd(UserUpdatePasswordDTO userUpdatePasswordDTO);

    /**
     * 用户授权角色
     */
    void authRoles(AuthRolesDTO authRolesDTO);

    /**
     * 查询未分配用户员工列表
     */
    List<EmployeeDTO> unallocatedEmployees(Long userId);

}
