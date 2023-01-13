package net.qixiaowei.job.salary;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EmpSalaryAdjustPlanJob {

    @Resource
    private RemoteSalaryAdjustPlanService remoteSalaryAdjustPlanService;

    /**
     * 每【日】检测个人调薪是否到达生效日期
     */
    @XxlJob("empAdjustUpdateOfDay")
    public void empAdjustUpdateOfDay() throws Exception {
        R<?> result = remoteSalaryAdjustPlanService.empAdjustUpdate(SecurityConstants.INNER);
        if (R.SUCCESS != result.getCode()) {
            XxlJobHelper.handleFail("个人调薪到达生效日期更新员工信息失败:" + result.getMsg());
        }
        XxlJobHelper.log("个人调薪到达生效日期更新员工信息成功");
    }
}
