package net.qixiaowei.operate.cloud.api.domain.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 区域表
* @author Graves
* @since 2022-10-07
*/
@Data
@Accessors(chain = true)
public class Area extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  areaId;
     /**
     * 区域编码
     */
     private  String  areaCode;
     /**
     * 区域名称
     */
     private  String  areaName;
     /**
     * 地区ID集合,用英文逗号隔开
     */
     private  String  regionIds;
     /**
     * 地区名称集合,用英文逗号隔开
     */
     private  String  regionNames;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

