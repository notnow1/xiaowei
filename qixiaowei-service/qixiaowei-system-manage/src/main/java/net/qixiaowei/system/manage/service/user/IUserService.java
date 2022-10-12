package net.qixiaowei.system.manage.service.user;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.api.vo.user.UserInfoVO;


/**
* UserService接口
* @author hzk
* @since 2022-10-05
*/
public interface IUserService{

    LoginUserVO getUserByUserAccount(String userAccount);

    UserInfoVO getInfo();

    /**
    * 查询用户表
    *
    * @param userId 用户表主键
    * @return 用户表
    */
    UserDTO selectUserByUserId(Long userId);

    /**
    * 查询用户表列表
    *
    * @param userDTO 用户表
    * @return 用户表集合
    */
    List<UserDTO> selectUserList(UserDTO userDTO);

    /**
    * 新增用户表
    *
    * @param userDTO 用户表
    * @return 结果
    */
    int insertUser(UserDTO userDTO);

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
    int logicDeleteUserByUserIds(List<Long> userIds);

    /**
    * 逻辑删除用户表信息
    *
    * @param userDTO
    * @return 结果
    */
    int logicDeleteUserByUserId(UserDTO userDTO);

}
