package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.CountryDTO;
import net.qixiaowei.system.manage.service.basic.ICountryService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-20
 */
@RestController
@RequestMapping("country")
public class CountryController extends BaseController {


    @Autowired
    private ICountryService countryService;


    /**
     * 查询国家表详情
     */
    @RequiresPermissions(value = {"system:manage:country:info", "system:manage:country:edit"}, logical = Logical.OR)
    @GetMapping("/info/{countryId}")
    public AjaxResult info(@PathVariable Long countryId) {
        CountryDTO countryDTO = countryService.selectCountryByCountryId(countryId);
        return AjaxResult.success(countryDTO);
    }

    /**
     * 分页查询国家表列表
     */
    @RequiresPermissions("system:manage:country:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(CountryDTO countryDTO) {
        startPage();
        List<CountryDTO> list = countryService.selectCountryList(countryDTO);
        return getDataTable(list);
    }

    /**
     * 新增国家表
     */
    @RequiresPermissions("system:manage:country:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody CountryDTO countryDTO) {
        return AjaxResult.success(countryService.insertCountry(countryDTO));
    }

    /**
     * 修改国家表
     */
    @RequiresPermissions("system:manage:country:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody CountryDTO countryDTO) {
        return toAjax(countryService.updateCountry(countryDTO));
    }

    /**
     * 逻辑删除国家表
     */
    @RequiresPermissions("system:manage:country:remove")
    @Log(title = "删除国家表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody CountryDTO countryDTO) {
        return toAjax(countryService.logicDeleteCountryByCountryId(countryDTO));
    }

    /**
     * 逻辑批量删除国家表
     */
    @RequiresPermissions("system:manage:country:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> countryIds) {
        return toAjax(countryService.logicDeleteCountryByCountryIds(countryIds));
    }

    /**
     * 查询国家表列表
     */
    @GetMapping("/list")
    public AjaxResult list(CountryDTO countryDTO) {
        List<CountryDTO> list = countryService.selectCountryList(countryDTO);
        return AjaxResult.success(list);
    }
}
