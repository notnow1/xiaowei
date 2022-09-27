package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 默认行业
* @author Graves
* @since 2022-09-26
*/
@Data
@Accessors(chain = true)
public class IndustryDefault extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  industryId;
     /**
     * 父级行业ID
     */
     private  Long  parentIndustryId;
     /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
     private  String  ancestors;
     /**
     * 行业编码
     */
     private  String  industryCode;
     /**
     * 行业名称
     */
     private  String  industryName;
     /**
     * 层级
     */
     private  Integer  level;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

