package net.qixiaowei.operate.cloud.api.factory.salary;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteSalaryAdjustPlanFallbackFactory implements FallbackFactory<RemoteSalaryAdjustPlanService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSalaryAdjustPlanFallbackFactory.class);

    @Override
    public RemoteSalaryAdjustPlanService create(Throwable throwable) {
        log.error("个人调薪服务调用失败:{}", throwable.getMessage());
        return new RemoteSalaryAdjustPlanService() {

            @Override
            public R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeId(Long employeeId, String source) {
                return R.fail("根据员工ID获取个人调薪服务失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmpSalaryAdjustPlanDTO>> selectByEmployeeIds(List<Long> employeeIds, String source) {
                return R.fail("根据员工ID集合获取个人调薪服务失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmpSalaryAdjustPlanDTO>> selectBySystemIds(List<Long> officialRankSystemIds, String source) {
                return R.fail("根据职级体系ID集合获取个人调薪失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmpSalaryAdjustPlanDTO>> selectByDepartmentId(Long officialRankSystemIds, String source) {
                return R.fail("根据部门ID集合获取个人调薪失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmpSalaryAdjustPlanDTO>> selectByPostId(Long postId, String source) {
                return R.fail("根据岗位ID集合获取个人调薪失败:" + throwable.getMessage());
            }

            @Override
            public R<Integer> empAdjustUpdate(String source) {
                return R.fail("个人调薪到达生效日期更新员工信息失败:" + throwable.getMessage());
            }

        };
    }
}
