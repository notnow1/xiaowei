package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.basic.NationDTO;
import net.qixiaowei.system.manage.service.basic.INationService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-20
 */
@RestController
@RequestMapping("nation")
public class NationController extends BaseController {


    @Autowired
    private INationService nationService;


    /**
     * 查询民族表详情
     */
    @RequiresPermissions("system:manage:nation:info")
    @GetMapping("/info/{nationId}")
    public AjaxResult info(@PathVariable Long nationId) {
        NationDTO nationDTO = nationService.selectNationByNationId(nationId);
        return AjaxResult.success(nationDTO);
    }

    /**
     * 分页查询民族表列表
     */
    @RequiresPermissions("system:manage:nation:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(NationDTO nationDTO) {
        startPage();
        List<NationDTO> list = nationService.selectNationList(nationDTO);
        return getDataTable(list);
    }

    /**
     * 新增民族表
     */
    @RequiresPermissions("system:manage:nation:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody NationDTO nationDTO) {
        return AjaxResult.success(nationService.insertNation(nationDTO));
    }


    /**
     * 修改民族表
     */
    @RequiresPermissions("system:manage:nation:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody NationDTO nationDTO) {
        return toAjax(nationService.updateNation(nationDTO));
    }

    /**
     * 逻辑删除民族表
     */
    @RequiresPermissions("system:manage:nation:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody NationDTO nationDTO) {
        return toAjax(nationService.logicDeleteNationByNationId(nationDTO));
    }

    /**
     * 逻辑批量删除民族表
     */
    @RequiresPermissions("system:manage:nation:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> nationIds) {
        return toAjax(nationService.logicDeleteNationByNationIds(nationIds));
    }


    /**
     * 查询民族表列表
     */
    @GetMapping("/list")
    public AjaxResult list(NationDTO nationDTO) {
        List<NationDTO> list = nationService.selectNationList(nationDTO);
        return AjaxResult.success(list);
    }

}
