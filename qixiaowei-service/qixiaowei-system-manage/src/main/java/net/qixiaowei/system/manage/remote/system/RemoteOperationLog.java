package net.qixiaowei.system.manage.remote.system;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.api.remote.log.RemoteOperationLogService;
import net.qixiaowei.system.manage.service.log.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author hzk
 * @Date 2022-10-20 20:39
 **/
@RestController
@RequestMapping("/operationLog")
public class RemoteOperationLog implements RemoteOperationLogService {

    @Autowired
    private IOperationLogService operationLogService;

    /**
     * 新增操作日志表
     */
    @Override
    @InnerAuth
    @PostMapping("/add")
    public R<?> add(@RequestBody OperationLogDTO operationLogDTO, String source) {
        return R.ok(operationLogService.insertOperationLog(operationLogDTO));
    }

    /**
     * 批量新增操作日志表
     */
    @Override
    @InnerAuth
    @PostMapping("/insertOperationLogs")
    public R<?> insertOperationLogs(@RequestBody List<OperationLogDTO> operationLogDTOList, String source) {
        return R.ok(operationLogService.insertOperationLogs(operationLogDTOList));
    }


}
