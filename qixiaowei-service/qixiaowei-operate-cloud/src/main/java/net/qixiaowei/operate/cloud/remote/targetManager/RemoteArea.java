package net.qixiaowei.operate.cloud.remote.targetManager;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.operate.cloud.service.targetManager.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Graves
 * @Date 2022-10-06 16:39
 **/
@RestController
@RequestMapping("/area")
public class RemoteArea implements RemoteAreaService {

    @Autowired
    private IAreaService areaService;

    /**
     * 查询分解维度区域下拉列表
     */
    @Override
    @InnerAuth
    @PostMapping("/dropList")
    public R<List<AreaDTO>> dropList(AreaDTO areaDTO, String source) {
        return R.ok(areaService.selectDropList(areaDTO));
    }

    /**
     * 查询区域配置列表通过IDS
     */
    @Override
    @InnerAuth
    @PostMapping("/getByIds")
    public R<List<AreaDTO>> selectAreaListByAreaIds(@RequestBody List<Long> areaIds, String source) {
        return R.ok(areaService.selectAreaListByAreaIds(areaIds));
    }

    /**
     * 根据区域ID查找数据
     */
    @Override
    @InnerAuth
    @GetMapping("/getById")
    public R<AreaDTO> getById(@RequestParam("areaId") Long areaId, String source) {
        return R.ok(areaService.selectAreaByAreaId(areaId));
    }

}
