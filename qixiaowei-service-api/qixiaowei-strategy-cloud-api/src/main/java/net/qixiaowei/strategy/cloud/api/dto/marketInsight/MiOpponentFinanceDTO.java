package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.Map;

/**
* 市场洞察对手财务表
* @author TANGMICHI
* @since 2023-03-12
*/
@Data
@Accessors(chain = true)
public class MiOpponentFinanceDTO {

    //查询检验
    public interface QueryMiOpponentFinanceDTO extends Default{

    }
    //新增检验
    public interface AddMiOpponentFinanceDTO extends Default{

    }

    //删除检验
    public interface DeleteMiOpponentFinanceDTO extends Default{

    }
    //修改检验
    public interface UpdateMiOpponentFinanceDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miOpponentFinanceId;
    /**
    * 市场洞察对手ID
    */
    private  Long marketInsightOpponentId;
    /**
    * 市场洞察对手选择ID
    */
    private  Long miOpponentChoiceId;
    /**
     * 指标ID
     */
    private Long indicatorId;
    /**
     *对手财务具体经营值集合
     */
    private List<MiFinanceIndicatorIdDTO> miFinanceIndicatorIdDTOS;
    /**
     * 指标名称
     */
    private  String indicatorName;


    /**
    * 经营年度
    */
    private  Integer operateYear;
    /**
    * 经营值
    */
    private  BigDecimal operateValue;
    /**
    * 排序
    */
    private  Integer sort;
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
    * 租户ID
    */
    private  Long tenantId;

    /**
    * 请求参数
    */
    private Map<String, Object> params;
}

