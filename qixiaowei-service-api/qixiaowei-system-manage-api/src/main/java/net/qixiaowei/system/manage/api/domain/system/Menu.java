package net.qixiaowei.system.manage.api.domain.system;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 菜单表
* @author hzk
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  menuId;
     /**
     * 父级菜单ID
     */
     private  Long  parentMenuId;
     /**
     * 1目录;2菜单;3按钮
     */
     private  Integer  menuType;
     /**
     * 菜单名称
     */
     private  String  menuName;
     /**
     * 产品包ID
     */
     private  Long  productPackageId;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 路由地址
     */
     private  String  path;
     /**
     * 组件路径
     */
     private  String  component;
     /**
     * 路由参数
     */
     private  String  query;
     /**
     * 外链标记:0否;1是
     */
     private  Integer  externalLinkFlag;
     /**
     * 缓存标记:0否;1是
     */
     private  Integer  cacheFlag;
     /**
     * 菜单可见状态:0否;1是
     */
     private  Integer  visibleFlag;
     /**
     * 权限编码
     */
     private  String  permissionCode;
     /**
     * 菜单图标
     */
     private  String  icon;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

