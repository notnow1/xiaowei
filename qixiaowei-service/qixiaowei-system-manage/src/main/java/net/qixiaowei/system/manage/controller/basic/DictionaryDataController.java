package net.qixiaowei.system.manage.controller.basic;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.service.basic.IDictionaryDataService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-07
*/
@RestController
@RequestMapping("dictionaryData")
public class DictionaryDataController extends BaseController
{


    @Autowired
    private IDictionaryDataService dictionaryDataService;

    /**
    * 分页查询字典数据表列表
    */
    //@RequiresPermissions("system:manage:dictionaryData:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DictionaryDataDTO dictionaryDataDTO){
    startPage();
    List<DictionaryDataDTO> list = dictionaryDataService.selectDictionaryDataList(dictionaryDataDTO);
    return getDataTable(list);
    }

    /**
    * 查询字典数据表列表
    */
    //@RequiresPermissions("system:manage:dictionaryData:list")
    @GetMapping("/list")
    public AjaxResult list(DictionaryDataDTO dictionaryDataDTO){
    List<DictionaryDataDTO> list = dictionaryDataService.selectDictionaryDataList(dictionaryDataDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增字典数据表
    */
    //@RequiresPermissions("system:manage:dictionaryData:add")
    //@Log(title = "新增字典数据表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DictionaryDataDTO dictionaryDataDTO) {
    return toAjax(dictionaryDataService.insertDictionaryData(dictionaryDataDTO));
    }


    /**
    * 修改字典数据表
    */
    //@RequiresPermissions("system:manage:dictionaryData:edit")
    //@Log(title = "修改字典数据表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DictionaryDataDTO dictionaryDataDTO)
    {
    return toAjax(dictionaryDataService.updateDictionaryData(dictionaryDataDTO));
    }

    /**
    * 逻辑删除字典数据表
    */
    //@RequiresPermissions("system:manage:dictionaryData:remove")
    //@Log(title = "删除字典数据表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DictionaryDataDTO dictionaryDataDTO)
    {
    return toAjax(dictionaryDataService.logicDeleteDictionaryDataByDictionaryDataId(dictionaryDataDTO));
    }
    /**
    * 批量修改字典数据表
    */
    //@RequiresPermissions("system:manage:dictionaryData:edits")
    //@Log(title = "批量修改字典数据表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DictionaryDataDTO> dictionaryDataDtos)
    {
    return toAjax(dictionaryDataService.updateDictionaryDatas(dictionaryDataDtos));
    }

    /**
    * 批量新增字典数据表
    */
    //@RequiresPermissions("system:manage:dictionaryData:insertDictionaryDatas")
    //@Log(title = "批量新增字典数据表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDictionaryDatas")
    public AjaxResult insertDictionaryDatas(@RequestBody List<DictionaryDataDTO> dictionaryDataDtos)
    {
    return toAjax(dictionaryDataService.insertDictionaryDatas(dictionaryDataDtos));
    }

    /**
    * 逻辑批量删除字典数据表
    */
    //@RequiresPermissions("system:manage:dictionaryData:removes")
    //@Log(title = "批量删除字典数据表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<DictionaryDataDTO>  DictionaryDataDtos)
    {
    return toAjax(dictionaryDataService.logicDeleteDictionaryDataByDictionaryDataIds(DictionaryDataDtos));
    }
}