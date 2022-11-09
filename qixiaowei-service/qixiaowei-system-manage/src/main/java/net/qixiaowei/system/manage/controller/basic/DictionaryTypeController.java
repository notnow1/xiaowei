package net.qixiaowei.system.manage.controller.basic;

import java.util.List;
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
*
* @author TANGMICHI
* @since 2022-10-15
*/
@RestController
@RequestMapping("dictionaryType")
public class DictionaryTypeController extends BaseController
{


    @Autowired
    private IDictionaryTypeService dictionaryTypeService;


    /**
    * 查询字典类型表详情
    */
    //@RequiresPermissions("system:manage:dictionaryType:info")
    @GetMapping("/info/{dictionaryTypeId}")
    public AjaxResult info(@PathVariable Long dictionaryTypeId){
    DictionaryTypeDTO dictionaryTypeDTO = dictionaryTypeService.selectDictionaryTypeByDictionaryTypeId(dictionaryTypeId);
        return AjaxResult.success(dictionaryTypeDTO);
    }
    /**
     * 查询字典类型表详情
     */
    //@RequiresPermissions("system:manage:dictionaryType:info")
    @GetMapping("/infoType/{dictionaryType}")
    public AjaxResult info(@PathVariable String dictionaryType){
        return AjaxResult.success(dictionaryTypeService.selectDictionaryTypeByDictionaryType(dictionaryType));
    }

    /**
    * 分页查询字典类型表列表
    */
    //@RequiresPermissions("system:manage:dictionaryType:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DictionaryTypeDTO dictionaryTypeDTO){
    startPage();
    List<DictionaryTypeDTO> list = dictionaryTypeService.selectDictionaryTypeList(dictionaryTypeDTO);
    return getDataTable(list);
    }

    /**
    * 查询字典类型表列表
    */
    //@RequiresPermissions("system:manage:dictionaryType:list")
    @GetMapping("/list")
    public AjaxResult list(DictionaryTypeDTO dictionaryTypeDTO){
    List<DictionaryTypeDTO> list = dictionaryTypeService.selectDictionaryTypeList(dictionaryTypeDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增字典类型表
    */
    //@RequiresPermissions("system:manage:dictionaryType:add")
    //@Log(title = "新增字典类型表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DictionaryTypeDTO dictionaryTypeDTO) {
    return AjaxResult.success(dictionaryTypeService.insertDictionaryType(dictionaryTypeDTO));
    }


    /**
    * 修改字典类型表
    */
    //@RequiresPermissions("system:manage:dictionaryType:edit")
    //@Log(title = "修改字典类型表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DictionaryTypeDTO dictionaryTypeDTO)
    {
    return toAjax(dictionaryTypeService.updateDictionaryType(dictionaryTypeDTO));
    }

    /**
    * 逻辑删除字典类型表
    */
    //@RequiresPermissions("system:manage:dictionaryType:remove")
    //@Log(title = "删除字典类型表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DictionaryTypeDTO dictionaryTypeDTO)
    {
    return toAjax(dictionaryTypeService.logicDeleteDictionaryTypeByDictionaryTypeId(dictionaryTypeDTO));
    }
    /**
    * 批量修改字典类型表
    */
    //@RequiresPermissions("system:manage:dictionaryType:edits")
    //@Log(title = "批量修改字典类型表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DictionaryTypeDTO> dictionaryTypeDtos)
    {
    return toAjax(dictionaryTypeService.updateDictionaryTypes(dictionaryTypeDtos));
    }

    /**
    * 批量新增字典类型表
    */
    //@RequiresPermissions("system:manage:dictionaryType:insertDictionaryTypes")
    //@Log(title = "批量新增字典类型表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDictionaryTypes")
    public AjaxResult insertDictionaryTypes(@RequestBody List<DictionaryTypeDTO> dictionaryTypeDtos)
    {
    return toAjax(dictionaryTypeService.insertDictionaryTypes(dictionaryTypeDtos));
    }

    /**
    * 逻辑批量删除字典类型表
    */
    //@RequiresPermissions("system:manage:dictionaryType:removes")
    //@Log(title = "批量删除字典类型表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  dictionaryTypeIds)
    {
    return toAjax(dictionaryTypeService.logicDeleteDictionaryTypeByDictionaryTypeIds(dictionaryTypeIds));
    }
}
