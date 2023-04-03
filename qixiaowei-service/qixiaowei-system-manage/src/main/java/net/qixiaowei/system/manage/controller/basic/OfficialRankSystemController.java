package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
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
    @Log(title = "新增职级", businessType = BusinessType.OFFICIAL_RANK_SYSTEM, businessId = "officialRankSystemId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:officialRankSystem:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody OfficialRankSystemDTO officialRankSystemDTO) {
        return AjaxResult.success("职级配置新增成功", officialRankSystemService.insertOfficialRankSystem(officialRankSystemDTO));
    }

    /**
     * 修改职级体系表
     */
    @Log(title = "编辑职级", businessType = BusinessType.OFFICIAL_RANK_SYSTEM, businessId = "officialRankSystemId", operationType = OperationType.UPDATE)
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
    @GetMapping("/decomposeDrop")
    public AjaxResult decomposeDrop(@RequestParam("type") Integer type, @RequestParam("status") Integer status) {
        return AjaxResult.success(officialRankSystemService.decomposeDrop(type, status));
    }

    /**
     * 查询岗位职级一览表
     */
    @RequiresPermissions("system:manage:officialRankSystem:rankView:list")
    @GetMapping("/rankView/list")
    public AjaxResult rankViewList(OfficialRankSystemDTO officialRankSystemDTO) {
        return AjaxResult.success(officialRankSystemService.selectRankViewList(officialRankSystemDTO));
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
     * @param officialRankSystemId 职级id
     * @return
     */
    @GetMapping("/selectRankMapById/{officialRankSystemId}")
    public AjaxResult selectRankMapById(@PathVariable Long officialRankSystemId) {
        return AjaxResult.success(officialRankSystemService.selectOfficialRankMapBySystemId(officialRankSystemId));
    }

    /**
     * 根据岗位查询调薪的职级下拉列表
     *
     * @param postId 岗位
     * @return
     */
    @GetMapping("/selectRankByPost/{postId}")
    public AjaxResult selectRankMapByPostId(@PathVariable Long postId) {
        return AjaxResult.success(officialRankSystemService.selectOfficialRankMapByPostId(postId));
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
