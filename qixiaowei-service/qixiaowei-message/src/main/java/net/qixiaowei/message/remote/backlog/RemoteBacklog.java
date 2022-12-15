package net.qixiaowei.message.remote.backlog;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.dto.backlog.BacklogSendDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.message.service.backlog.IBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public R<?> add(@RequestBody BacklogSendDTO backlogSendDTO, String source) {
        BacklogDTO backlogDTO = new BacklogDTO();
        BeanUtils.copyProperties(backlogSendDTO, backlogDTO);
        return R.ok(backlogService.insertBacklog(backlogDTO));
    }

    @InnerAuth
    @Override
    @PostMapping("/insertBacklogs")
    public R<?> insertBacklogs(@RequestBody List<BacklogSendDTO> backlogSendDTOS, String source) {
        List<BacklogDTO> backlogDTOS = new ArrayList<>();
        for (BacklogSendDTO backlogSendDTO : backlogSendDTOS) {
            BacklogDTO backlogDTO = new BacklogDTO();
            BeanUtils.copyProperties(backlogSendDTO, backlogDTO);
            backlogDTOS.add(backlogDTO);
        }
        return R.ok(backlogService.insertBacklogs(backlogDTOS));
    }

    @InnerAuth
    @Override
    @PostMapping("/handled")
    public R<?> handled(@RequestBody BacklogDTO backlogDTO, String source) {
        backlogService.handled(backlogDTO);
        return R.ok();
    }
}
