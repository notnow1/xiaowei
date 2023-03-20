package net.qixiaowei.strategy.cloud.remote.marketInsight;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightIndustryService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 看行业配置远程实现类
 * @Author: TANGMICHI
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/marketInsightIndustry")
public class RemoteMarketInsightIndustry implements RemoteMarketInsightIndustryService {
    @Autowired
    private IMarketInsightIndustryService marketInsightIndustryService;
    /**
     * 远程查询看行业列表是否被引用
     * @param marketInsightIndustryDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteMarketInsightIndustryList")
    public R<List<MarketInsightIndustryDTO>> remoteMarketInsightIndustryList(@RequestBody MarketInsightIndustryDTO marketInsightIndustryDTO, String source) {
        return R.ok(marketInsightIndustryService.remoteMarketInsightCustomerList(marketInsightIndustryDTO));
    }
}
