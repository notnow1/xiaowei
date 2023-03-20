package net.qixiaowei.strategy.cloud.remote.strategyDecode;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteAnnualKeyWorkService;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 年度重点工作配置远程实现类
 * @Author: Graves
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/annualKeyWork")
public class RemoteAnnualKeyWork implements RemoteAnnualKeyWorkService {

    @Autowired
    private IAnnualKeyWorkService iAnnualKeyWorkService;

    /**
     * 年度重点工作
     *
     * @param source 根源
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteAnnualKeyWork")
    public R<List<AnnualKeyWorkDTO>> remoteAnnualKeyWork(@RequestBody AnnualKeyWorkDTO annualKeyWorkDTO, String source) {
        return R.ok(iAnnualKeyWorkService.remoteAnnualKeyWork(annualKeyWorkDTO));
    }

    /**
     * 年度重点工作获取部门是否引用
     *
     * @param source 根源
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteAnnualKeyWork")
    public R<List<AnnualKeyWorkDetailDTO>> remoteAnnualKeyWorkDepartment(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO, String source) {
        return R.ok(iAnnualKeyWorkService.remoteAnnualKeyWorkDepartment(annualKeyWorkDetailDTO));
    }
}
