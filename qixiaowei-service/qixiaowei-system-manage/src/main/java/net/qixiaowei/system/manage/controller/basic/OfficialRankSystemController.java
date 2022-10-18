package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.service.basic.IOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-10-07
 */
@RestController
@RequestMapping("officialRankSystem")
public class OfficialRankSystemController extends BaseController {

    @Autowired
    private IOfficialRankSystemService officialRankSystemService;

    /**
     * 分页查询职级体系表列表
     */
//    @RequiresPermissions("system:manage:officialRankSystem:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(OfficialRankSystemDTO officialRankSystemDTO) {
        startPage();
        List<OfficialRankSystemDTO> list = officialRankSystemService.selectOfficialRankSystemPageList(officialRankSystemDTO);
        return getDataTable(list);
    }

    /**
     * 查询职级体系表列表
     */
//    @RequiresPermissions("system:manage:officialRankSystem:list")
    @GetMapping("/list")
    public AjaxResult list(OfficialRankSystemDTO officialRankSystemDTO) {
        List<OfficialRankSystemDTO> list = officialRankSystemService.selectOfficialRankSystemList(officialRankSystemDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增职级体系表
     */
//    @RequiresPermissions("system:manage:officialRankSystem:add")
    @Log(title = "新增职级体系表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody OfficialRankSystemDTO officialRankSystemDTO) {
        return toAjax(officialRankSystemService.insertOfficialRankSystem(officialRankSystemDTO));
    }

    /**
     * 修改职级体系表
     */
//    @RequiresPermissions("system:manage:officialRankSystem:edit")
    @Log(title = "修改职级体系表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OfficialRankSystemDTO officialRankSystemDTO) {
        return toAjax(officialRankSystemService.updateOfficialRankSystem(officialRankSystemDTO));
    }

    /**
     * 根据officialRankSystemId查询职级体系表详情
     */
//    @RequiresPermissions("system:manage:officialRankSystem:add")
    @Log(title = "职级体系表详情")
    @GetMapping("/info/{officialRankSystemId}")
    public AjaxResult info(@PathVariable Long officialRankSystemId) {
        return AjaxResult.success(officialRankSystemService.detailOfficialRankSystem(officialRankSystemId));
    }

    /**
     * 逻辑删除职级体系表
     */
//    @RequiresPermissions("system:manage:officialRankSystem:remove")
    @Log(title = "删除职级体系表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody OfficialRankSystemDTO officialRankSystemDTO) {
        return toAjax(officialRankSystemService.logicDeleteOfficialRankSystemByOfficialRankSystemId(officialRankSystemDTO));
    }

    /**
     * 逻辑批量删除职级体系表
     */
//    @RequiresPermissions("system:manage:officialRankSystem:removes")
    @Log(title = "批量删除职级体系表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> OfficialRankSystemIds) {
        return toAjax(officialRankSystemService.logicDeleteOfficialRankSystemByOfficialRankSystemIds(OfficialRankSystemIds));
    }
}
