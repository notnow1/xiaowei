package net.qixiaowei.operate.cloud.api.dto.product;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 产品文件表
* @author TANGMICHI
* @since 2022-10-09
*/
@Data
@Accessors(chain = true)
public class ProductFileDTO {

    //查询检验
    public interface QueryProductFileDTO extends Default{

    }
    //新增检验
    public interface AddProductFileDTO extends Default{

    }

    //删除检验
    public interface DeleteProductFileDTO extends Default{

    }
    //修改检验
    public interface UpdateProductFileDTO extends Default{

    }
    /**
    * ID
    */
    private  Long productFileId;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
    * 产品文件名称
    */
    private  String productFileName;
    /**
    * 产品文件格式
    */
    private  String productFileFormat;
    /**
    * 产品文件大小
    */
    private  Long productFileSize;
    /**
    * 产品文件路径
    */
    private  String productFilePath;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

