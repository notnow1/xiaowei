package net.qixiaowei.strategy.cloud.controller.businessDesign;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Graves
 * @since 2023-02-28
 */
@RestController
@RequestMapping("businessDesign")
public class BusinessDesignController extends BaseController {


    @Autowired
    private IBusinessDesignService businessDesignService;


    /**
     * 查询业务设计表详情
     */
    @RequiresPermissions("strategy:cloud:businessDesign:info")
    @GetMapping("/info/{businessDesignId}")
    public AjaxResult info(@PathVariable Long businessDesignId) {
        BusinessDesignDTO businessDesignDTO = businessDesignService.selectBusinessDesignByBusinessDesignId(businessDesignId);
        return AjaxResult.success(businessDesignDTO);
    }

    /**
     * 分页查询业务设计表列表
     */
    @RequiresPermissions("strategy:cloud:businessDesign:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(BusinessDesignDTO businessDesignDTO) {
        startPage();
        List<BusinessDesignDTO> list = businessDesignService.selectBusinessDesignList(businessDesignDTO);
        return getDataTable(list);
    }

    /**
     * 查询业务设计表列表
     */
    @RequiresPermissions("strategy:cloud:businessDesign:list")
    @GetMapping("/list")
    public AjaxResult list(BusinessDesignDTO businessDesignDTO) {
        List<BusinessDesignDTO> list = businessDesignService.selectBusinessDesignList(businessDesignDTO);
        List<BusinessDesignDTO> businessDesignDTOS = new ArrayList<>();
        List<Long> businessDesignIds = new ArrayList<>();
        for (BusinessDesignDTO designDTO : list) {
            Long planBusinessUnitId = designDTO.getPlanBusinessUnitId();
            if (!businessDesignIds.contains(planBusinessUnitId)) {
                businessDesignIds.add(planBusinessUnitId);
                businessDesignDTOS.add(designDTO);
            }
        }
        return AjaxResult.success(businessDesignDTOS);
    }


    /**
     * 新增业务设计表
     */
    @RequiresPermissions("strategy:cloud:businessDesign:add")
    @Log(title = "新增业务设计表", businessType = BusinessType.BUSINESS_DESIGN, businessId = "businessDesignId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody BusinessDesignDTO businessDesignDTO) {
        return AjaxResult.success(businessDesignService.insertBusinessDesign(businessDesignDTO));
    }


    /**
     * 修改业务设计表
     */
    @RequiresPermissions("strategy:cloud:businessDesign:edit")
    @Log(title = "修改业务设计表", businessType = BusinessType.BUSINESS_DESIGN, businessId = "businessDesignId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody BusinessDesignDTO businessDesignDTO) {
        return toAjax(businessDesignService.updateBusinessDesign(businessDesignDTO));
    }

    /**
     * 逻辑删除业务设计表
     */
    @RequiresPermissions("strategy:cloud:businessDesign:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody BusinessDesignDTO businessDesignDTO) {
        return toAjax(businessDesignService.logicDeleteBusinessDesignByBusinessDesignId(businessDesignDTO));
    }

    /**
     * 逻辑批量删除业务设计表
     */
    @RequiresPermissions("strategy:cloud:businessDesign:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> businessDesignIds) {
        return toAjax(businessDesignService.logicDeleteBusinessDesignByBusinessDesignIds(businessDesignIds));
    }

}
