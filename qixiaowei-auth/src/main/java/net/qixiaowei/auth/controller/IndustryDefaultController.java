package net.qixiaowei.auth.controller;

import net.qixiaowei.auth.service.IndustryDefaultService;
import net.qixiaowei.integration.common.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 默认行业 控制
 */
@RestController
public class IndustryDefaultController {

    @Autowired
    private IndustryDefaultService industryDefaultService;


    /**
     * 查询默认行业列表
     */
    @GetMapping("/industryDefault/getTreeList")
    public R<?> getTreeList() {
        return R.ok(industryDefaultService.getTreeList());
    }


}
