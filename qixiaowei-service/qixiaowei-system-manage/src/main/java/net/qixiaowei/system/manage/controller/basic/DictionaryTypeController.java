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
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
import net.qixiaowei.system.manage.service.basic.IDictionaryTypeService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-15
 */
@RestController
@RequestMapping("dictionaryType")
public class DictionaryTypeController extends BaseController {


    @Autowired
    private IDictionaryTypeService dictionaryTypeService;


    /**
     * 分页查询字典类型表列表
     */
    @RequiresPermissions("system:manage:dictionaryType:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DictionaryTypeDTO dictionaryTypeDTO) {
        startPage();
        List<DictionaryTypeDTO> list = dictionaryTypeService.selectDictionaryTypeList(dictionaryTypeDTO);
        return getDataTable(list);
    }


    /**
     * 查询字典类型表详情
     */
    @RequiresPermissions(value = {"system:manage:dictionaryType:pageList","system:manage:dictionaryType:edit"},logical = Logical.OR)
    @GetMapping("/info/{dictionaryTypeId}")
    public AjaxResult info(@PathVariable Long dictionaryTypeId) {
        DictionaryTypeDTO dictionaryTypeDTO = dictionaryTypeService.selectDictionaryTypeByDictionaryTypeId(dictionaryTypeId);
        return AjaxResult.success(dictionaryTypeDTO);
    }

    /**
     * 查询字典类型表详情
     */
    @RequiresPermissions(value = {"system:manage:dictionaryType:pageList","system:manage:dictionaryType:edit"},logical = Logical.OR)
    @GetMapping("/infoType/{dictionaryType}")
    public AjaxResult info(@PathVariable String dictionaryType) {
        return AjaxResult.success(dictionaryTypeService.selectDictionaryTypeByDictionaryType(dictionaryType));
    }

    /**
     * 新增字典类型表
     */
    @RequiresPermissions("system:manage:dictionaryType:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DictionaryTypeDTO dictionaryTypeDTO) {
        return AjaxResult.success(dictionaryTypeService.insertDictionaryType(dictionaryTypeDTO));
    }

    /**
     * 修改字典类型表
     */
    @RequiresPermissions("system:manage:dictionaryType:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DictionaryTypeDTO dictionaryTypeDTO) {
        return toAjax(dictionaryTypeService.updateDictionaryType(dictionaryTypeDTO));
    }

    /**
     * 逻辑删除字典类型表
     */
    @RequiresPermissions("system:manage:dictionaryType:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DictionaryTypeDTO dictionaryTypeDTO) {
        return toAjax(dictionaryTypeService.logicDeleteDictionaryTypeByDictionaryTypeId(dictionaryTypeDTO));
    }

    /**
     * 逻辑批量删除字典类型表
     */
    @RequiresPermissions("system:manage:dictionaryType:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> dictionaryTypeIds) {
        return toAjax(dictionaryTypeService.logicDeleteDictionaryTypeByDictionaryTypeIds(dictionaryTypeIds));
    }

    /**
     * 查询字典类型表列表
     */
    @GetMapping("/list")
    public AjaxResult list(DictionaryTypeDTO dictionaryTypeDTO) {
        List<DictionaryTypeDTO> list = dictionaryTypeService.selectDictionaryTypeList(dictionaryTypeDTO);
        return AjaxResult.success(list);
    }
}
