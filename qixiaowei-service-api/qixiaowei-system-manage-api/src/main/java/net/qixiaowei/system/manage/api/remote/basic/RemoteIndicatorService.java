package net.qixiaowei.system.manage.api.remote.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteIndicatorFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域服务
 */
@FeignClient(contextId = "remoteIndicatorService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteIndicatorFallbackFactory.class)
public interface RemoteIndicatorService {

    String API_PREFIX_INDICATOR = "/indicator";

    /**
     * 通过Code查找指标列表
     *
     * @param indicatorCode 区域ids
     * @return 结果
     */
    @GetMapping(API_PREFIX_INDICATOR + "/listByCode")
    R<IndicatorDTO> selectIndicatorByCode(@RequestParam("indicatorCode") String indicatorCode, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查找指标列表-树结构
     *
     * @param indicatorDTO 指标DTO
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDICATOR + "/remoteTreeList")
    R<List<Tree<Long>>> selectIndicatorTreeList(@RequestBody IndicatorDTO indicatorDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过CodeList查找指标列表
     *
     * @param indicatorCodes 区域ids
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDICATOR + "/listByCodes")
    R<List<IndicatorDTO>> selectIndicatorByCodeList(@RequestBody List<String> indicatorCodes, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查找指标列表结构
     *
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDICATOR + "/remoteList")
    R<List<IndicatorDTO>> selectIndicatorList(@RequestBody IndicatorDTO indicatorDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过IdsList查找指标列表
     *
     * @param indicatorIds 指标ids
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDICATOR + "/listByIds")
    R<List<IndicatorDTO>> selectIndicatorByIds(@RequestBody List<Long> indicatorIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过Id查找指标
     *
     * @param indicatorId 指标id
     * @return 结果
     */
    @GetMapping(API_PREFIX_INDICATOR + "/byId")
    R<IndicatorDTO> selectIndicatorById(@RequestParam Long indicatorId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * 通过指标名称List查找指标列表
     *
     * @param indicatorNames 指标名称
     * @return 结果
     */
    @PostMapping(API_PREFIX_INDICATOR + "/listByNames")
    R<List<IndicatorDTO>> selectIndicatorByNames(@RequestBody List<String>indicatorNames, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
