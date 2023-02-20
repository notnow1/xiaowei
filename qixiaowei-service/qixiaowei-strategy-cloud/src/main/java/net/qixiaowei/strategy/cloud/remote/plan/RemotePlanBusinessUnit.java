package net.qixiaowei.strategy.cloud.remote.plan;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.remote.plan.RemotePlanBusinessUnitService;
import net.qixiaowei.strategy.cloud.service.plan.IPlanBusinessUnitService;
import org.bouncycastle.LICENSE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业务规划单元远程接口
 *
 * @author Graves
 * @since 2023-02-20
 */
@RestController
@RequestMapping("/planBusinessUnit")
public class RemotePlanBusinessUnit implements RemotePlanBusinessUnitService {

    @Autowired
    IPlanBusinessUnitService planBusinessUnitService;

    /**
     * 规划业务单元列表
     *
     * @param planBusinessUnitDTO 规划业务单元DTO
     * @param source              请求方式
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/remotePlanBusinessUnit")
    public R<List<PlanBusinessUnitDTO>> remotePlanBusinessUnit(@RequestBody PlanBusinessUnitDTO planBusinessUnitDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) {
        return R.ok(planBusinessUnitService.selectPlanBusinessUnitList(planBusinessUnitDTO));
    }

}
