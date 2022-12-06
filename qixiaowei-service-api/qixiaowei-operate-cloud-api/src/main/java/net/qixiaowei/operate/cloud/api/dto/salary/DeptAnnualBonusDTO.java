package net.qixiaowei.operate.cloud.api.dto.salary;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 部门年终奖表
* @author TANGMICHI
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusDTO {

    //查询检验
    public interface QueryDeptAnnualBonusDTO extends Default{

    }
    //新增检验
    public interface AddDeptAnnualBonusDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptAnnualBonusDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptAnnualBonusDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptAnnualBonusId;
    /**
    * 年终奖年度
    */
    private  Integer annualBonusYear;
    /**
    * 公司年终奖总包
    */
    private  BigDecimal companyAnnualBonus;
    /**
    * 部门年终奖总包
    */
    private  BigDecimal departmentAnnualBonus;
    /**
    * 状态
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

