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
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.service.basic.IOfficialRankDecomposeService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-10-07
 */
@RestController
@RequestMapping("officialRankDecompose")
public class OfficialRankDecomposeController extends BaseController {


    @Autowired
    private IOfficialRankDecomposeService officialRankDecomposeService;

    /**
     * 分页查询职级分解表列表
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        startPage();
        List<OfficialRankDecomposeDTO> list = officialRankDecomposeService.selectOfficialRankDecomposeList(officialRankDecomposeDTO);
        return getDataTable(list);
    }

    /**
     * 查询职级分解表列表
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:list")
    @GetMapping("/list")
    public AjaxResult list(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        List<OfficialRankDecomposeDTO> list = officialRankDecomposeService.selectOfficialRankDecomposeList(officialRankDecomposeDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增职级分解表
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:add")
    @Log(title = "新增职级分解表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        return toAjax(officialRankDecomposeService.insertOfficialRankDecompose(officialRankDecomposeDTO));
    }

    /**
     * 修改职级分解表
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:edit")
    @Log(title = "修改职级分解表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        return toAjax(officialRankDecomposeService.updateOfficialRankDecompose(officialRankDecomposeDTO));
    }

    /**
     * 逻辑删除职级分解表
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:remove")
    @Log(title = "删除职级分解表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        return toAjax(officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecomposeDTO));
    }

    /**
     * 逻辑批量删除职级分解表
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:removes")
    @Log(title = "批量删除职级分解表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<OfficialRankDecomposeDTO> OfficialRankDecomposeDtos) {
        return toAjax(officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(OfficialRankDecomposeDtos));
    }

    /**
     * 查询职级分解表详情
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:pageList")
    @GetMapping("/detail")
    public AjaxResult detail(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        return AjaxResult.success(officialRankDecomposeService.selectOfficialRankDecomposeByOfficialRankSystemDTO(officialRankDecomposeDTO));
    }

}
