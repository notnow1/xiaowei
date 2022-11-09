package net.qixiaowei.system.manage.remote.system;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.service.system.IRegionService;
import net.qixiaowei.system.manage.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/getRegionsByIds")
    public R<List<RegionDTO>> getRegionsByIds(@RequestParam("regionIds") Set<Long> regionIds, String source) {
        return R.ok(regionService.getRegionsByIds(regionIds));
    }

}
