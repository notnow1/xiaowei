package net.qixiaowei.strategy.cloud.remote;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.remote.RemoteOperateCloudInitDataService;
import net.qixiaowei.strategy.cloud.api.remote.RemoteStrategyCloudInitDataService;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author hzk
 * @Date 2023-03-03 18:39
 **/
@RestController
@RequestMapping("/strategy-cloud")
public class RemoteStrategyCloudInitData implements RemoteStrategyCloudInitDataService {

    @Autowired
    private IIndustryAttractionService iIndustryAttractionService;

    /**
     * 初始化数据
     */
    @Override
    @InnerAuth
    @PostMapping("/initData")
    public R<Boolean> initData(String source) {
        Boolean initData;
        initData = iIndustryAttractionService.initIndustryAttraction();
        return R.ok(initData);
    }


}
