package net.qixiaowei.strategy.cloud.remote.industry;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.remote.industry.RemoteIndustryAttractionService;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description: 行业吸引力配置远程实现类
 * @Author: TANGMICHI
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/industryAttraction")
public class RemoteIndustryAttraction implements RemoteIndustryAttractionService {

    @Autowired
    private IIndustryAttractionService iIndustryAttractionService;


    /**
     * 初始化行业吸引力配置
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/initIndustryAttraction")
    public R<Boolean> initIndustryAttraction(String source) {
        return R.ok(iIndustryAttractionService.initIndustryAttraction());
    }
}
