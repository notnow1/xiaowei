package net.qixiaowei.operate.cloud.controller.bonus;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.service.bonus.IBonusPayApplicationService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;



/**
*
* @author TANGMICHI
* @since 2022-12-08
*/
@RestController
@RequestMapping("bonusPayApplication")
public class BonusPayApplicationController extends BaseController
{


    @Autowired
    private IBonusPayApplicationService bonusPayApplicationService;



    /**
    * 查询奖金发放申请表详情
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:info")
    @GetMapping("/info/{bonusPayApplicationId}")
    public AjaxResult info(@PathVariable Long bonusPayApplicationId){
    BonusPayApplicationDTO bonusPayApplicationDTO = bonusPayApplicationService.selectBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationId);
        return AjaxResult.success(bonusPayApplicationDTO);
    }

    /**
    * 分页查询奖金发放申请表列表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(BonusPayApplicationDTO bonusPayApplicationDTO){
    startPage();
    List<BonusPayApplicationDTO> list = bonusPayApplicationService.selectBonusPayApplicationList(bonusPayApplicationDTO);
    return getDataTable(list);
    }

    /**
    * 查询奖金发放申请表列表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:list")
    @GetMapping("/list")
    public AjaxResult list(BonusPayApplicationDTO bonusPayApplicationDTO){
    List<BonusPayApplicationDTO> list = bonusPayApplicationService.selectBonusPayApplicationList(bonusPayApplicationDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增奖金发放申请表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:add")
    @Log(title = "新增奖金发放申请表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody BonusPayApplicationDTO bonusPayApplicationDTO) {
    return AjaxResult.success(bonusPayApplicationService.insertBonusPayApplication(bonusPayApplicationDTO));
    }


    /**
    * 修改奖金发放申请表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:edit")
    @Log(title = "修改奖金发放申请表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody BonusPayApplicationDTO bonusPayApplicationDTO)
    {
    return toAjax(bonusPayApplicationService.updateBonusPayApplication(bonusPayApplicationDTO));
    }

    /**
    * 逻辑删除奖金发放申请表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:remove")
    @Log(title = "删除奖金发放申请表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody BonusPayApplicationDTO bonusPayApplicationDTO)
    {
    return toAjax(bonusPayApplicationService.logicDeleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationDTO));
    }
    /**
    * 批量修改奖金发放申请表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:edits")
    @Log(title = "批量修改奖金发放申请表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<BonusPayApplicationDTO> bonusPayApplicationDtos)
    {
    return toAjax(bonusPayApplicationService.updateBonusPayApplications(bonusPayApplicationDtos));
    }

    /**
    * 批量新增奖金发放申请表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:insertBonusPayApplications")
    @Log(title = "批量新增奖金发放申请表", businessType = BusinessType.INSERT)
    @PostMapping("/insertBonusPayApplications")
    public AjaxResult insertBonusPayApplications(@RequestBody List<BonusPayApplicationDTO> bonusPayApplicationDtos)
    {
    return toAjax(bonusPayApplicationService.insertBonusPayApplications(bonusPayApplicationDtos));
    }

    /**
    * 逻辑批量删除奖金发放申请表
    */
    @RequiresPermissions("operate:cloud:bonusPayApplication:removes")
    @Log(title = "批量删除奖金发放申请表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  bonusPayApplicationIds)
    {
    return toAjax(bonusPayApplicationService.logicDeleteBonusPayApplicationByBonusPayApplicationIds(bonusPayApplicationIds));
    }
}
