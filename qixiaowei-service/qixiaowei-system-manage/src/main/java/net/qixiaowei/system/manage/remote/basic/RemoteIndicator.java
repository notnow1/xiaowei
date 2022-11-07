package net.qixiaowei.system.manage.remote.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author hzk
 * @Date 2022-10-20 20:39
 **/
@RestController
@RequestMapping("/indicator")
public class RemoteIndicator implements RemoteIndicatorService {

    @Autowired
    private IIndicatorService indicatorService;

    @Override
//    @InnerAuth
    @GetMapping("/listByCode")
    public R<IndicatorDTO> selectIndicatorByCode(@RequestParam("indicatorCode") String indicatorCode) {
        return R.ok(indicatorService.selectIndicatorByCode(indicatorCode));
    }

    @Override
    @PostMapping("/listByCodes")
    public R<List<IndicatorDTO>> selectIndicatorByCodeList(@RequestBody List<String> indicatorCodes) {
        return R.ok(indicatorService.selectIndicatorByCodeList(indicatorCodes));
    }

    @Override
    @PostMapping("/remoteTreeList")
    public R<List<Tree<Long>>> selectIndicatorTreeList(@RequestBody IndicatorDTO indicatorDTO) {
        return R.ok(indicatorService.selectTreeList(indicatorDTO));
    }

    @Override
    @PostMapping("/remoteList")
    public R<List<IndicatorDTO>> selectIndicatorList(@RequestBody IndicatorDTO indicatorDTO) {
        return R.ok(indicatorService.selectTargetIndicatorList(indicatorDTO));
    }

    @Override
    @PostMapping("/listByIds")
    public R<List<IndicatorDTO>> selectIndicatorByIds(@RequestBody List<Long> indicatorIds) {
        return R.ok(indicatorService.selectIndicatorByIds(indicatorIds));
    }
}
