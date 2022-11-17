package net.qixiaowei.operate.cloud.remote.targetManager;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.operate.cloud.service.targetManager.IAreaService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TMICHI
 * @Date 2022-11-17
 **/
@RestController
@RequestMapping("/targetDecompose")
public class RemoteDecompose implements RemoteDecomposeService {

    @Autowired
    private ITargetDecomposeService targetDecomposeService;

    @Override
    @InnerAuth
    @GetMapping("/remote/{targetDecomposeId}")
    public R<TargetDecomposeDTO> info(Long targetDecomposeId, String source) {
        return R.ok(targetDecomposeService.selectRollTargetDecomposeByTargetDecomposeId(targetDecomposeId));
    }
}
