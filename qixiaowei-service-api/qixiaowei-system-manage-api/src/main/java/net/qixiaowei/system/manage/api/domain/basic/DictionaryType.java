package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 字典类型表
* @author TANGMICHI
* @since 2022-10-15
*/
@Data
@Accessors(chain = true)
public class DictionaryType extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  dictionaryTypeId;
     /**
     * 字典类型
     */
     private  String  dictionaryType;
     /**
     * 字典名称
     */
     private  String  dictionaryName;
     /**
     * 菜单0层级名称
     */
     private  String  menuZerothName;
     /**
     * 菜单1层级名称
     */
     private  String  menuFirstName;
     /**
     * 菜单2层级名称
     */
     private  String  menuSecondName;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

