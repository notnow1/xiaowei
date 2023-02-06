package net.qixiaowei.job.tenant;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.system.manage.api.remote.tenant.RemoteTenantService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 租户相关job
 */
@Component
public class TenantJob {

    @Resource
    private RemoteTenantService remoteTenantService;

    /**
     * 维护租户状态
     */
    @XxlJob("maintainTenantStatus")
    @IgnoreTenant
    public void maintainTenantStatus() throws Exception {
        R<?> result = remoteTenantService.maintainTenantStatus(SecurityConstants.INNER);
        if (R.SUCCESS != result.getCode()) {
            XxlJobHelper.handleFail("维护租户状态失败:" + result.getMsg());
        }
        XxlJobHelper.log("维护租户状态成功");
    }

}
