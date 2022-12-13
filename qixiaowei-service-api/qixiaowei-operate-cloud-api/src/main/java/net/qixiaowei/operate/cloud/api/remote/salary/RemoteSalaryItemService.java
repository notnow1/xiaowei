package net.qixiaowei.operate.cloud.api.remote.salary;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.factory.salary.RemoteSalaryItemFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(contextId = "remoteSalaryItemService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteSalaryItemFallbackFactory.class)
public interface RemoteSalaryItemService {

    String API_PREFIX_SALARY_ITEM = "/salaryItem";

    /**
     * 初始化工资项
     */
    @PostMapping(API_PREFIX_SALARY_ITEM + "/initSalaryItem")
    R<Boolean> initSalaryItem(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}