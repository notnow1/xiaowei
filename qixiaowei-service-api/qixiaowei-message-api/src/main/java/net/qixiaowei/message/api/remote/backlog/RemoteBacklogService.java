package net.qixiaowei.message.api.remote.backlog;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.dto.backlog.BacklogSendDTO;
import net.qixiaowei.message.api.factory.backlog.RemoteBacklogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


/**
 * 待办事项服务
 */
@FeignClient(contextId = "remoteBacklogService", value = ServiceNameConstants.MESSAGE_SERVICE, fallbackFactory = RemoteBacklogFallbackFactory.class)
public interface RemoteBacklogService {

    String API_PREFIX_BACKLOG = "/backlog";

    /**
     * 新增待办事项
     *
     * @param backlogSendDTO 待办事项
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping(API_PREFIX_BACKLOG + "/add")
    R<?> add(@Validated @RequestBody BacklogSendDTO backlogSendDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 批量新增待办事项
     *
     * @param backlogSendDTOS 待办事项集合
     * @param source      请求来源
     * @return 结果
     */
    @PostMapping(API_PREFIX_BACKLOG + "/insertBacklogs")
    R<?> insertBacklogs(@Validated @RequestBody List<BacklogSendDTO> backlogSendDTOS, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 将待办事项处理为已办
     *
     * @param backlogDTO 待办事项
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping(API_PREFIX_BACKLOG + "/handled")
    R<?> handled(@Validated(BacklogDTO.UpdateBacklogDTO.class) @RequestBody BacklogDTO backlogDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
