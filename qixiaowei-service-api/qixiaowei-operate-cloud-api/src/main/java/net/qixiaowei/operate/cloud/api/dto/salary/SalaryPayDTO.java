package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工资发薪表
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@Accessors(chain = true)
public class SalaryPayDTO {

    //查询检验
    public interface QuerySalaryPayDTO extends Default {

    }

    //新增检验
    public interface AddSalaryPayDTO extends Default {

    }

    //删除检验
    public interface DeleteSalaryPayDTO extends Default {

    }

    //修改检验
    public interface UpdateSalaryPayDTO extends Default {

    }

    /**
     * ID
     */
    private Long salaryPayId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 员工工号
     */
    private String employeeCode;
    /**
     * 员工名称
     */
    private String employeeName;
    /**
     * 岗位名称
     */
    private String employeePostName;
    /**
     * 个人职级名称
     */
    private String employeeRankName;
    /**
     * 岗位职级
     */
    private String postRank;
    /**
     * 岗位职级名称
     */
    private String postRankName;
    /**
     * 部门ID
     */
    private Long employeeDepartmentId;
    /**
     * 部门名称
     */
    private String employeeDepartmentName;
    /**
     * 发薪年份
     */
    private Integer payYear;
    /**
     * 发薪月份
     */
    private Integer payMonth;
    /**
     * 工资金额
     */
    private BigDecimal salaryAmount;
    /**
     * 津贴金额
     */
    private BigDecimal allowanceAmount;
    /**
     * 福利金额
     */
    private BigDecimal welfareAmount;
    /**
     * 奖金金额
     */
    private BigDecimal bonusAmount;
    /**
     * 倒退12个月奖金金额合计
     */
    private BigDecimal bonusAmountSum;
    /**
     * 代扣代缴金额
     */
    private BigDecimal withholdRemitTax;
    /**
     * 其他扣款金额
     */
    private BigDecimal otherDeductions;
    /**
     * 总扣减项
     */
    private BigDecimal totalDeductions;
    /**
     * 总薪酬包
     */
    private BigDecimal paymentBonus;
    /**
     * 总工资包
     */
    private BigDecimal totalWages;
    /**
     * 总计
     */
    private BigDecimal totalAmount;
    /**
     * 增人减人工资包合计 (工资+津贴+福利)
     */
    private BigDecimal payAmountSum;
    /**
     * 总奖金包
     */
    private BigDecimal amountBonus;
    /**
     * 薪酬合计
     */
    private BigDecimal paymentBonusSum;
    /**
     * 发薪金额
     */
    private BigDecimal payAmount;
    /**
     * 固定占比（%）
     */
    private BigDecimal fixedProportion;
    /**
     * 浮动占比（%）
     */
    private BigDecimal floatProportion;
    /**
     * 月度工资数据
     */
    private List<SalaryPayDTO> salaryPayDTOList;
    /**
     * 月度工资数据详情
     */
    private List<SalaryItemDTO> salaryItemDTOList;
    /**
     * 月度工资数据详情
     */
    private List<SalaryPayDetailsDTO> salaryPayDetailsDTOList;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 工资发薪ID集合
     */
    private List<Long> salaryPayIds;
    /**
     * 是否勾选（0-仅勾选数据,1-所有符合条件数据）
     */
    private Integer isSelect;
    /**
     * 请求参数
     */
    private Map<String, Object> params;

}

