package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.service.basic.IIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/industry")
public class RemoteIndustry implements RemoteIndustryService {
    @Autowired
    private IIndustryService industryService;

    /**
     * 根据code集合查询行业数据
     *
     * @param industryCodes
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/codeList")
    public R<List<IndustryDTO>> selectCodeList(@RequestBody List<String> industryCodes, String source) {
        return R.ok(industryService.selectCodeList(industryCodes));
    }

    /**
     * 通过Id查找行业列表
     *
     * @param industryId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/selectById")
    public R<IndustryDTO> selectById(@RequestParam("industryId") Long industryId, String source) {
        return R.ok(industryService.selectIndustryByIndustryId(industryId));
    }

    /**
     * 通过ID集合查找行业列表
     *
     * @param industryIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectByIds")
    public R<List<IndustryDTO>> selectByIds(@RequestBody List<Long> industryIds, String source) {
        return R.ok(industryService.selectIndustryByIndustryIds(industryIds));
    }

    /**
     * 查找行业列表
     *
     * @return 结果
     */
    @Override
    @InnerAuth
    @PostMapping("/selectList")
    public R<List<IndustryDTO>> selectListByIndustry(@RequestBody IndustryDTO industryDTO, String inner) {
        return R.ok(industryService.selectIndustryList(industryDTO));
    }

}
