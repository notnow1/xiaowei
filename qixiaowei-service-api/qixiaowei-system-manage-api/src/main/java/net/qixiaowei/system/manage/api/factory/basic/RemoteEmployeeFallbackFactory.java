package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 人员服务降级处理
 */
@Component
public class RemoteEmployeeFallbackFactory implements FallbackFactory<RemoteEmployeeService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteEmployeeFallbackFactory.class);

    @Override
    public RemoteEmployeeService create(Throwable throwable) {
        log.error("人员服务调用失败:{}", throwable.getMessage());
        return new RemoteEmployeeService() {

            @Override
            public R<List<EmployeeDTO>> selectCodeList(List<String> employeeCodes, String source) {
                return R.fail("获取根据code集合人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectRemoteList(EmployeeDTO employeeDTO, String source) {
                return R.fail("获取根据code集合人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<EmployeeDTO> selectByEmployeeId(Long employeeId, String source) {
                return R.fail("根据id获取人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectByEmployeeIds(List<Long> employeeIds, String source) {
                return R.fail("根据id集合获取人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectByBudgeList(List<List<Long>> list, String source) {
                return R.fail("根据部门 职级 获取人员信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<EmployeeDTO>> selectByCodes(List<String> assessmentList, String source) {
                return R.fail("根据部门 职级 获取人员信息失败:" + throwable.getMessage());
            }
        };
    }
}
