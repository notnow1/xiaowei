package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;
import lombok.ToString;


/**
 * @author hzk
 * AdminCompany对象
 */
@Data
@ToString
public class AdminCompanyDTO {
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 企业LOGO
     */
    private String companyLogo;
    /**
     * 企业登录LOGO
     */
    private String companyLoginLogo;
    /**
     * 企业过期时间
     */
    private String endTime;
    /**
     * 过期天数
     */
    private Integer endDay;
}
