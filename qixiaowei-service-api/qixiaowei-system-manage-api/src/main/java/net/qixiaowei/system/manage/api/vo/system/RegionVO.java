package net.qixiaowei.system.manage.api.vo.system;

import lombok.Data;

/**
* 区域表
* @author hzk
* @since 2022-10-20
*/
@Data
public class RegionVO {

    /**
    * ID
    */
    private  Long regionId;
    /**
    * 父级区域ID
    */
    private  Long parentRegionId;
    /**
    * 区域名称
    */
    private  String regionName;

}

