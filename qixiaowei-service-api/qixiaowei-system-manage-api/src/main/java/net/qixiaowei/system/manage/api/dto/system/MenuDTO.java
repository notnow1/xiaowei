package net.qixiaowei.system.manage.api.dto.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 菜单表
 *
 * @author hzk
 * @since 2022-10-07
 */
@Data
@Accessors(chain = true)
public class MenuDTO {

    //查询检验
    public interface QueryMenuDTO extends Default {

    }

    //新增检验
    public interface AddMenuDTO extends Default {

    }

    //新增检验
    public interface DeleteMenuDTO extends Default {

    }

    //修改检验
    public interface UpdateMenuDTO extends Default {

    }

    /**
     * ID
     */
    private Long menuId;
    /**
     * 父级菜单ID
     */
    private Long parentMenuId;
    /**
     * 1目录;2菜单;3按钮
     */
    private Integer menuType;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 产品包ID
     */
    private Long productPackageId;
    /**
     * 产品包名
     */
    private  String productPackageName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 路由参数
     */
    private String query;
    /**
     * 外链标记:0否;1是
     */
    private Integer externalLinkFlag;
    /**
     * 缓存标记:0否;1是
     */
    private Integer cacheFlag;
    /**
     * 菜单可见状态:0否;1是
     */
    private Integer visibleFlag;
    /**
     * 权限编码
     */
    private String permissionCode;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 子菜单
     */
    private List<MenuDTO> children = new ArrayList<MenuDTO>();

}

