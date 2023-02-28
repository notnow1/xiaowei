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
* 市场洞察宏观详情表
* @author TANGMICHI
* @since 2023-02-28
*/
@Data
@Accessors(chain = true)
public class MiMacroDetailDTO {

    //查询检验
    public interface QueryMiMacroDetailDTO extends Default{

    }
    //新增检验
    public interface AddMiMacroDetailDTO extends Default{

    }

    //删除检验
    public interface DeleteMiMacroDetailDTO extends Default{

    }
    //修改检验
    public interface UpdateMiMacroDetailDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miMacroDetailId;
    /**
    * 市场洞察宏观ID
    */
    private  Long marketInsightMacroId;
    /**
    * 视角
    */
    private  Long visualAngle;
    /**
    * 企业相关因素
    */
    private  String companyRelatedFactor;
    /**
    * 变化及趋势
    */
    private  String changeTrend;
    /**
    * 影响描述
    */
    private  String influenceDescription;
    /**
    * 建议措施
    */
    private  String recommendedPractice;
    /**
    * 规划期
    */
    private  Integer planPeriod;
    /**
    * 预估机会点金额
    */
    private  BigDecimal estimateOpportunityAmount;
    /**
    * 提出人员ID
    */
    private  Long proposeEmployeeId;
    /**
    * 提出人员姓名
    */
    private  String proposeEmployeeName;
    /**
    * 提出人员编码
    */
    private  String proposeEmployeeCode;
    /**
    * 排序
    */
    private  Integer sort;
    /**
     * 市场洞察宏观预估表集合
     */
    private List<MiMacroEstimateDTO> miMacroEstimateDTOS;
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

