package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标分解详情表
* @author TANGMICHI
* @since 2022-10-28
*/
@Data
@Accessors(chain = true)
public class TargetDecomposeDetailsDTO {

    //查询检验
    public interface QueryTargetDecomposeDetailsDTO extends Default{

    }
    //新增检验
    public interface AddTargetDecomposeDetailsDTO extends Default{

    }

    //删除检验
    public interface DeleteTargetDecomposeDetailsDTO extends Default{

    }
    //修改检验
    public interface UpdateTargetDecomposeDetailsDTO extends Default{

    }
    /**
    * ID
    */
    private  Long targetDecomposeDetailsId;
    /**
    * 目标分解ID
    */
    private  Long targetDecomposeId;
    /**
    * 员工ID
    */

    private  Long employeeId;
    /**
     * 员工名称
     */
    private  String employeeName;
    /**
    * 区域ID
    */
    private  Long areaId;
    /**
     * 区域名称
     */
    private  String areaName;

    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
     * 部门名称
     */
    private  String departmentName;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
     * 产品名称
     */
    private  String productName;
    /**
    * 省份ID
    */
    private  Long regionId;
    /**
     * 省份名称
     */
    private  String regionName;
    /**
    * 行业ID
    */
    private  Long industryId;
    /**
     * 行业名称
     */
    private  String industryName;
    /**
    * 负责人ID
    */
    @NotNull(message = "滚动预测负责人ID不能为空",groups = {TargetDecomposeDetailsDTO.AddTargetDecomposeDetailsDTO.class,TargetDecomposeDetailsDTO.UpdateTargetDecomposeDetailsDTO.class})
    private  Long principalEmployeeId;
    /**
     * 负责人名称
     */
    private  String principalEmployeeName;
    /**
     * 分解目标值
     */
    private BigDecimal decomposeTarget;
    /**
     * 年度预测值
     */
    private  BigDecimal forecastYear;
    /**
     * 累计实际值
     */
    private  BigDecimal actualTotal;
    /**
     * 目标完成率
     */
    private  BigDecimal targetPercentageComplete;
    /**
    * 汇总目标值
    */
    private BigDecimal amountTarget;
    /**
     * 目标分解详情周期表
     */
    @NotEmpty(message = "目标分解详情周期信息不能为空",groups = {TargetDecomposeDTO.AddTargetDecomposeDTO.class,TargetDecomposeDTO.UpdateTargetDecomposeDTO.class})
    private List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS;
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

