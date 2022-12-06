package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
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
    @RequiresPermissions("system:manage:officialRankSystem:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(OfficialRankSystemDTO officialRankSystemDTO) {
        startPage();
        List<OfficialRankSystemDTO> list = officialRankSystemService.selectOfficialRankSystemPageList(officialRankSystemDTO);
        return getDataTable(list);
    }

    /**
     * 新增职级体系表
     */
    @RequiresPermissions("system:manage:officialRankSystem:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody OfficialRankSystemDTO officialRankSystemDTO) {
        return AjaxResult.success(officialRankSystemService.insertOfficialRankSystem(officialRankSystemDTO));
    }

    /**
     * 修改职级体系表
     */
    @RequiresPermissions("system:manage:officialRankSystem:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OfficialRankSystemDTO officialRankSystemDTO) {
        return toAjax(officialRankSystemService.updateOfficialRankSystem(officialRankSystemDTO));
    }

    /**
     * 根据officialRankSystemId查询职级体系表详情
     */
    @RequiresPermissions(value = {"system:manage:officialRankSystem:info", "system:manage:officialRankSystem:edit"}, logical = Logical.OR)
    @GetMapping("/info/{officialRankSystemId}")
    public AjaxResult info(@PathVariable Long officialRankSystemId) {
        return AjaxResult.success(officialRankSystemService.detailOfficialRankSystem(officialRankSystemId));
    }

    /**
     * 逻辑删除职级体系表
     */
    @RequiresPermissions("system:manage:officialRankSystem:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody OfficialRankSystemDTO officialRankSystemDTO) {
        return toAjax(officialRankSystemService.logicDeleteOfficialRankSystemByOfficialRankSystemId(officialRankSystemDTO));
    }

    /**
     * 逻辑批量删除职级体系表
     */
    @RequiresPermissions("system:manage:officialRankSystem:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> OfficialRankSystemIds) {
        return toAjax(officialRankSystemService.logicDeleteOfficialRankSystemByOfficialRankSystemIds(OfficialRankSystemIds));
    }


    /**
     * 职级分解维度下拉框
     */
    @RequiresPermissions(value = {"system:manage:officialRankSystem:info", "system:manage:officialRankSystem:edit"}, logical = Logical.OR)
    @GetMapping("/decomposeDrop/{rankDecomposeDimension}")
    public AjaxResult decomposeDrop(@PathVariable Integer rankDecomposeDimension) {
        return AjaxResult.success(officialRankSystemService.decomposeDrop(rankDecomposeDimension));
    }

    /**
     * 通过Id查找职级上下限
     *
     * @param officialRankSystemId
     * @return
     */
    @GetMapping("/selectRankById/{officialRankSystemId}")
    public AjaxResult selectRankById(@PathVariable Long officialRankSystemId) {
        return AjaxResult.success(officialRankSystemService.selectOfficialRankByOfficialRankSystemId(officialRankSystemId));
    }

    /**
     * 通过Id查找职级上下限
     *
     * @param officialRankSystemId
     * @return
     */
    @GetMapping("/selectRankMapById/{officialRankSystemId}")
    public AjaxResult selectRankMapById(@PathVariable Long officialRankSystemId) {
        return AjaxResult.success(officialRankSystemService.selectOfficialRankMapBySystemId(officialRankSystemId));
    }

    /**
     * 查询职级体系表列表
     */
    @GetMapping("/list")
    public AjaxResult list(OfficialRankSystemDTO officialRankSystemDTO) {
        List<OfficialRankSystemDTO> list = officialRankSystemService.selectOfficialRankSystemList(officialRankSystemDTO);
        return AjaxResult.success(list);
    }
}
