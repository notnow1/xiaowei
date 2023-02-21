package net.qixiaowei.system.manage.controller.basic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.integration.common.utils.excel.CustomVerticalCellStyleStrategy;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import net.qixiaowei.system.manage.excel.basic.EmployeeImportListener;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import net.qixiaowei.system.manage.service.basic.IPostService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2022-09-30
 */
@RestController
@RequestMapping("employee")
public class EmployeeController extends BaseController {


    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IPostService postService;


    /**
     * 分页查询员工表列表
     */
    @RequiresPermissions("system:manage:employee:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeDTO employeeDTO) {
        startPage();
        List<EmployeeDTO> list = employeeService.selectEmployeeList(employeeDTO);
        return getDataTable(list);
    }

    /**
     * 生成员工编码
     *
     * @return 员工编码
     */
    @RequiresPermissions(value = {"system:manage:employee:add", "system:manage:employee:edit"}, logical = Logical.OR)
    @GetMapping("/generate/employeeCode")
    public AjaxResult generateEmployeeCode() {
        return AjaxResult.success("操作成功",employeeService.generateEmployeeCode());
    }

    /**
     * 新增员工表
     */
    @Log(title = "新增员工", businessType = BusinessType.EMPLOYEE, businessId = "employeeId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:employee:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmployeeDTO.AddEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.insertEmployee(employeeDTO));
    }

    /**
     * 修改员工表
     */
    @Log(title = "编辑员工", businessType = BusinessType.EMPLOYEE, businessId = "employeeId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:employee:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(EmployeeDTO.UpdateEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.updateEmployee(employeeDTO));
    }

    /**
     * 查询员工单条信息
     */
    @RequiresPermissions(value = {"system:manage:employee:info", "system:manage:employee:edit"}, logical = Logical.OR)
    @GetMapping("/info/{employeeId}")
    public AjaxResult info(@PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.selectEmployeeByEmployeeId(employeeId);
        return AjaxResult.success(employeeDTO);
    }

    /**
     * 逻辑删除员工表
     */
    @RequiresPermissions("system:manage:employee:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(EmployeeDTO.DeleteEmployeeDTO.class) EmployeeDTO employeeDTO) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeId(employeeDTO));
    }

    /**
     * 逻辑批量删除员工表
     */
    @RequiresPermissions("system:manage:employee:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> employeeIds) {
        return toAjax(employeeService.logicDeleteEmployeeByEmployeeIds(employeeIds));
    }

