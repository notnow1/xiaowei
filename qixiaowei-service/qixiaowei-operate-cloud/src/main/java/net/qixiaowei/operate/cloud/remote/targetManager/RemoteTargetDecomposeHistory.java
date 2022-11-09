package net.qixiaowei.operate.cloud.remote.targetManager;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteTargetDecomposeHistoryService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author Graves
 * @Date 2022-10-06 16:39
 **/
@RestController
@RequestMapping("/targetDecomposeHistory")
public class RemoteTargetDecomposeHistory implements RemoteTargetDecomposeHistoryService {
    @Autowired
    private ITargetDecomposeHistoryService targetDecomposeHistoryService;


    /**
     * 定时任务生成目标分解历史数据
     */
    @InnerAuth
    @PostMapping("/cron")
    @Override
    public R<?> cronCreateHistoryList(@RequestBody Integer timeDimension, String source) {
        targetDecomposeHistoryService.cronCreateHistoryList(timeDimension);
        return R.ok();
    }
}
