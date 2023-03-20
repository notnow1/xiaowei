package net.qixiaowei.strategy.cloud.remote.marketInsight;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.api.remote.industry.RemoteIndustryAttractionService;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightSelfService;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 行业吸引力配置远程实现类
 * @Author: TANGMICHI
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/marketInsightSelf")
public class RemoteMarketInsightSelf implements RemoteMarketInsightSelfService {
    @Autowired
    private IMarketInsightSelfService marketInsightSelfService;
    /**
     * 看自身远程调用列表查询是否被引用
     * @param marketInsightSelfDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteMarketInsightSelfList")
    public R<List<MarketInsightSelfDTO>> remoteMarketInsightSelfList(@RequestBody  MarketInsightSelfDTO marketInsightSelfDTO, String source) {
        return R.ok(marketInsightSelfService.remoteMarketInsightSelfList(marketInsightSelfDTO));
    }
}
