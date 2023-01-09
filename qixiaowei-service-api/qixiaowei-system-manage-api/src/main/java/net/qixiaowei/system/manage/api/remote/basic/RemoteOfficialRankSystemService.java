package net.qixiaowei.system.manage.api.remote.basic;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.factory.basic.RemoteOfficialRankSystemFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行业服务
 */
@FeignClient(contextId = "remoteOfficialRankSystemService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteOfficialRankSystemFallbackFactory.class)
public interface RemoteOfficialRankSystemService {

    String API_PREFIX_OFFICIAL = "/officialRankSystem";

    /**
     * 查找职级等级列表
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_OFFICIAL + "/selectAll")
    R<List<OfficialRankSystemDTO>> selectAll(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过Id查找职级等级列表
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_OFFICIAL + "/selectById")
    R<OfficialRankSystemDTO> selectById(@RequestParam("officialRankSystemId") Long officialRankSystemId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过Id查找职级上下限
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_OFFICIAL + "/selectRankById")
    R<List<String>> selectRankById(@RequestParam("officialRankSystemId") Long officialRankSystemId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 通过ID集合查找职级等级列表
     *
     * @return 结果
     */
    @PostMapping(API_PREFIX_OFFICIAL + "/selectByIds")
    R<List<OfficialRankSystemDTO>> selectByIds(@RequestBody List<Long> officialRankSystemIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 根据职级体系ID查找分解表
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_OFFICIAL + "/selectDecomposeById")
    R<List<OfficialRankDecomposeDTO>> selectOfficialDecomposeBySystemId(@RequestParam("officialRankSystemId") Long officialRankSystemId,
                                                                        @RequestParam("rankDecomposeDimension") Integer rankDecomposeDimension,
                                                                        @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据职级体系ID和职级分解维度查找分解表
     *
     * @return 结果
     */
    @GetMapping(API_PREFIX_OFFICIAL + "/selectByDimension")
    R<List<OfficialRankDecomposeDTO>> selectOfficialDecomposeByDimension(@RequestParam("decomposeDimension") Long decomposeDimension,
                                                                         @RequestParam("rankDecomposeDimension") Integer rankDecomposeDimension,
                                                                         @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据职级体系ID集合和职级分解维度查找分解表-引用可用
     *
     * @return 结果
     */
    @PostMapping(API_PREFIX_OFFICIAL + "/selectByDimensions")
    R<List<OfficialRankDecomposeDTO>> selectOfficialDecomposeByDimensions(@RequestBody List<Long> decomposeDimensions,
                                                                          @RequestParam("rankDecomposeDimension") Integer rankDecomposeDimension,
                                                                          @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
