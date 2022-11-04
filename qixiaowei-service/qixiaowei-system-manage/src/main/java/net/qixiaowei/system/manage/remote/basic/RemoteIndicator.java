package net.qixiaowei.system.manage.remote.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/listByCodes")
    public R<List<IndicatorDTO>> selectIndicatorByCodeList(List<String> indicatorCodes) {
        return R.ok(indicatorService.selectIndicatorByCodeList(indicatorCodes));
    }

    @Override
    @GetMapping("/remoteTreeList")
    public R<List<Tree<Long>>> selectIndicatorTreeList(@RequestParam("indicatorDTO") IndicatorDTO indicatorDTO) {
        return R.ok(indicatorService.selectTreeList(indicatorDTO));
    }

    @Override
    @GetMapping("/remoteList")
    public R<List<IndicatorDTO>> selectIndicatorList(IndicatorDTO indicatorDTO) {
        return R.ok(indicatorService.selectIndicatorList(indicatorDTO));
    }
}
