package net.qixiaowei.system.manage.remote.system;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
import net.qixiaowei.system.manage.service.system.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Author hzk
 * @Date 2022-10-20 20:39
 **/
@RestController
@RequestMapping("/region")
public class RemoteRegion implements RemoteRegionService {

    @Autowired
    private IRegionService regionService;

    @Override
    @InnerAuth
    @PostMapping("/getRegionsByIds")
    public R<List<RegionDTO>> getRegionsByIds(@RequestBody Set<Long> regionIds, String source) {
        return R.ok(regionService.getRegionsByIds(regionIds));
    }

    /**
     * 根据省份名称集合查询省份数据
     * @param regionNames 省份集合
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/codeList")
    public R<List<RegionDTO>> selectCodeList(@RequestBody List<String> regionNames, String source) {
        return R.ok(regionService.selectCodeList(regionNames));
    }

    /**
     * 远程查询excel省份下拉框
     * @param regionDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/getDropList")
    public R<List<RegionDTO>> getDropList(@RequestBody RegionDTO regionDTO, String source) {
        return R.ok(regionService.getDropList(regionDTO));
    }

}
