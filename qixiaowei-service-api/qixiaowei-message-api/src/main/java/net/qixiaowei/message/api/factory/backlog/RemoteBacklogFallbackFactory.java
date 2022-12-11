package net.qixiaowei.message.api.factory.backlog;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 待办事项服务降级处理
 */
@Component
public class RemoteBacklogFallbackFactory implements FallbackFactory<RemoteBacklogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteBacklogFallbackFactory.class);

    @Override
    public RemoteBacklogService create(Throwable throwable) {
        log.error("待办事项服务调用失败:{}", throwable.getMessage());
        return new RemoteBacklogService() {
            @Override
            public R<?> add(BacklogDTO backlogDTO, String source) {
                return R.fail("新增待办事项失败:" + throwable.getMessage());
            }

            @Override
            public R<?> insertBacklogs(List<BacklogDTO> backlogDTOS, String source) {
                return R.fail("批量新增待办事项失败:" + throwable.getMessage());
            }

            @Override
            public R<?> handled(BacklogDTO backlogDTO, String source) {
                return R.fail("待办事项处理为已办失败:" + throwable.getMessage());
            }
        };
    }
}
