package net.qixiaowei.system.manage.api.domain.user;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 用户表
* @author hzk
* @since 2022-10-05
*/
@Data
@Accessors(chain = true)
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  userId;
     /**
     * 员工ID
     */
     private  Long  employeeId;
     /**
     * 用户帐号
     */
     private  String  userAccount;
     /**
     * 密码
     */
     private  String  password;
     /**
     * 用户名称
     */
     private  String  userName;
     /**
     * 手机号码
     */
     private  String  mobilePhone;
     /**
     * 邮箱
     */
     private  String  email;
     /**
     * 头像
     */
     private  String  avatar;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

