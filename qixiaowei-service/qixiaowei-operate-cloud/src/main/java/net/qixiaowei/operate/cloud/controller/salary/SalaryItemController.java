package net.qixiaowei.operate.cloud.controller.salary;

import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryItemExcel;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author Graves
 * @since 2022-10-05
 */
@RestController
@RequestMapping("salaryItem")
public class SalaryItemController extends BaseController {


    @Autowired
    private ISalaryItemService salaryItemService;

    /**
     * 分页查询工资项列表
     */
    @RequiresPermissions("operate:cloud:salaryItem:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(SalaryItemDTO salaryItemDTO) {
        startPage();
        List<SalaryItemDTO> list = salaryItemService.selectSalaryItemPageList(salaryItemDTO);
        return getDataTable(list);
    }

    /**
     * 查询工资项列表
     */
    @GetMapping("/list")
    @RequiresPermissions("operate:cloud:salaryItem:pageList")
    public AjaxResult list(SalaryItemDTO salaryItemDTO) {
        List<SalaryItemDTO> list = salaryItemService.selectSalaryItemList(salaryItemDTO);
        return AjaxResult.success(list);
    }

    /**
     * 新增工资项
     */
    @RequiresPermissions("operate:cloud:salaryItem:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody SalaryItemDTO salaryItemDTO) {
        return toAjax(salaryItemService.insertSalaryItem(salaryItemDTO));
    }

    /**
     * 修改工资项
     */
    @RequiresPermissions("operate:cloud:salaryItem:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody SalaryItemDTO salaryItemDTO) {
        return toAjax(salaryItemService.updateSalaryItem(salaryItemDTO));
    }

    /**
     * 工资项详情
     */
    @RequiresPermissions(value = {"operate:cloud:salaryItem:info", "operate:cloud:salaryItem:edit"}, logical = Logical.OR)
    @GetMapping("/info/{salaryItemId}")
    public AjaxResult info(@PathVariable Long salaryItemId) {
        SalaryItemDTO salaryItemDTO = salaryItemService.detailSalaryItemBySalaryId(salaryItemId);
        return AjaxResult.success(salaryItemDTO);
    }

    /**
     * 逻辑删除工资项
     */
    @RequiresPermissions("operate:cloud:salaryItem:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody SalaryItemDTO salaryItemDTO) {
        Long salaryItemId = salaryItemDTO.getSalaryItemId();
        if (StringUtils.isNull(salaryItemId)) {
            return AjaxResult.error("工资条配置id不能为空！");
        }
        return toAjax(salaryItemService.logicDeleteSalaryItemBySalaryItemId(salaryItemDTO));
    }

    /**
     * 逻辑批量删除工资项
     */
    @RequiresPermissions("operate:cloud:salaryItem:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> salaryItemIds) {
        return toAjax(salaryItemService.logicDeleteSalaryItemBySalaryItemIds(salaryItemIds));
    }

    /**
     * 导出用户
     */
    @SneakyThrows
    @RequiresPermissions("operate:cloud:salaryItem:export")
    @PostMapping("/export")
    public void export(@RequestBody SalaryItemDTO salaryItemDTO, HttpServletResponse response) {
        List<SalaryItemExcel> salaryItemExcels = salaryItemService.exportSalaryExcel(salaryItemDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("工资条配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SalaryItemExcel.class).sheet("工资条配置").doWrite(salaryItemExcels);
    }

    /**
     * 查找二级为奖金的三级工资条
     */
    @GetMapping("/bonusList")
    public AjaxResult bonusList(SalaryItemDTO salaryItemDTO) {
        return AjaxResult.success(salaryItemService.selectBonusItemList(salaryItemDTO));
    }

    /**
     * 查找二级为奖金的三级工资条
     */
    @GetMapping("/applyBonus")
    public AjaxResult applyBonusList() {
        return AjaxResult.success(salaryItemService.applyBonusList());
    }


}
