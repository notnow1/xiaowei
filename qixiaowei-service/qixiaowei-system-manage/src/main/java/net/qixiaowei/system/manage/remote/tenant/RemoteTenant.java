package net.qixiaowei.system.manage.remote.tenant;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.remote.tenant.RemoteTenantService;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author hzk
 * @Date 2022-11-29 11:39
 **/
@RestController
@RequestMapping("/tenant")
public class RemoteTenant implements RemoteTenantService {

    @Autowired
    private ITenantService tenantService;

    @InnerAuth
    @Override
    @GetMapping("/getTenantIds")
    public R<List<Long>> getTenantIds(String source) {
        return R.ok(tenantService.getTenantIds());
    }
}
