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
     * 查询人员数据
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
}
