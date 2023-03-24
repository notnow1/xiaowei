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
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2023-03-07
 */
@RestController
@RequestMapping("marketInsightCustomer")
public class MarketInsightCustomerController extends BaseController {


    @Autowired
    private IMarketInsightCustomerService marketInsightCustomerService;


    /**
     * 查询市场洞察客户表详情
     */
    @RequiresPermissions("strategy:cloud:marketInsightCustomer:info")
    @GetMapping("/info/{marketInsightCustomerId}")
    public AjaxResult info(@PathVariable Long marketInsightCustomerId) {
        MarketInsightCustomerDTO marketInsightCustomerDTO = marketInsightCustomerService.selectMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomerId);
        return AjaxResult.success(marketInsightCustomerDTO);
    }

    /**
     * 分页查询市场洞察客户表列表
     */
    @RequiresPermissions("strategy:cloud:marketInsightCustomer:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        startPage();
        List<MarketInsightCustomerDTO> list = marketInsightCustomerService.selectMarketInsightCustomerList(marketInsightCustomerDTO);
        if (StringUtils.isNotEmpty(list)) {
            Set<Long> userIds = list.stream().map(MarketInsightCustomerDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            list.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
        return getDataTable(list);
    }

    /**
     * 查询市场洞察客户表列表
     */
    @RequiresPermissions("strategy:cloud:marketInsightCustomer:list")
    @GetMapping("/list")
    public AjaxResult list(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        List<MarketInsightCustomerDTO> list = marketInsightCustomerService.selectMarketInsightCustomerList(marketInsightCustomerDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增市场洞察客户表
     */
    @RequiresPermissions("strategy:cloud:marketInsightCustomer:add")
    @Log(title = "保存看客户", businessType = BusinessType.MARKET_INSIGHT_CUSTOMER, businessId = "marketInsightCustomerId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated({MarketInsightCustomerDTO.AddMarketInsightCustomerDTO.class}) MarketInsightCustomerDTO marketInsightCustomerDTO) {
        return AjaxResult.success(marketInsightCustomerService.insertMarketInsightCustomer(marketInsightCustomerDTO));
    }


    /**
     * 修改市场洞察客户表
     */
    @RequiresPermissions("strategy:cloud:marketInsightCustomer:edit")
    @Log(title = "保存看客户", businessType = BusinessType.MARKET_INSIGHT_CUSTOMER, businessId = "marketInsightCustomerId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated({MarketInsightCustomerDTO.UpdateMarketInsightCustomerDTO.class}) MarketInsightCustomerDTO marketInsightCustomerDTO) {
        return toAjax(marketInsightCustomerService.updateMarketInsightCustomer(marketInsightCustomerDTO));
    }

    /**
     * 逻辑删除市场洞察客户表
     */
    @RequiresPermissions("strategy:cloud:marketInsightCustomer:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated({MarketInsightCustomerDTO.DeleteMarketInsightCustomerDTO.class}) MarketInsightCustomerDTO marketInsightCustomerDTO) {
        return toAjax(marketInsightCustomerService.logicDeleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomerDTO));
    }

    /**
     * 逻辑批量删除市场洞察客户表
     */
    @RequiresPermissions("strategy:cloud:marketInsightCustomer:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> marketInsightCustomerIds) {
        return toAjax(marketInsightCustomerService.logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(marketInsightCustomerIds));
    }

}
