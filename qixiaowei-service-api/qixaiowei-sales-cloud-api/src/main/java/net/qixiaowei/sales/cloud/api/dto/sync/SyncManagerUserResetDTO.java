package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;


/**
 * 企业主账号重置VO
 *
 * @author hzk
 */
@Data
public class SyncManagerUserResetDTO {
    /**
     * 旧手机号
     */
    private String oldPhone;
    /**
     * 新手机号
     */
    private String newPhone;
    /**
     * 新密码
     */
    private String newPassword;
}
