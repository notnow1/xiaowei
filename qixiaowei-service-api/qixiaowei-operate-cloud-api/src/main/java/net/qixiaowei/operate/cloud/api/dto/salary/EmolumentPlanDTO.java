package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;

/**
* 薪酬规划表
* @author TANGMICHI
* @since 2022-11-18
*/
@Data
@Accessors(chain = true)
public class EmolumentPlanDTO {

    //查询检验
    public interface QueryEmolumentPlanDTO extends Default{

    }
    //新增检验
    public interface AddEmolumentPlanDTO extends Default{

    }

    //删除检验
    public interface DeleteEmolumentPlanDTO extends Default{

    }
    //修改检验
    public interface UpdateEmolumentPlanDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "薪酬规划id不能为空", groups = {EmolumentPlanDTO.DeleteEmolumentPlanDTO.class})
    private  Long emolumentPlanId;
    /**
    * 预算年度
    */
    @NotNull(message = "预算年度不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  Integer planYear;
    /**
     * 指标id
     */
    private Long indicatorId;
    /**
    * 预算年前一年销售收入
    */
    @NotNull(message = "预算年前一年销售收入不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private BigDecimal revenueBeforeOne;
    /**
    * 预算年销售收入
    */
    @NotNull(message = "预算年销售收入不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal revenue;
    /**
    * 预算年后一年销售收入
    */
    @NotNull(message = "预算年后一年销售收入不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal revenueAfterOne;
    /**
    * 预算年后二年销售收入
    */
    @NotNull(message = "预算年后二年销售收入不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal revenueAfterTwo;

    /**
     * 预算年E/R值(%)
     */
    private  BigDecimal er;
    /**
    * 预算年前一年E/R值(%)
    */
    @NotNull(message = "预算年前一年E/R值(%)不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal erBeforeOne;

    /**
     * 预算年后一年E/R值(%)
     */
    private  BigDecimal erAfterOne;
    /**
     * 预算年后二年E/R值(%)
     */
    private  BigDecimal erAfterTwo;

    /**
     * 预算年前一年E/R值改进率(%)
     */
    private  BigDecimal emolumentRevenueImproveBeforeOne;
    /**
    * 预算年E/R值改进率(%)
    */
    @NotNull(message = "预算年E/R值改进率(%)不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal emolumentRevenueImprove;

    /**
     * 预算年E/R值实际改进率(%)
     */
    private  BigDecimal emolumentPracticalRevenueImprove;
    /**
    * 预算年后一年E/R值改进率(%)
    */
    @NotNull(message = "预算年后一年E/R值改进率(%)不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal erImproveAfterOne;
    /**
    * 预算年后二年E/R值改进率(%)
    */
    @NotNull(message = "预算年后二年E/R值改进率(%)不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal erImproveAfterTwo;
    /**
    * 预算年前一年总薪酬包
    */
    @NotNull(message = "预算年前一年总薪酬包不能为空", groups = {EmolumentPlanDTO.AddEmolumentPlanDTO.class})
    private  BigDecimal emolumentPackageBeforeOne;

    /**
     * 预算年总薪酬包
     */
    private  BigDecimal emolumentPackage;
    /**
     * 预算年后一年总薪酬包
     */
    private  BigDecimal emolumentPackageAfterOne;
    /**
     * 预算年后二年总薪酬包
     */
    private  BigDecimal emolumentPackageAfterTwo;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
     * 创建人名称
     */
    private  String createName;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  updateTime;

}

