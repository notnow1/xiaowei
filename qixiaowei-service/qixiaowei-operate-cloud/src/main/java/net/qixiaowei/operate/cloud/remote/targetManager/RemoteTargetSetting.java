package net.qixiaowei.operate.cloud.remote.targetManager;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteSettingService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Graves
 * @Date 2022-11-17
 **/
@RestController
@RequestMapping("/targetSetting/remote")
public class RemoteTargetSetting implements RemoteSettingService {

    @Autowired
    private ITargetSettingService targetSettingService;


    /**
     * 根据指标ID查询目标制定是否引用
     *
     * @param indicatorIds 指标ID集合
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("queryIndicatorSetting")
    public R<List<TargetSettingDTO>> queryIndicatorSetting(List<Long> indicatorIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        return R.ok(targetSettingService.selectByIndicatorIds(indicatorIds));
    }

}
