package net.qixiaowei.system.manage.api.vo.system;

import cn.hutool.core.lang.tree.Tree;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author hzk
 * @Date 2022-10-08 16:25
 **/
@Data
public class RoleMenuTreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Long> checkedKeys;

    private List<Tree<Long>> menus;

}
