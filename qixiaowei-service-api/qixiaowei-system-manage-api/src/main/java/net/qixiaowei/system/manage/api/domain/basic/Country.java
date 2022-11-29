package net.qixiaowei.system.manage.api.domain.basic;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 国家表
* @author TANGMICHI
* @since 2022-10-20
*/
@Data
@Accessors(chain = true)
public class Country extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  countryId;
     /**
     * 父级国家ID
     */
     private  Long  parentCountryId;
     /**
     * 国家名称
     */
     private  String  countryName;
     /**
     * 排序
     */
     private  Integer  sort;

}

