package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.service.basic.IDictionaryDataService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-15
 */
@RestController
@RequestMapping("dictionaryData")
public class DictionaryDataController extends BaseController {


    @Autowired
    private IDictionaryDataService dictionaryDataService;


    /**
     * 查询字典数据表详情
     */
    @RequiresPermissions("system:manage:dictionaryData:info")
    @GetMapping("/info/{dictionaryDataId}")
    public AjaxResult info(@PathVariable Long dictionaryDataId) {
        DictionaryDataDTO dictionaryDataDTO = dictionaryDataService.selectDictionaryDataByDictionaryDataId(dictionaryDataId);
        return AjaxResult.success(dictionaryDataDTO);
    }

    /**
     * 分页查询字典数据表列表
     */
    @RequiresPermissions("system:manage:dictionaryData:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DictionaryDataDTO dictionaryDataDTO) {
        startPage();
        List<DictionaryDataDTO> list = dictionaryDataService.selectDictionaryDataList(dictionaryDataDTO);
        return getDataTable(list);
    }

    /**
     * 新增字典数据表
     */
    @RequiresPermissions("system:manage:dictionaryData:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DictionaryDataDTO dictionaryDataDTO) {
        return AjaxResult.success(dictionaryDataService.insertDictionaryData(dictionaryDataDTO));
    }


    /**
     * 修改字典数据表
     */
    @RequiresPermissions("system:manage:dictionaryData:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DictionaryDataDTO dictionaryDataDTO) {
        return toAjax(dictionaryDataService.updateDictionaryData(dictionaryDataDTO));
    }

    /**
     * 逻辑删除字典数据表
     */
    @RequiresPermissions("system:manage:dictionaryData:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DictionaryDataDTO dictionaryDataDTO) {
        return toAjax(dictionaryDataService.logicDeleteDictionaryDataByDictionaryDataId(dictionaryDataDTO));
    }

    /**
     * 逻辑批量删除字典数据表
     */
    @RequiresPermissions("system:manage:dictionaryData:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> dictionaryDataIds) {
        return toAjax(dictionaryDataService.logicDeleteDictionaryDataByDictionaryDataIds(dictionaryDataIds));
    }


    /**
     * 查询字典数据表列表
     */
    @GetMapping("/list")
    public AjaxResult list(DictionaryDataDTO dictionaryDataDTO) {
        List<DictionaryDataDTO> list = dictionaryDataService.selectDictionaryDataList(dictionaryDataDTO);
        return AjaxResult.success(list);
    }
    /**
     * 查询字典数据表列表包括失效
     */
    @GetMapping("/listAll")
    public AjaxResult listAll(DictionaryDataDTO dictionaryDataDTO) {
        List<DictionaryDataDTO> list = dictionaryDataService.selectDictionaryDataListAll(dictionaryDataDTO);
        return AjaxResult.success(list);
    }
}
