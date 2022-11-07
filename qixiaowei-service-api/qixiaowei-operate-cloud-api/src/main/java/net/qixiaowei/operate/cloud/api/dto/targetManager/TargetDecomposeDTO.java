package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.*;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标分解表
* @author TANGMICHI
* @since 2022-10-27
*/
@Data
@Accessors(chain = true)
public class TargetDecomposeDTO {

    //查询检验
    public interface QueryTargetDecomposeDTO extends Default{

    }
    //新增检验
    public interface AddTargetDecomposeDTO extends Default{

    }

    //删除检验
    public interface DeleteTargetDecomposeDTO extends Default{

    }
    //修改检验
    public interface UpdateTargetDecomposeDTO extends Default{

    }

    //修改检验
    public interface RollUpdateTargetDecomposeDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "ID不能为空",groups = {TargetDecomposeDTO.DeleteTargetDecomposeDTO.class,TargetDecomposeDTO.RollUpdateTargetDecomposeDTO.class})
    private  Long targetDecomposeId;
    /**
    * 目标分解类型:0自定义;1销售订单;2销售收入;3销售回款
    */
    private  Integer targetDecomposeType;
    /**
    * 指标ID
    */
    private  Long indicatorId;
    /**
     * 版本号
     */
    private  String  version;
    /**
    * 目标年度
    */
    @NotNull(message = "目标年度不能为空",groups = {TargetDecomposeDTO.AddTargetDecomposeDTO.class,TargetDecomposeDTO.UpdateTargetDecomposeDTO.class})
    private  Integer targetYear;
    /**
    * 目标分解维度ID
    */
    @NotNull(message = "目标分解维度ID不能为空",groups = {TargetDecomposeDTO.AddTargetDecomposeDTO.class,TargetDecomposeDTO.UpdateTargetDecomposeDTO.class})
    private  Long targetDecomposeDimensionId;
    /**
    * 分解维度
    */
    @NotBlank(message = "分解维度名称不能为空",groups = {TargetDecomposeDTO.AddTargetDecomposeDTO.class,TargetDecomposeDTO.UpdateTargetDecomposeDTO.class})
    private  String decompositionDimension;
    /**
     * 负责人ID
     */
    @NotNull(message = "负责人ID不能为空",groups = {TargetDecomposeDTO.RollUpdateTargetDecomposeDTO.class})
    private  Long principalEmployeeId;
    /**
     * 负责人名称
     */
    private  String principalEmployeeName;
    /**
     * 指标名称
     */
    private  String indicatorName;
    /**
     * 挑战值
     */
    private BigDecimal challengeValue;
    /**
     * 目标值(公司目标)
     */
    private BigDecimal targetValue;
    /**
     * 保底值
     */
    private BigDecimal guaranteedValue;
    /**
    * 时间维度:1年度;2半年度;3季度;4月度;5周
    */
    @NotNull(message = "时间维度不能为空",groups = {TargetDecomposeDTO.AddTargetDecomposeDTO.class,TargetDecomposeDTO.UpdateTargetDecomposeDTO.class})
    private  Integer timeDimension;
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
    * 状态:0待录入;1已录入
    */
    private  Integer status;
    /**
     * 目标分解详情信息
     */
    @NotEmpty(message = "目标分解详情信息不能为空",groups = {TargetDecomposeDTO.AddTargetDecomposeDTO.class,TargetDecomposeDTO.UpdateTargetDecomposeDTO.class})
    @Valid
    private List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS;

    /**
     * 字段名称
     */
    private List<Map<String, String>> fileNameList;
    /**
     * 预测周期
     */
    private  String  forecastCycle;
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

}

