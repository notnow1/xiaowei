package net.qixiaowei.system.manage.remote.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryDefaultService;
import net.qixiaowei.system.manage.service.basic.IIndustryDefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/industryDefault")
public class RemoteIndustryDefault implements RemoteIndustryDefaultService {

    @Autowired
    private IIndustryDefaultService iIndustryDefaultService;


    @Override
    @InnerAuth
    @GetMapping("/getTreeList")
    public R<List<Tree<Long>>> getTreeList(String source) {
        IndustryDefaultDTO industryDefaultDTO = new IndustryDefaultDTO();
        industryDefaultDTO.setStatus(BusinessConstants.NORMAL);
        return R.ok(iIndustryDefaultService.selectIndustryDefaultTreeList(industryDefaultDTO));
    }


}
