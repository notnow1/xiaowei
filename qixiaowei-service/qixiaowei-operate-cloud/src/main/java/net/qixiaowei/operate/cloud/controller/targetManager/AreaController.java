package net.qixiaowei.operate.cloud.controller.targetManager;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.service.targetManager.IAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-10-07
 */
@RestController
@RequestMapping("area")
public class AreaController extends BaseController {


    @Autowired
    private IAreaService areaService;


    /**
     * 分页查询区域表列表
     */
    @RequiresPermissions("operate:cloud:area:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(AreaDTO areaDTO) {
        startPage();
        List<AreaDTO> list = areaService.selectAreaList(areaDTO);
        return getDataTable(list);
    }

    /**
     * 查询区域表列表
     */
    @GetMapping("/list")
    public AjaxResult list(AreaDTO areaDTO) {
        List<AreaDTO> list = areaService.selectAreaList(areaDTO);
        return AjaxResult.success(list);
    }

    /**
     * 生成区域编码
     *
     * @return 区域编码
     */
    @RequiresPermissions(value = {"operate:cloud:area:add", "operate:cloud:area:edit"}, logical = Logical.OR)
    @GetMapping("/generate/areaCode")
    public AjaxResult generateAreaCode() {
        return AjaxResult.success("操作成功",areaService.generateAreaCode());
    }

    /**
     * 新增区域表
     */
    @RequiresPermissions("operate:cloud:area:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody AreaDTO areaDTO) {
        return toAjax(areaService.insertArea(areaDTO));
    }

    /**
     * 修改区域表
     */
    @RequiresPermissions("operate:cloud:area:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody AreaDTO areaDTO) {
        return toAjax(areaService.updateArea(areaDTO));
    }

    /**
     * 逻辑批量删除区域表
     */
    @RequiresPermissions("operate:cloud:area:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@Validated(AreaDTO.DeleteAreaDTO.class) @RequestBody AreaDTO areaDTO) {
        return toAjax(areaService.logicDeleteAreaByAreaDTO(areaDTO));
    }

    /**
     * 逻辑批量删除区域表
     */
    @RequiresPermissions("operate:cloud:area:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> areaIds) {
        return toAjax(areaService.logicDeleteAreaByAreaIds(areaIds));
    }
}
