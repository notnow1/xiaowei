package net.qixiaowei.operate.cloud.controller.bonus;

import java.util.List;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayStandingDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.EmployeeAnnualBonusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.service.bonus.IBonusPayApplicationService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;

/**
 * @author TANGMICHI
 * @since 2022-12-08
 */
@RestController
@RequestMapping("bonusPayApplication")
public class BonusPayApplicationController extends BaseController {


    @Autowired
    private IBonusPayApplicationService bonusPayApplicationService;


    /**
     * 分页查询奖金发放申请表列表
     */
    @RequiresPermissions("operate:cloud:bonusPayApplication:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(BonusPayApplicationDTO bonusPayApplicationDTO) {
        startPage();
        List<BonusPayApplicationDTO> list = bonusPayApplicationService.selectBonusPayApplicationList(bonusPayApplicationDTO);
        return getDataTable(list);
    }

    /**
     * 生成奖项编码
     *
     * @return 奖项编码
     */
    @RequiresPermissions(value = {"operate:cloud:bonusPayApplication:add", "operate:cloud:bonusPayApplication:edit"}, logical = Logical.OR)
    @GetMapping("/generate/awardCode")
    public AjaxResult generateAwardCode() {
        return AjaxResult.success("操作成功",bonusPayApplicationService.generateAwardCode());
    }

    /**
     * 新增奖金发放申请表
     */
    @Log(title = "新增奖金发放申请", businessType = BusinessType.BONUS_PAY_APPLICATION, businessId = "bonusPayApplicationId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:bonusPayApplication:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody BonusPayApplicationDTO bonusPayApplicationDTO) {
        return AjaxResult.success(bonusPayApplicationService.insertBonusPayApplication(bonusPayApplicationDTO));
    }

    /**
     * 修改奖金发放申请表
     */
    @Log(title = "保存奖金发放申请", businessType = BusinessType.BONUS_PAY_APPLICATION, businessId = "bonusPayApplicationId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:bonusPayApplication:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody BonusPayApplicationDTO bonusPayApplicationDTO) {
        return toAjax(bonusPayApplicationService.updateBonusPayApplication(bonusPayApplicationDTO));
    }

    /**
     * 查询奖金发放申请表详情
     */
    @RequiresPermissions(value = {"operate:cloud:bonusPayApplication:info","operate:cloud:bonusPayApplication:edit"},logical = Logical.OR)
    @GetMapping("/info/{bonusPayApplicationId}")
    public AjaxResult info(@PathVariable Long bonusPayApplicationId) {
        BonusPayApplicationDTO bonusPayApplicationDTO = bonusPayApplicationService.selectBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationId);
        return AjaxResult.success(bonusPayApplicationDTO);
    }

    /**
     * 逻辑删除奖金发放申请表
     */
    @RequiresPermissions("operate:cloud:bonusPayApplication:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody BonusPayApplicationDTO bonusPayApplicationDTO) {
        return toAjax(bonusPayApplicationService.logicDeleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationDTO));
    }

    /**
     * 逻辑批量删除奖金发放申请表
     */
    @RequiresPermissions("operate:cloud:bonusPayApplication:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> bonusPayApplicationIds) {
        return toAjax(bonusPayApplicationService.logicDeleteBonusPayApplicationByBonusPayApplicationIds(bonusPayApplicationIds));
    }


    /**
     * 查询奖金发放申请表列表
     */
    @RequiresPermissions("operate:cloud:bonusPayApplication:pageList")
    @GetMapping("/list")
    public AjaxResult list(BonusPayApplicationDTO bonusPayApplicationDTO) {
        List<BonusPayApplicationDTO> list = bonusPayApplicationService.selectBonusPayApplicationList(bonusPayApplicationDTO);
        return AjaxResult.success(list);
    }


    /**
     * 分页查询奖金发放台账
     */
    @RequiresPermissions("operate:cloud:bonusPayApplication:bonusGrantStandingPage")
    @GetMapping("/bonusGrantStandingPage")
    public TableDataInfo bonusGrantStandingPage(BonusPayApplicationDTO bonusPayApplicationDTO) {
        startPage();
        List<BonusPayStandingDTO> list = bonusPayApplicationService.bonusGrantStandingList(bonusPayApplicationDTO);
        return getDataTable(list);
    }
}
