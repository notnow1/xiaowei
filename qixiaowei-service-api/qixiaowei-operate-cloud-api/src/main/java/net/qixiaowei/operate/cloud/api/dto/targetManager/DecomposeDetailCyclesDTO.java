package net.qixiaowei.operate.cloud.api.dto.targetManager;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 目标分解详情周期表
* @author TANGMICHI
* @since 2022-10-28
*/
@Data
@Accessors(chain = true)
public class DecomposeDetailCyclesDTO {

    //查询检验
    public interface QueryDecomposeDetailCyclesDTO extends Default{

    }
    //新增检验
    public interface AddDecomposeDetailCyclesDTO extends Default{

    }

    //删除检验
    public interface DeleteDecomposeDetailCyclesDTO extends Default{

    }
    //修改检验
    public interface UpdateDecomposeDetailCyclesDTO extends Default{

    }
    /**
    * ID
    */
    private  Long decomposeDetailCyclesId;
    /**
    * 目标分解详情ID
    */
    private  Long targetDecomposeDetailsId;
    /**
    * 目标分解ID
    */
    private  Long targetDecomposeId;
    /**
    * 周期数(顺序递增)
    */
    private  Integer cycleNumber;
    /**
    * 周期目标值
    */
    private  BigDecimal cycleTarget;
    /**
    * 周期预测值
    */
    private BigDecimal cycleForecast;
    /**
    * 周期实际值
    */
    private  BigDecimal cycleActual;

    /**
     * 预测偏差
     */
    private  BigDecimal cycleForecastDeviation;
    /**
     * 预测与目标偏差率
     */
    private  BigDecimal cycleForecastDeviationRate;
    /**
     * 目标完成率
     */
    private  BigDecimal cyclePercentageComplete;
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
    /**
     * 指标id
     */
    private  Long indicatorId;

}