    /**
     * 导入人员
     */
    @RequiresPermissions("system:manage:employee:import")
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }

        List<EmployeeExcel> list = new ArrayList<>();

        //构建读取器
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        ExcelReaderSheetBuilder sheet = read.sheet(0);
        List<Map<Integer, String>> listMap = sheet.doReadSync();


        EmployeeExcel employeeExcel = new EmployeeExcel();
        ExcelUtils.mapToListModel(2, 0, listMap, employeeExcel, list);
        // 调用importer方法
        try {
            employeeService.importEmployee(list);
        } catch (ParseException e) {
            return AjaxResult.error();
        }

        return AjaxResult.success();
    }

    /**
     * 导出人员
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:export")
    @GetMapping("export")
    public void exportEmployee(@RequestParam Map<String, Object> employee, EmployeeDTO employeeDTO, HttpServletResponse response) {
        //部门名称集合
        List<String> parentDepartmentExcelNames = departmentService.selectDepartmentListName();
        //岗位名称
        List<String> postNames = new ArrayList<>();
        PostDTO postDTO = new PostDTO();
        //查询岗位信息
        List<PostDTO> postDTOS = postService.selectPostList(postDTO);
        if (StringUtils.isNotEmpty(postDTOS)) {
            postNames = postDTOS.stream().map(PostDTO::getPostName).collect(Collectors.toList());
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = EmployeeImportListener.exportHead(selectMap, parentDepartmentExcelNames, postNames);
        List<EmployeeExcel> employeeExcelList = employeeService.exportEmployee(employeeDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人员信息配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        CustomVerticalCellStyleStrategy levelStrategy = new CustomVerticalCellStyleStrategy(head);

        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
                .head(head)
                .registerWriteHandler(levelStrategy)
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(17))
                //设置文本
                .registerWriteHandler(new CellWriteHandler() {
                    @Override
                    public void afterCellDispose(CellWriteHandlerContext context) {
                        // 3.0 设置单元格为文本
                        WriteCellData<?> cellData = context.getFirstCellData();
                        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();
                        DataFormatData dataFormatData = new DataFormatData();
                        dataFormatData.setIndex((short) 49);
                        writeCellStyle.setDataFormatData(dataFormatData);
                    }
                })
                .sheet("人员信息配置")// 设置 sheet 的名字
                .doWrite(EmployeeImportListener.dataList(employeeExcelList));
    }

    /**
     * 导出模板
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:employee:import")
    @GetMapping("/export-template")
    public void exportEmployeeTemplate(HttpServletResponse response) {
        //部门名称集合
        List<String> parentDepartmentExcelNames = departmentService.selectDepartmentListName();
        //岗位名称
        List<String> postNames = new ArrayList<>();
        PostDTO postDTO = new PostDTO();
        //查询岗位信息
        List<PostDTO> postDTOS = postService.selectPostList(postDTO);
        if (StringUtils.isNotEmpty(postDTOS)) {
            postNames = postDTOS.stream().map(PostDTO::getPostName).collect(Collectors.toList());
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = EmployeeImportListener.importHead(selectMap, parentDepartmentExcelNames, postNames);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人员信息配置" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        CustomVerticalCellStyleStrategy levelStrategy = new CustomVerticalCellStyleStrategy(head);

        EasyExcel.write(response.getOutputStream())
                .excelType(ExcelTypeEnum.XLSX)
                .registerWriteHandler(new SelectSheetWriteHandler(selectMap))
                .head(head)
                .registerWriteHandler(levelStrategy)
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(17))
                // 重写AbstractColumnWidthStyleStrategy策略的setColumnWidth方法
                .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                    @Override
                    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
                        if (integer == 0){
                            Row row = cell.getRow();
                            row.setHeightInPoints(144);
                        }
                    }
                })
                .sheet("人员信息配置")// 设置 sheet 的名字
                .doWrite(new ArrayList<>());
    }


    //==============================岗位薪酬报表==================================//

    /**
     * 分页查询岗位薪酬报表
     */
    @RequiresPermissions("system:manage:employee:pagePostSalaryReportList")
    @GetMapping("/pagePostSalaryReportList")
    public TableDataInfo pagePostSalaryReportList(EmployeeDTO employeeDTO) {
        startPage();
        List<EmployeeDTO> list = employeeService.pagePostSalaryReportList(employeeDTO);
        return getDataTable(list);
    }

    //==============================其他==================================//

    /**
     * 查询员工表列表(下拉框)
     */
    @GetMapping("/list")
    public AjaxResult list(EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.selectDropEmployeeList(employeeDTO));
    }

    /**
     * 根据部门id查询员工表列表
     */
    @GetMapping("/queryEmployeeByDept/{employeeDepartmentId}")
    public AjaxResult queryEmployeeByDept(@PathVariable Long employeeDepartmentId) {
        List<EmployeeDTO> list = employeeService.queryEmployeeByDept(employeeDepartmentId);
        return AjaxResult.success(list);
    }

    /**
     * 新增人力预算上年期末数集合预制数据
     */
    @PostMapping("/amountLastYear")
    public AjaxResult selecTamountLastYearList(@RequestBody EmployeeDTO employeeDTO) {
        return AjaxResult.success(employeeService.selecTamountLastYearList(employeeDTO));
    }

    /**
     * 根据部门id查询员工表列表
     */
    @GetMapping("/empSalaryAdjustPlanById/{employeeId}")
    public AjaxResult empSalaryAdjustPlanById(@PathVariable Long employeeId) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employeeId);
        return AjaxResult.success(employeeService.empSalaryAdjustPlan(employeeDTO));
    }

    /**
     * 查询有账号的员工
     */
    @RequiresPermissions("system:manage:employee:getUseEmployeeUser")
    @GetMapping("/getUseEmployeeUser")
    public AjaxResult getUseEmployeeUser() {
        return AjaxResult.success(employeeService.getUseEmployeeUser());
    }

}
