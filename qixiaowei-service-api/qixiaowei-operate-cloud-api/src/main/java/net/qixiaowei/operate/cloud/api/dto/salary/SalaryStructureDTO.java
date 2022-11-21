package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工资发薪表
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@Accessors(chain = true)
public class SalaryStructureDTO {

    //查询检验
    public interface QuerySalaryPayDTO extends Default {

    }

    //新增检验
    public interface AddSalaryStructureDTO extends Default {

    }

    //删除检验
    public interface DeleteSalaryStructureDTO extends Default {

    }

    //修改检验
    public interface UpdateSalaryStructureDTO extends Default {

    }

    /**
     * 工资金额合计
     */
    private BigDecimal salaryAmountSum;
    /**
     * 津贴金额合计
     */
    private BigDecimal allowanceAmountSum;
    /**
     * 福利金额合计
     */
    private BigDecimal welfareAmountSum;
    /**
     * 奖金金额合计
     */
    private BigDecimal bonusAmountSum;
    /**
     * 部门ID
     */
    private Long postId;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 报表列表
     */
    private List<SalaryPayDTO> salaryPayDTOList;

}

