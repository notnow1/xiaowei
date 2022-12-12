package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 奖金发放对象表
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPayObjectsDTO {

    //查询检验
    public interface QueryBonusPayObjectsDTO extends Default{

    }
    //新增检验
    public interface AddBonusPayObjectsDTO extends Default{

    }

    //删除检验
    public interface DeleteBonusPayObjectsDTO extends Default{

    }
    //修改检验
    public interface UpdateBonusPayObjectsDTO extends Default{

    }
    /**
    * ID
    */
    private  Long bonusPayObjectsId;
    /**
    * 奖金发放申请ID
    */
    private  Long bonusPayApplicationId;
    /**
    * 奖金发放对象:1部门;2员工
    */
    private  Integer bonusPayObject;
    /**
    * 奖金发放对象ID
    */
    private  Long bonusPayObjectId;
    /**
     * 奖金发放对象名称
     */
    private  String bonusPayObjectName;
    /**
    * 奖项金额
    */
    private  BigDecimal awardAmount;
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
    /**
    * 租户ID
    */
    private  Long tenantId;

}

