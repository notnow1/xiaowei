package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-09-26
 */
@RestController
@RequestMapping("industry")
public class IndustryController extends BaseController {


    @Autowired
    private IIndustryService industryService;

    @Autowired
    private IIndustryDefaultService industryDefaultService;

    /**
     * 分页查询行业列表
     */
//    @RequiresPermissions("system:manage:industry:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryDTO industryDTO) {
        startPage();
        List<IndustryDTO> list = industryService.selectIndustryList(industryDTO);
        return getDataTable(list);
    }

    /**
     * 查询行业列表
     */
//    @RequiresPermissions("system:manage:industry:list")
    @GetMapping("/list")
    public AjaxResult list(IndustryDTO industryDTO) {
        List<IndustryDTO> list = industryService.selectIndustryList(industryDTO);
        return AjaxResult.success(list);
    }

    /**
     * 获取启用行业列表
     */
//    @RequiresPermissions("system:manage:industry:list")
    @GetMapping("/enableList")
    public AjaxResult enableList(IndustryDTO industryDTO) {
        // todo 0-默认,1-自定义
        int enableType = industryService.getEnableType();
        if (enableType == 1) {
            return AjaxResult.success(industryService.selectIndustryList(industryDTO));
        } else {
            IndustryDefaultDTO industryDefaultDTO = new IndustryDefaultDTO();
            BeanUtils.copyProperties(industryDTO, industryDefaultDTO);
            return AjaxResult.success(industryDefaultService.selectIndustryDefaultList(industryDefaultDTO));
        }
    }

    /**
     * 修改启用行业类型
     */
//    @RequiresPermissions("system:manage:industry:list")
    @PostMapping("/enableEdit")
    public AjaxResult enableEdit(IndustryDTO industryDTO) {
        return AjaxResult.success(industryService.updateEnableType(industryDTO));
    }

    /**
     * 新增行业
     */
//    @RequiresPermissions("system:manage:industry:add")
    @Log(title = "新增行业", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryDTO industryDTO) {
        return toAjax(industryService.insertIndustry(industryDTO));
    }

    /**
     * 修改行业
     */
//    @RequiresPermissions("system:manage:industry:edit")
    @Log(title = "修改行业", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryDTO industryDTO) {
        return toAjax(industryService.updateIndustry(industryDTO));
    }

    /**
     * 行业配置详情
     */
//    @RequiresPermissions("operate:cloud:performanceRank:edit")
    @GetMapping("/info/{industryId}")
    public AjaxResult info(@PathVariable Long industryId) {
        return AjaxResult.success(industryService.detailIndustry(industryId));
    }

    /**
     * 逻辑删除行业
     */
//    @RequiresPermissions("system:manage:industry:remove")
    @Log(title = "删除行业", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryDTO industryDTO) {
        Long industryId = industryDTO.getIndustryId();
        if (StringUtils.isNull(industryId)) {
            return AjaxResult.error("行业id不能为空！");
        }
        return toAjax(industryService.logicDeleteIndustryByIndustryId(industryId));
    }

    /**
     * 批量新增行业
     */
//    @RequiresPermissions("system:manage:industry:insertIndustrys")
    @Log(title = "批量新增行业", businessType = BusinessType.INSERT)
    @PostMapping("/insertIndustrys")
    public AjaxResult insertIndustrys(@RequestBody List<IndustryDTO> industryDtos) {
        return toAjax(industryService.insertIndustrys(industryDtos));
    }

    /**
     * 逻辑批量删除行业
     */
//    @RequiresPermissions("system:manage:industry:removes")
    @Log(title = "批量删除行业", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> industryIds) {
        return toAjax(industryService.logicDeleteIndustryByIndustryIds(industryIds));
    }
}
