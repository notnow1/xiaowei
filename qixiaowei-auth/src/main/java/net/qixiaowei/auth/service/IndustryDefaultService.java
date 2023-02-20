package net.qixiaowei.auth.service;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.message.api.remote.message.RemoteMessageService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryDefaultService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认行业
 */
@Component
public class IndustryDefaultService {
    @Autowired
    private RemoteIndustryDefaultService remoteIndustryDefaultService;


    public List<Tree<Long>> getTreeList() {
        R<List<Tree<Long>>> treeList = remoteIndustryDefaultService.getTreeList(SecurityConstants.INNER);
        if (R.SUCCESS != treeList.getCode()) {
            throw new ServiceException("系统异常，请联系管理员。");
        }
        return treeList.getData();
    }

}
