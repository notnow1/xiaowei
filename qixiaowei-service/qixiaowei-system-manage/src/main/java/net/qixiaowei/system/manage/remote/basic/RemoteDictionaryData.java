package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDictionaryDataService;
import net.qixiaowei.system.manage.service.basic.IDictionaryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dictionaryData/remote")
public class RemoteDictionaryData implements RemoteDictionaryDataService {
    @Autowired
    private IDictionaryDataService dictionaryDataService;

    /**
     * 根据字典id查询字典数据表详情
     * @param dictionaryDataId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/{dictionaryDataId}")
    public R<DictionaryDataDTO> info(Long dictionaryDataId, String source) {
        return R.ok(dictionaryDataService.selectDictionaryDataByDictionaryDataId(dictionaryDataId));
    }

    /**
     * 根据字典id集合查询字典数据表详情
     * @param dictionaryDataIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectDictionaryDataIds")
    public R<List<DictionaryDataDTO>> selectDictionaryDataByDictionaryDataIds(@RequestBody List<Long> dictionaryDataIds, String source) {
        return R.ok(dictionaryDataService.selectDictionaryDataByDictionaryDataIds(dictionaryDataIds));
    }

}