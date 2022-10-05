package net.qixiaowei.system.manage.api.dto.basic;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 岗位表
* @author TANGMICHI
* @since 2022-09-30
*/
@Data
@Accessors(chain = true)
public class PostDTO {

    //查询检验
    public interface QueryPostDTO extends Default{

    }
    //新增检验
    public interface AddPostDTO extends Default{

    }

    //新增检验
    public interface DeletePostDTO extends Default{

    }
    //修改检验
    public interface UpdatePostDTO extends Default{

    }
    /**
    * ID
    */
    private  Long postId;
    /**
    * 岗位编码
    */
    private  String postCode;
    /**
    * 岗位名称
    */
    private  String postName;
    /**
    * 职级体系ID
    */
    private  Long officialRankSystemId;
    /**
    * 岗位职级下限
    */
    private  Integer postRankLower;
    /**
    * 岗位职级
    */
    private  Integer postRank;
    /**
    * 岗位职级上限
    */
    private  Integer postRankUpper;
    /**
    * 岗位说明书URL路径
    */
    private  String postDescription;
    /**
    * 状态:0失效;1生效
    */
    private  Integer status;
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

