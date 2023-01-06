package net.qixiaowei.system.manage.api.factory.log;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.api.remote.log.RemoteOperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志服务降级处理
 */
@Component
public class RemoteOperationLogFallbackFactory implements FallbackFactory<RemoteOperationLogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteOperationLogFallbackFactory.class);

    @Override
    public RemoteOperationLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteOperationLogService() {

            @Override
            public R<?> add(OperationLogDTO operationLogDTO, String source) {
                return R.fail("新增操作日志失败:" + throwable.getMessage());
            }

            @Override
            public R<?> insertOperationLogs(List<OperationLogDTO> operationLogDTOList, String source) {
                return R.fail("批量新增操作日志失败:" + throwable.getMessage());
            }
        };

    }
}
