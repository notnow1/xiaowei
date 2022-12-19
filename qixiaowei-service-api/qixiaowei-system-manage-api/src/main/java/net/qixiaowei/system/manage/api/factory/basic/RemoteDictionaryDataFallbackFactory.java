package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDictionaryDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典服务降级处理
 */
@Component
public class RemoteDictionaryDataFallbackFactory implements FallbackFactory<RemoteDictionaryDataService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteDictionaryDataFallbackFactory.class);

    @Override
    public RemoteDictionaryDataService create(Throwable throwable) {
        log.error("字典服务调用失败:{}", throwable.getMessage());
        return new RemoteDictionaryDataService() {


            @Override
            public R<DictionaryDataDTO> info(Long dictionaryDataId, String source) {
                return R.fail("根据字典id获取字典信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<DictionaryDataDTO>> selectDictionaryDataByDictionaryDataIds(List<Long> dictionaryDataIds, String source) {
                return R.fail("根据字典id集合获取字典信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<DictionaryDataDTO>> selectDictionaryTypeByProduct(String source) {
                return R.fail("根据枚举查询产品应用字典名称数据失败:" + throwable.getMessage());
            }
        };
    }
}
