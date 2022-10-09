package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 职级体系表
* @author Graves
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class OfficialRankSystem extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  officialRankSystemId;
     /**
     * 职级体系名称
     */
     private  String  officialRankSystemName;
     /**
     * 级别前缀编码
     */
     private  String  rankPrefixCode;
     /**
     * 起始级别
     */
     private  Integer  rankStart;
     /**
     * 终止级别
     */
     private  Integer  rankEnd;
     /**
     * 职级分解维度:1部门;2区域;3省份;4产品
     */
     private  Integer  rankDecomposeDimension;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

