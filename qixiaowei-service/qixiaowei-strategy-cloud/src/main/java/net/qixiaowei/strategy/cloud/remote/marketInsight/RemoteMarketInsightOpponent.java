package net.qixiaowei.strategy.cloud.remote.marketInsight;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightOpponentDTO;
import net.qixiaowei.strategy.cloud.api.remote.industry.RemoteIndustryAttractionService;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightOpponentService;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightOpponentService;
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
@RequestMapping("/marketInsightOpponent")
public class RemoteMarketInsightOpponent implements RemoteMarketInsightOpponentService {
    @Autowired
    private IMarketInsightOpponentService marketInsightOpponentService;
    /**
     * 看对手远程查询列表是否被引用
     * @param marketInsightOpponentDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteMarketInsightOpponentList")
    public R<List<MarketInsightOpponentDTO>> remoteMarketInsightOpponentList(@RequestBody MarketInsightOpponentDTO marketInsightOpponentDTO, String source) {
        return R.ok(marketInsightOpponentService.remoteMarketInsightOpponentList(marketInsightOpponentDTO));
    }
}
