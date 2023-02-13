package net.qixiaowei.operate.cloud.remote.targetManager;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteSettingService;
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
    @PostMapping("/queryIndicatorSetting")
    public R<List<TargetSettingDTO>> queryIndicatorSetting(@RequestBody List<Long> indicatorIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        return R.ok(targetSettingService.selectByIndicatorIds(indicatorIds));
    }

    /**
     * 查询目标制定list
     *
     * @param targetSettingDTO 目标制定DTO
     * @param source           source
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/queryIndicatorSettingList")
    public R<List<TargetSettingDTO>> queryIndicatorSettingList(@RequestBody TargetSettingDTO targetSettingDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        return R.ok(targetSettingService.queryIndicatorSettingList(targetSettingDTO));
    }

}
