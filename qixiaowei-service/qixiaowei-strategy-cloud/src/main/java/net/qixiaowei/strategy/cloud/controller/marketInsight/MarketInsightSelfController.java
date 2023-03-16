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
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentChoiceDTO;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2023-03-13
 */
@RestController
@RequestMapping("marketInsightSelf")
public class MarketInsightSelfController extends BaseController {


    @Autowired
    private IMarketInsightSelfService marketInsightSelfService;


    /**
     * 查询市场洞察自身表详情
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:info")
    @GetMapping("/info/{marketInsightSelfId}")
    public AjaxResult info(@PathVariable Long marketInsightSelfId) {
        MarketInsightSelfDTO marketInsightSelfDTO = marketInsightSelfService.selectMarketInsightSelfByMarketInsightSelfId(marketInsightSelfId);
        return AjaxResult.success(marketInsightSelfDTO);
    }

    /**
     * 分页查询市场洞察自身表列表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MarketInsightSelfDTO marketInsightSelfDTO) {
        startPage();
        List<MarketInsightSelfDTO> list = marketInsightSelfService.selectMarketInsightSelfList(marketInsightSelfDTO);
        if (StringUtils.isNotEmpty(list)) {
            Set<Long> userIds = list.stream().map(MarketInsightSelfDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            list.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
        return getDataTable(list);
    }

    /**
     * 查询市场洞察自身表列表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:list")
    @GetMapping("/list")
    public AjaxResult list(MarketInsightSelfDTO marketInsightSelfDTO) {
        List<MarketInsightSelfDTO> list = marketInsightSelfService.selectMarketInsightSelfList(marketInsightSelfDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增市场洞察自身表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:add")
    @Log(title = "新增市场洞察自身表", businessType = BusinessType.MARKET_INSIGHT_SELF, businessId = "marketInsightSelfId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated({MarketInsightSelfDTO.AddMarketInsightSelfDTO.class}) MarketInsightSelfDTO marketInsightSelfDTO) {
        return AjaxResult.success(marketInsightSelfService.insertMarketInsightSelf(marketInsightSelfDTO));
    }


    /**
     * 修改市场洞察自身表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:edit")
    @Log(title = "修改市场洞察自身表", businessType = BusinessType.MARKET_INSIGHT_SELF, businessId = "marketInsightSelfId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated({MarketInsightSelfDTO.UpdateMarketInsightSelfDTO.class}) MarketInsightSelfDTO marketInsightSelfDTO) {
        return toAjax(marketInsightSelfService.updateMarketInsightSelf(marketInsightSelfDTO));
    }

    /**
     * 逻辑删除市场洞察自身表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated({MarketInsightSelfDTO.DeleteMarketInsightSelfDTO.class}) MarketInsightSelfDTO marketInsightSelfDTO) {
        return toAjax(marketInsightSelfService.logicDeleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelfDTO));
    }

    /**
     * 逻辑批量删除市场洞察自身表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> marketInsightSelfIds) {
        return toAjax(marketInsightSelfService.logicDeleteMarketInsightSelfByMarketInsightSelfIds(marketInsightSelfIds));
    }
    /**
     * 根据规划年度和业务单元查询看对手详情表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:opponentNameList")
    @GetMapping("/opponentNameList")
    public AjaxResult opponentNameList(MarketInsightSelfDTO marketInsightSelfDTO) {
        return AjaxResult.success(marketInsightSelfService.opponentNameList(marketInsightSelfDTO));
    }

}
