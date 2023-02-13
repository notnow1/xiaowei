package net.qixiaowei.system.manage.remote.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
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
    @InnerAuth
    @GetMapping("/listByCode")
    public R<IndicatorDTO> selectIndicatorByCode(@RequestParam("indicatorCode") String indicatorCode, String source) {
        return R.ok(indicatorService.selectIndicatorByCode(indicatorCode));
    }

    @Override
    @InnerAuth
    @PostMapping("/listByCodes")
    public R<List<IndicatorDTO>> selectIndicatorByCodeList(@RequestBody List<String> indicatorCodes, String source) {
        return R.ok(indicatorService.selectIndicatorByCodeList(indicatorCodes));
    }

    @Override
    @InnerAuth
    @PostMapping("/remoteTreeList")
    public R<List<Tree<Long>>> selectIndicatorTreeList(@RequestBody IndicatorDTO indicatorDTO, String source) {
        return R.ok(indicatorService.selectTreeList(indicatorDTO));
    }

    @Override
    @InnerAuth
    @PostMapping("/remoteList")
    public R<List<IndicatorDTO>> selectIndicatorList(@RequestBody IndicatorDTO indicatorDTO, String source) {
        return R.ok(indicatorService.selectTargetIndicatorList(indicatorDTO));
    }

    @Override
    @InnerAuth
    @GetMapping("/isDriverList")
    public R<List<IndicatorDTO>> selectIsDriverList(String source) {
        return R.ok(indicatorService.selectIsDriverList());
    }

    @Override
    @InnerAuth
    @PostMapping("/listByIds")
    public R<List<IndicatorDTO>> selectIndicatorByIds(@RequestBody List<Long> indicatorIds, String source) {
        return R.ok(indicatorService.selectIndicatorByIds(indicatorIds));
    }

    @Override
    @InnerAuth
    @GetMapping("/byId")
    public R<IndicatorDTO> selectIndicatorById(@RequestParam("indicatorId") Long indicatorId, String source) {
        return R.ok(indicatorService.selectIndicatorById(indicatorId));
    }

    @Override
    @InnerAuth
    @PostMapping("/listByNames")
    public R<List<IndicatorDTO>> selectIndicatorByNames(@RequestBody List<String> indicatorNames, String source) {
        return R.ok(indicatorService.selectIndicatorByNames(indicatorNames));
    }

    /**
     *远程查询指标列表平铺
     * @param indicatorDTO 指标
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/getIndicatorAllData")
    public R<List<IndicatorDTO>> getIndicatorAllData(IndicatorDTO indicatorDTO, String source) {
        return R.ok(indicatorService.getIndicatorAllData(indicatorDTO));
    }
}
