package net.qixiaowei.operate.cloud.remote;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.remote.RemoteOperateCloudInitDataService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author hzk
 * @Date 2023-03-03 18:39
 **/
@RestController
@RequestMapping("/operate-cloud")
public class RemoteOperateCloudInitData implements RemoteOperateCloudInitDataService {

    @Autowired
    private ISalaryItemService salaryItemService;

    /**
     * 初始化数据
     */
    @Override
    @InnerAuth
    @PostMapping("/initData")
    public R<Boolean> initData(String source) {
        Boolean initData;
        initData = salaryItemService.initSalaryItem();
        return R.ok(initData);
    }


}
