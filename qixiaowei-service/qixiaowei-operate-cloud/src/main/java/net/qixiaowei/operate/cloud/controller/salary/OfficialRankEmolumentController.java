package net.qixiaowei.operate.cloud.controller.salary;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-11-30
 */
@RestController
@RequestMapping("officialRankEmolument")
public class OfficialRankEmolumentController extends BaseController {


    @Autowired
    private IOfficialRankEmolumentService officialRankEmolumentService;


    /**
     * 查询职级薪酬表详情
     */
    @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:edit"}, logical = Logical.OR)
    @GetMapping("/info")
    public AjaxResult info(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return AjaxResult.success(officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO));
    }

    /**
     * 查看该职级的分解信息
     */
    @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:edit"}, logical = Logical.OR)
    @GetMapping("/decomposeInfo")
    public AjaxResult list(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return AjaxResult.success(officialRankEmolumentService.selectOfficialDecomposeList(officialRankEmolumentDTO));
    }

    /**
     * 修改职级薪酬表
     */
    @Log(title = "保存", businessType = BusinessType.OFFICIAL_RANK_EMOLUMENT, businessId = "officialRankSystemId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:officialRankEmolument:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return toAjax(officialRankEmolumentService.updateOfficialRankEmolument(officialRankEmolumentDTO));
    }

    /**
     * 根据岗位ID获取职级确定薪酬提示
     */
    @RequiresPermissions(value = {"operate:cloud:officialRankEmolument:info", "operate:cloud:officialRankEmolument:edit"}, logical = Logical.OR)
    @GetMapping("/selectByPostId/{postId}")
    public AjaxResult selectByPostId(@PathVariable Long postId) {
        return AjaxResult.success(officialRankEmolumentService.selectByPostId(postId));
    }

}
