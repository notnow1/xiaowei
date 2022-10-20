package net.qixiaowei.operate.cloud.remote.targetManager;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.operate.cloud.service.targetManager.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/dropList")
    public R<List<AreaDTO>> dropList(AreaDTO areaDTO) {
        return R.ok(areaService.selectDropList(areaDTO));
    }

}
