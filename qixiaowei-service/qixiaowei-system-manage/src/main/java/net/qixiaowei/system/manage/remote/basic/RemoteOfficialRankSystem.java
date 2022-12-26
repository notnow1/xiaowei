package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankDecomposeService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officialRankSystem")
public class RemoteOfficialRankSystem implements RemoteOfficialRankSystemService {

    @Autowired
    IOfficialRankSystemService officialRankSystemService;

    @Autowired
    IOfficialRankDecomposeService officialRankDecomposeService;

    @Override
    @InnerAuth
    @GetMapping("/selectAll")
    public R<List<OfficialRankSystemDTO>> selectAll(String source) {
        return R.ok(officialRankSystemService.selectOfficialRankSystemList(new OfficialRankSystemDTO()));
    }

    /**
     * 通过Id查找职级等级列表
     *
     * @param officialRankSystemId
     * @param source
     * @return 】
     */
    @Override
    @InnerAuth
    @GetMapping("/selectById")
    public R<OfficialRankSystemDTO> selectById(@RequestParam("officialRankSystemId") Long officialRankSystemId, String source) {
        return R.ok(officialRankSystemService.selectOfficialRankSystemByOfficialRankSystemId(officialRankSystemId));
    }

    /**
     * 通过Id查找职级上下限
     *
     * @param officialRankSystemId 职级体系ID
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/selectRankById")
    public R<List<String>> selectRankById(@RequestParam("officialRankSystemId") Long officialRankSystemId, String source) {
        return R.ok(officialRankSystemService.selectOfficialRankByOfficialRankSystemId(officialRankSystemId));
    }

    /**
     * 通过ID集合查找职级等级列表
     *
     * @param officialRankSystemIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByIds")
    public R<List<OfficialRankSystemDTO>> selectByIds(@RequestBody List<Long> officialRankSystemIds, String source) {
        return R.ok(officialRankSystemService.selectOfficialRankSystemByOfficialRankSystemIds(officialRankSystemIds));
    }

    /**
     * 根据职级体系ID查找分解表
     *
     * @param officialRankSystemId   职级体系ID
     * @param rankDecomposeDimension 职级分类
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/selectDecomposeById")
    public R<List<OfficialRankDecomposeDTO>> selectOfficialDecomposeBySystemId(@RequestParam("officialRankSystemId") Long officialRankSystemId,
                                                                               @RequestParam("rankDecomposeDimension") Integer rankDecomposeDimension,
                                                                               String source) {
        return R.ok(officialRankDecomposeService.selectOfficialRankDecomposeAndNameByOfficialRankSystemId(officialRankSystemId, rankDecomposeDimension));
    }

    /**
     * 根据职级体系ID和职级分解维度查找分解表
     *
     * @param decomposeDimension     具体分解ID
     * @param rankDecomposeDimension 分解类型
     * @param source                 根源
     * @return R
     */
    @Override
    @InnerAuth
    @GetMapping("/selectDecomposeById")
    public R<List<OfficialRankDecomposeDTO>> selectOfficialDecomposeByDimension(@RequestParam("decomposeDimension") Long decomposeDimension,
                                                                                @RequestParam("rankDecomposeDimension") Integer rankDecomposeDimension,
                                                                                String source) {
        return R.ok(officialRankDecomposeService.selectOfficialDecomposeByDimension(decomposeDimension, rankDecomposeDimension));
    }

    /**
     * 根据职级体系ID集合和职级分解维度查找分解表
     *
     * @param decomposeDimensions    具体分解ID集合
     * @param rankDecomposeDimension 分解类型
     * @param source                 根源
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByDimensions")
    public R<List<OfficialRankDecomposeDTO>> selectOfficialDecomposeByDimensions(@RequestBody List<Long> decomposeDimensions,
                                                                                 @RequestParam("rankDecomposeDimension") Integer rankDecomposeDimension,
                                                                                 String source) {
        return R.ok(officialRankDecomposeService.selectOfficialDecomposeByDimensions(decomposeDimensions, rankDecomposeDimension));
    }

}
