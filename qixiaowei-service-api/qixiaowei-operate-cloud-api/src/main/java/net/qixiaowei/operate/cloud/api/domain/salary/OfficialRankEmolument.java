package net.qixiaowei.operate.cloud.api.domain.salary;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 职级薪酬表
* @author Graves
* @since 2022-11-30
*/
@Data
@Accessors(chain = true)
public class OfficialRankEmolument extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  officialRankEmolumentId;
     /**
     * 职级体系ID
     */
     private  Long  officialRankSystemId;
     /**
     * 职级
     */
     private  Integer  officialRank;
     /**
     * 工资上限
     */
     private BigDecimal salaryCap;
     /**
     * 工资下限
     */
     private  BigDecimal  salaryFloor;
     /**
     * 工资中位数
     */
     private  BigDecimal  salaryMedian;
}

