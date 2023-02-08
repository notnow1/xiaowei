package net.qixiaowei.system.manage.controller.field;

import java.util.List;

import net.qixiaowei.system.manage.api.vo.field.FieldListConfigVO;
import net.qixiaowei.system.manage.api.vo.field.FieldListHeaderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.field.FieldListConfigDTO;
import net.qixiaowei.system.manage.service.field.IFieldListConfigService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2023-02-08
 */
@RestController
@RequestMapping("fieldListConfig")
public class FieldListConfigController extends BaseController {


    @Autowired
    private IFieldListConfigService fieldListConfigService;


    /**
     * 查询表头字段列表配置表列表
     */
    @GetMapping("/header/list")
    public AjaxResult headerList(@RequestParam("businessType") Integer businessType) {
        List<FieldListHeaderVO> list = fieldListConfigService.selectHeaderFieldListConfigList(businessType);
        return AjaxResult.success(list);
    }

    /**
     * 查询字段列表配置表列表
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam("businessType") Integer businessType) {
        List<FieldListConfigVO> list = fieldListConfigService.selectFieldListConfigList(businessType);
        return AjaxResult.success(list);
    }

    /**
     * 修改字段列表配置表
     */
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(FieldListConfigDTO.UpdateFieldListConfigDTO.class) @RequestBody FieldListConfigDTO fieldListConfigDTO) {
        return toAjax(fieldListConfigService.updateFieldListConfig(fieldListConfigDTO));
    }

    /**
     * 批量修改字段列表配置表
     */
    @PostMapping("/edits")
    public AjaxResult editSaves(@Validated(FieldListConfigDTO.UpdateFieldListConfigDTO.class) @RequestBody List<FieldListConfigDTO> fieldListConfigDtos) {
        return toAjax(fieldListConfigService.updateFieldListConfigs(fieldListConfigDtos));
    }


}
