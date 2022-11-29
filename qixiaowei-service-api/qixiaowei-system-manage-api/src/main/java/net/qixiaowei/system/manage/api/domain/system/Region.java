package net.qixiaowei.system.manage.api.domain.system;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* 区域表
* @author hzk
* @since 2022-10-20
*/
@Data
@Accessors(chain = true)
public class Region extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  regionId;
     /**
     * 父级区域ID
     */
     private  Long  parentRegionId;
     /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
     private  String  ancestors;
     /**
     * 区域名称
     */
     private  String  regionName;
     /**
     * 省级区划编号
     */
     private  String  provinceCode;
     /**
     * 省级名称
     */
     private  String  provinceName;
     /**
     * 市级区划编号
     */
     private  String  cityCode;
     /**
     * 市级名称
     */
     private  String  cityName;
     /**
     * 区级区划编号
     */
     private  String  districtCode;
     /**
     * 区级名称
     */
     private  String  districtName;
     /**
     * 镇级区划编号
     */
     private  String  townCode;
     /**
     * 镇级名称
     */
     private  String  townName;
     /**
     * 村级区划编号
     */
     private  String  villageCode;
     /**
     * 村级名称
     */
     private  String  villageName;
     /**
     * 层级
     */
     private  Integer  level;
     /**
     * 排序
     */
     private  Integer  sort;

}

