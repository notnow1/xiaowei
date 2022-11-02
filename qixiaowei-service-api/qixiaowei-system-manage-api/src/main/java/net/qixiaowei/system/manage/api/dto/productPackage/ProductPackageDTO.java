package net.qixiaowei.system.manage.api.dto.productPackage;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 产品包
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductPackageDTO {

    //查询检验
    public interface QueryProductPackageDTO extends Default{

    }
    //新增检验
    public interface AddProductPackageDTO extends Default{

    }

    //删除检验
    public interface DeleteProductPackageDTO extends Default{

    }
    //修改检验
    public interface UpdateProductPackageDTO extends Default{

    }
    /**
    * ID
    */
    private  Long productPackageId;
    /**
    * 产品包名
    */
    private  String productPackageName;
    /**
    * 产品包描述
    */
    private  String productPackageDescription;
    /**
    * 备注
    */
    private  String remark;
    /**
    * 排序
    */
    private  Integer sort;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

    private List<Map<Long,String>> list;
}

