package net.qixiaowei.system.manage.api.domain.system;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 角色表
* @author hzk
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  roleId;
     /**
     * 角色编码
     */
     private  String  roleCode;
     /**
     * 角色名称
     */
     private  String  roleName;
     /**
     * 数据范围:1全公司;2本部门及下属部门;3本部门;4本人及下属;5本人
     */
     private  Integer  dataScope;
     /**
     * 分配的产品包
     */
     private  String  productPackage;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

