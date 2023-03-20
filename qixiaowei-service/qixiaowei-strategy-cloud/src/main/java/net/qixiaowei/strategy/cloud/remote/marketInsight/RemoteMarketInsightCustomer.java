package net.qixiaowei.strategy.cloud.remote.marketInsight;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightCustomerService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 看客户远程实现类
 * @Author: TANGMICHI
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/marketInsightCustomer")
public class RemoteMarketInsightCustomer implements RemoteMarketInsightCustomerService {
    @Autowired
    private IMarketInsightCustomerService marketInsightCustomerService;

    /**
     * 远程查询看客户是否被引用
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteMarketInsightCustomerList")
    public R<List<MarketInsightCustomerDTO>> remoteMarketInsightCustomerList(@RequestBody MarketInsightCustomerDTO marketInsightCustomerDTO, String source) {
        return R.ok(marketInsightCustomerService.remoteMarketInsightCustomerList(marketInsightCustomerDTO));
    }
}
