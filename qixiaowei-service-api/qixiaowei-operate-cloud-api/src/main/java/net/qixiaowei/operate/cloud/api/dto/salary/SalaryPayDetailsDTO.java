package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 工资发薪明细表
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@Accessors(chain = true)
public class SalaryPayDetailsDTO {

    //查询检验
    public interface QuerySalaryPayDetailsDTO extends Default {

    }

    //新增检验
    public interface AddSalaryPayDetailsDTO extends Default {

    }

    //删除检验
    public interface DeleteSalaryPayDetailsDTO extends Default {

    }

    //修改检验
    public interface UpdateSalaryPayDetailsDTO extends Default {

    }

    /**
     * ID
     */
    private Long salaryPayDetailsId;
    /**
     * 工资发薪ID
     */
    private Long salaryPayId;
    /**
     * 工资项ID
     */
    private Long salaryItemId;
    /**
     * 金额(单位/元)
     */
    private BigDecimal amount;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
