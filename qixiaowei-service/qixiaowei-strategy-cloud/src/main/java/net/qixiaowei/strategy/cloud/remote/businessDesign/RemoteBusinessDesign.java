package net.qixiaowei.strategy.cloud.remote.businessDesign;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.remote.businessDesign.RemoteBusinessDesignService;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignParamService;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 业务设计配置远程实现类
 * @Author: Graves
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/businessDesign")
public class RemoteBusinessDesign implements RemoteBusinessDesignService {

    @Autowired
    private IBusinessDesignService iBusinessDesignService;

    @Autowired
    private IBusinessDesignParamService businessDesignParamService;

    /**
     * 业务设计
     *
     * @param source 根源
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteBusinessDesign")
    public R<List<BusinessDesignDTO>> remoteBusinessDesign(@RequestBody BusinessDesignDTO businessDesignDTO, String source) {
        return R.ok(iBusinessDesignService.remoteBusinessDesign(businessDesignDTO));
    }

    /**
     * 战略衡量指标详情列表
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteBusinessDesignParams")
    public R<List<BusinessDesignParamDTO>> remoteBusinessDesignParams(@RequestBody BusinessDesignParamDTO businessDesignParamDTO, String source) {
        return R.ok(businessDesignParamService.selectBusinessDesignParamList(businessDesignParamDTO));
    }
}
