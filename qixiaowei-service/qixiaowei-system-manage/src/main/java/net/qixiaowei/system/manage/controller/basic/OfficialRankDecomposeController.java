package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.service.basic.IOfficialRankDecomposeService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-10-07
 */
@RestController
@RequestMapping("officialRankDecompose")
public class OfficialRankDecomposeController extends BaseController {


    @Autowired
    private IOfficialRankDecomposeService officialRankDecomposeService;

    /**
     * 查询职级分解表详情
     */
//    @RequiresPermissions("system:manage:officialRankDecompose:pageList")
    @Log(title = "查询职级分解表详情")
    @GetMapping("/info/{officialRankSystemId}")
    public AjaxResult info(@PathVariable Long officialRankSystemId) {
        return AjaxResult.success(officialRankDecomposeService.selectOfficialRankDecomposeByOfficialRankSystemDTO(officialRankSystemId));
    }

}
