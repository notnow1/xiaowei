package net.qixiaowei.message.remote.backlog;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.message.service.backlog.IBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author hzk
 * @Date 2022-12-11 10:39
 **/
@RestController
@RequestMapping("/backlog")
public class RemoteBacklog implements RemoteBacklogService {

    @Autowired
    private IBacklogService backlogService;

    @InnerAuth
    @Override
    @PostMapping("/add")
    public R<?> add(@RequestBody BacklogDTO backlogDTO, String source) {
        return R.ok(backlogService.insertBacklog(backlogDTO));
    }

    @InnerAuth
    @Override
    @PostMapping("/insertBacklogs")
    public R<?> insertBacklogs(@RequestBody List<BacklogDTO> backlogDTOS, String source) {
        return R.ok(backlogService.insertBacklogs(backlogDTOS));
    }

    @InnerAuth
    @Override
    @PostMapping("/handled")
    public R<?> handled(@RequestBody BacklogDTO backlogDTO, String source) {
        return R.ok(backlogService.handled(backlogDTO));
    }
}
