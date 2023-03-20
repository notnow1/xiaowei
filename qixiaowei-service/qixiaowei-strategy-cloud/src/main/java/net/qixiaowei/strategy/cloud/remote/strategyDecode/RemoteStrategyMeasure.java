package net.qixiaowei.strategy.cloud.remote.strategyDecode;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMeasureService;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @description: 战略举措清单配置远程实现类
 * @Author: Graves
 * @date: 2023/02/20 14:07
 **/
@RestController
@RequestMapping("/strategyMeasure")
public class RemoteStrategyMeasure implements RemoteStrategyMeasureService {

    @Autowired
    private IStrategyMeasureService iStrategyMeasureService;

    /**
     * 战略举措清单
     *
     * @param source 根源
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/remoteStrategyMeasure")
    public R<List<StrategyMeasureDTO>> remoteStrategyMeasure(@RequestBody StrategyMeasureDTO strategyMeasureDTO, String source) {
        return R.ok(iStrategyMeasureService.remoteStrategyMeasure(strategyMeasureDTO));
    }

    /**
     * 查询远程战略举措清单列表责任部门
     *
     * @param strategyMeasureDetailVO 详情vo
     * @param source                  结果
     * @return R
     */
    @Override
    public R<List<StrategyMeasureTaskDTO>> remoteDutyMeasure(StrategyMeasureDetailVO strategyMeasureDetailVO, String source) {
        return R.ok(iStrategyMeasureService.remoteDutyMeasure(strategyMeasureDetailVO));
    }
}
