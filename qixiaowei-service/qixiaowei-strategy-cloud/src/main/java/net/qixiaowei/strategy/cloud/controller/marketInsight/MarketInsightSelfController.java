package net.qixiaowei.strategy.cloud.controller.marketInsight;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    public AjaxResult addSave(@RequestBody MarketInsightSelfDTO marketInsightSelfDTO) {
        return AjaxResult.success(marketInsightSelfService.insertMarketInsightSelf(marketInsightSelfDTO));
    }


    /**
     * 修改市场洞察自身表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:edit")
    @Log(title = "修改市场洞察自身表", businessType = BusinessType.MARKET_INSIGHT_SELF, businessId = "marketInsightSelfId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody MarketInsightSelfDTO marketInsightSelfDTO) {
        return toAjax(marketInsightSelfService.updateMarketInsightSelf(marketInsightSelfDTO));
    }

    /**
     * 逻辑删除市场洞察自身表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody MarketInsightSelfDTO marketInsightSelfDTO) {
        return toAjax(marketInsightSelfService.logicDeleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelfDTO));
    }

    /**
     * 逻辑批量删除市场洞察自身表
     */
    @RequiresPermissions("strategy:cloud:marketInsightSelf:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> marketInsightSelfIds) {
        return toAjax(marketInsightSelfService.logicDeleteMarketInsightSelfByMarketInsightSelfIds(marketInsightSelfIds));
    }
}