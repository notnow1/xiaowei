package net.qixiaowei.operate.cloud.api.factory.salary;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;


public class RemoteSalaryItemFallbackFactory implements FallbackFactory<RemoteSalaryItemService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSalaryItemFallbackFactory.class);

    @Override
    public RemoteSalaryItemService create(Throwable throwable) {
        log.error("工资条服务调用失败:{}", throwable.getMessage());
        return new RemoteSalaryItemService() {

            @Override
            public R<List<SalaryPayDTO>> selectByEmployeeId(Long employeeId, String source) {
                return R.fail("根据员工ID查询工资条失败:" + throwable.getMessage());
            }

        };
    }
}
