package net.qixiaowei.system.manage.controller.basic;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.excel.CustomVerticalCellStyleStrategy;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.common.utils.excel.SelectSheetWriteHandler;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import net.qixiaowei.system.manage.excel.basic.EmployeeImportListener;
import net.qixiaowei.system.manage.excel.post.PostExcel;
import net.qixiaowei.system.manage.excel.post.PostImportListener;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankSystemService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.service.basic.IPostService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * @author TANGMICHI
 * @since 2022-09-30
 */
@RestController
@RequestMapping("post")
public class PostController extends BaseController {


    @Autowired
    private IPostService postService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IOfficialRankSystemService officialRankSystemService;



    /**
     * 分页查询岗位表列表
     */
    @RequiresPermissions("system:manage:post:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PostDTO postDTO) {
        startPage();
        List<PostDTO> list = postService.selectPostList(postDTO);
        return getDataTable(list);
    }

    /**
     * 生成岗位编码
     *
     * @return 岗位编码
     */
    @RequiresPermissions(value = {"system:manage:post:add", "system:manage:post:edit"}, logical = Logical.OR)
    @GetMapping("/generate/postCode")
    public AjaxResult generatePostCode() {
        return AjaxResult.success("操作成功",postService.generatePostCode());
    }

    /**
     * 新增岗位表
     */
    @Log(title = "新增岗位", businessType = BusinessType.POST, businessId = "postId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:post:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(PostDTO.AddPostDTO.class) PostDTO postDTO) {
        return AjaxResult.success(postService.insertPost(postDTO));
    }

    /**
     * 修改岗位表
     */
    @Log(title = "编辑岗位", businessType = BusinessType.POST, businessId = "postId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:post:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(PostDTO.UpdatePostDTO.class) PostDTO postDTO) {
        return toAjax(postService.updatePost(postDTO));
    }

    /**
     * 查询岗位详情
     */
    @RequiresPermissions(value = {"system:manage:post:info", "system:manage:post:edit"}, logical = Logical.OR)
    @GetMapping("/info/{postId}")
    public AjaxResult list(@PathVariable Long postId) {
        PostDTO postDTO = postService.selectPostByPostId(postId);
        return AjaxResult.success(postDTO);
    }

    /**
     * 逻辑删除岗位表
     */
    @RequiresPermissions("system:manage:post:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(PostDTO.DeletePostDTO.class) PostDTO postDTO) {
        return toAjax(postService.logicDeletePostByPostId(postDTO));
    }

    /**
     * 逻辑批量删除岗位表
     */
    @RequiresPermissions("system:manage:post:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> postIds) {
        return toAjax(postService.logicDeletePostByPostIds(postIds));
    }

    /**
     * 查询岗位表列表
     */
    @GetMapping("/list")
    public AjaxResult list(PostDTO postDTO) {
        List<PostDTO> list = postService.selectPostList(postDTO);
        return AjaxResult.success(list);
    }

    /**
     * 根据部门查询岗位表列表
     */
    @GetMapping("/postList/{departmentId}")
    public AjaxResult selectBydepartmentId(@PathVariable Long departmentId, @RequestParam(required = false) Integer status) {
        List<PostDTO> list = postService.selectBydepartmentId(departmentId,status);
        return AjaxResult.success(list);
    }

    /**
     * 导出模板
     */
    @SneakyThrows
   @RequiresPermissions("system:manage:post:import")
    @GetMapping("/export-template")
    public void exportEmployeeTemplate(HttpServletResponse response) {
        //部门名称集合
        List<String> parentDepartmentExcelNames = departmentService.selectDepartmentListName();
        if (StringUtils.isNull(parentDepartmentExcelNames)){
            throw new ServiceException("请先创建部门数据！");
        }
        //职级体系集合
        List<OfficialRankSystemDTO> officialRankSystemDTOS = officialRankSystemService.selectOfficialRankSystemList(new OfficialRankSystemDTO());
        //职级体系名称
        List<String> officialRankSystemNames = new ArrayList<>();
        if (StringUtils.isNotEmpty(officialRankSystemDTOS)){
            officialRankSystemNames = officialRankSystemDTOS.stream().map(OfficialRankSystemDTO::getOfficialRankSystemName).filter(Objects::nonNull).collect(Collectors.toList());

        }else {
            throw new ServiceException("请先创建职级数据！");
        }
        Map<Integer, List<String>> selectMap = new HashMap<>();
        //自定义表头
        List<List<String>> head = PostImportListener.importHead(selectMap, parentDepartmentExcelNames,officialRankSystemNames);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("岗位信息" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
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
                .sheet("岗位信息")// 设置 sheet 的名字
                .doWrite(new ArrayList<>());
    }

    /**
     * 导入岗位
     */
    @RequiresPermissions("system:manage:post:import")
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }

        List<PostExcel> list = new ArrayList<>();

        //构建读取器
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        ExcelReaderSheetBuilder sheet = read.sheet(0);
        List<Map<Integer, String>> listMap = sheet.doReadSync();


        PostExcel postExcel = new PostExcel();
        ExcelUtils.mapToListModel(1, 0, listMap, postExcel, list);
        // 调用importer方法
        try {
            postService.importPost(list);
        } catch (ParseException e) {
            return AjaxResult.error();
        }

        return AjaxResult.success();
    }


}
