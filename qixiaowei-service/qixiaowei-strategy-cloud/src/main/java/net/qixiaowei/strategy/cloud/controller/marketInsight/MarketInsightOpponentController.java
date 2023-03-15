package net.qixiaowei.strategy.cloud.controller.marketInsight;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightOpponentDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightOpponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
*
* @author TANGMICHI
* @since 2023-03-12
*/
@RestController
@RequestMapping("marketInsightOpponent")
public class MarketInsightOpponentController extends BaseController
{


    @Autowired
    private IMarketInsightOpponentService marketInsightOpponentService;



    /**
    * 查询市场洞察对手表详情
    */
    @RequiresPermissions("strategy:cloud:marketInsightOpponent:info")
    @GetMapping("/info/{marketInsightOpponentId}")
    public AjaxResult info(@PathVariable Long marketInsightOpponentId){
    MarketInsightOpponentDTO marketInsightOpponentDTO = marketInsightOpponentService.selectMarketInsightOpponentByMarketInsightOpponentId(marketInsightOpponentId);
        return AjaxResult.success(marketInsightOpponentDTO);
    }

    /**
    * 分页查询市场洞察对手表列表
    */
    @RequiresPermissions("strategy:cloud:marketInsightOpponent:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MarketInsightOpponentDTO marketInsightOpponentDTO){
    startPage();
    List<MarketInsightOpponentDTO> list = marketInsightOpponentService.selectMarketInsightOpponentList(marketInsightOpponentDTO);
        if (StringUtils.isNotEmpty(list)) {
            Set<Long> userIds = list.stream().map(MarketInsightOpponentDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            list.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    return getDataTable(list);
    }

    /**
    * 查询市场洞察对手表列表
    */
    @RequiresPermissions("strategy:cloud:marketInsightOpponent:list")
    @GetMapping("/list")
    public AjaxResult list(MarketInsightOpponentDTO marketInsightOpponentDTO){
    List<MarketInsightOpponentDTO> list = marketInsightOpponentService.selectMarketInsightOpponentList(marketInsightOpponentDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增市场洞察对手表
    */
    @RequiresPermissions("strategy:cloud:marketInsightOpponent:add")
    @Log(title = "新增市场洞察对手表", businessType = BusinessType.MARKET_INSIGHT_OPPONENT, businessId = "marketInsightOpponentId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated({MarketInsightOpponentDTO.AddMarketInsightOpponentDTO.class}) MarketInsightOpponentDTO marketInsightOpponentDTO) {
    return AjaxResult.success(marketInsightOpponentService.insertMarketInsightOpponent(marketInsightOpponentDTO));
    }


    /**
    * 修改市场洞察对手表
    */
    @RequiresPermissions("strategy:cloud:marketInsightOpponent:edit")
    @Log(title = "修改市场洞察对手表", businessType = BusinessType.MARKET_INSIGHT_OPPONENT, businessId = "marketInsightOpponentId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated({MarketInsightOpponentDTO.UpdateMarketInsightOpponentDTO.class}) MarketInsightOpponentDTO marketInsightOpponentDTO)
    {
    return toAjax(marketInsightOpponentService.updateMarketInsightOpponent(marketInsightOpponentDTO));
    }

    /**
    * 逻辑删除市场洞察对手表
    */
    @RequiresPermissions("strategy:cloud:marketInsightOpponent:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated({MarketInsightOpponentDTO.DeleteMarketInsightOpponentDTO.class}) MarketInsightOpponentDTO marketInsightOpponentDTO)
    {
    return toAjax(marketInsightOpponentService.logicDeleteMarketInsightOpponentByMarketInsightOpponentId(marketInsightOpponentDTO));
    }
    /**
    * 逻辑批量删除市场洞察对手表
    */
    @RequiresPermissions("strategy:cloud:marketInsightOpponent:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  marketInsightOpponentIds)
    {
    return toAjax(marketInsightOpponentService.logicDeleteMarketInsightOpponentByMarketInsightOpponentIds(marketInsightOpponentIds));
    }
}
