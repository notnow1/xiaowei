package net.qixiaowei.strategy.cloud.remote.marketInsight;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiMacroDetailDTO;
import net.qixiaowei.strategy.cloud.api.remote.industry.RemoteIndustryAttractionService;
import net.qixiaowei.strategy.cloud.api.remote.marketInsight.RemoteMarketInsightMacroService;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightMacroService;
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
@RequestMapping("/marketInsightMacro")
public class RemoteMarketInsightMacro implements RemoteMarketInsightMacroService {
    @Autowired
    private IMarketInsightMacroService marketInsightMacroService;
    /**
     * 看宏观远程调用列表查询是否被引用
     * @param marketInsightMacroDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteMarketInsightMacroList")
    public R<List<MarketInsightMacroDTO>> remoteMarketInsightMacroList(@RequestBody MarketInsightMacroDTO marketInsightMacroDTO, String source) {
        return R.ok(marketInsightMacroService.remoteMarketInsightMacroList(marketInsightMacroDTO));
    }

    /**
     * 看宏观远程调用列表查询是否被引用
     * @param miMacroDetailDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteMiMacroDetailList")
    public R<List<MiMacroDetailDTO>> remoteMiMacroDetailList(@RequestBody MiMacroDetailDTO miMacroDetailDTO, String source) {
        return R.ok(marketInsightMacroService.remoteMiMacroDetailList(miMacroDetailDTO));
    }
}
