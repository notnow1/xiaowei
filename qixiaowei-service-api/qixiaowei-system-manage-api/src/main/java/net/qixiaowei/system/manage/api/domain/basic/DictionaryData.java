package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 字典数据表
* @author TANGMICHI
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class DictionaryData extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  dictionaryDataId;
     /**
     * 字典类型ID
     */
     private  Long  dictionaryTypeId;
     /**
     * 字典标签
     */
     private  String  dictionaryLabel;
     /**
     * 字典值
     */
     private  String  dictionaryValue;
     /**
     * 默认标记:0否;1是
     */
     private  Integer  defaultFlag;
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

