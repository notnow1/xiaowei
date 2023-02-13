package net.qixiaowei.operate.cloud.remote.targetManager;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author TMICHI
 * @Date 2022-11-17
 **/
@RestController
@RequestMapping("/targetDecompose/remote")
public class RemoteDecompose implements RemoteDecomposeService {

    @Autowired
    private ITargetDecomposeService targetDecomposeService;


    /**
     * 根据目标分解id查询目标分解数据
     *
     * @param targetDecomposeId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/targetDecomposeId")
    public R<TargetDecomposeDTO> info(Long targetDecomposeId, String source) {
        return R.ok(targetDecomposeService.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId));
    }

    /**
     * 根据目标分解id查询目标分解数据
     *
     * @param targetDecomposeIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/decomposeDetails/targetDecomposeIds")
    public R<List<TargetDecomposeDTO>> selectBytargetDecomposeIds(@RequestBody List<Long> targetDecomposeIds, String source) {
        return R.ok(targetDecomposeService.selectTargetDecomposeByTargetDecomposeIds(targetDecomposeIds));
    }

    /**
     * 根据目标分解id查询目标分解详情数据
     *
     * @param targetDecomposeId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/decomposeDetails/targetDecomposeId")
    public R<List<TargetDecomposeDetailsDTO>> selectDecomposeDetailsBytargetDecomposeId(Long targetDecomposeId, String source) {
        return R.ok(targetDecomposeService.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId));
    }

    /**
     * 传入实体类根据条件查询
     *
     * @param targetDecomposeDetailsDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/decomposeDetails/getDecomposeDetails")
    public R<List<TargetDecomposeDetailsDTO>> getDecomposeDetails(@RequestBody TargetDecomposeDetailsDTO targetDecomposeDetailsDTO, String source) {
        return R.ok(targetDecomposeService.getDecomposeDetails(targetDecomposeDetailsDTO));
    }

    /**
     * 目标分解是否被引用
     *
     * @param departmentId
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/decomposeDetails/queryDeptDecompose")
    public R<List<TargetDecompose>> queryDeptDecompose(Long departmentId) {
        return R.ok(targetDecomposeService.queryDeptDecompose(departmentId));
    }

    /**
     * 根据区域ID集合查询目标分解数据
     * 1.员工ID
     * 2.区域ID
     * 3.部门ID
     * 4.产品ID
     * 5.省份ID
     * 6.行业ID
     * 7.负责人ID
     *
     * @param map    ID集合
     * @param source 根源
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/decomposeDetails/selectByIds")
    public R<List<TargetDecomposeDetailsDTO>> selectByIds(@RequestBody Map<Integer, List<Long>> map, String source) {
        return R.ok(targetDecomposeService.selectByIds(map));
    }

    /**
     * 根据指标ID查询目标分解
     *
     * @param indicatorIds 指标ID集合
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/decomposeDetails/selectByIndicatorIds")
    public R<List<TargetDecomposeDTO>> selectByIndicatorIds(@RequestBody List<Long> indicatorIds, String source) {
        return R.ok(targetDecomposeService.selectByIndicatorIds(indicatorIds));
    }

}
