package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门服务降级处理
 */
@Component
public class RemoteDepartmentFallbackFactory implements FallbackFactory<RemoteDepartmentService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteDepartmentFallbackFactory.class);

    @Override
    public RemoteDepartmentService create(Throwable throwable) {
        log.error("部门服务调用失败:{}", throwable.getMessage());
        return new RemoteDepartmentService() {

            @Override
            public R<List<DepartmentDTO>> selectCodeList(List<String> departmentCodes, String source) {
                return R.fail("根据code集合获取部门信息失败:" + throwable.getMessage());
            }
        };
    }
}