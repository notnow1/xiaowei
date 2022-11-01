package net.qixiaowei.operate.cloud.service.targetManager;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeHistoryDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeHistoryExcel;


/**
* TargetDecomposeHistoryService接口
* @author TANGMICHI
* @since 2022-10-31
*/
public interface ITargetDecomposeHistoryService{
    /**
    * 查询目标分解历史版本表
    *
    * @param targetDecomposeHistoryId 目标分解历史版本表主键
    * @return 目标分解历史版本表
    */
    TargetDecomposeHistoryDTO selectTargetDecomposeHistoryByTargetDecomposeHistoryId(Long targetDecomposeHistoryId);

    /**
    * 查询目标分解历史版本表列表
    *
    * @param targetDecomposeHistoryDTO 目标分解历史版本表
    * @return 目标分解历史版本表集合
    */
    List<TargetDecomposeHistoryDTO> selectTargetDecomposeHistoryList(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO);

    /**
    * 新增目标分解历史版本表
    *
    * @param targetDecomposeHistoryDTO 目标分解历史版本表
    * @return 结果
    */
    TargetDecomposeHistoryDTO insertTargetDecomposeHistory(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO);

    /**
    * 修改目标分解历史版本表
    *
    * @param targetDecomposeHistoryDTO 目标分解历史版本表
    * @return 结果
    */
    int updateTargetDecomposeHistory(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO);

    /**
    * 批量修改目标分解历史版本表
    *
    * @param targetDecomposeHistoryDtos 目标分解历史版本表
    * @return 结果
    */
    int updateTargetDecomposeHistorys(List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDtos);

    /**
    * 批量新增目标分解历史版本表
    *
    * @param targetDecomposeHistoryDtos 目标分解历史版本表
    * @return 结果
    */
    int insertTargetDecomposeHistorys(List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDtos);

    /**
    * 逻辑批量删除目标分解历史版本表
    *
    * @param targetDecomposeHistoryIds 需要删除的目标分解历史版本表集合
    * @return 结果
    */
    int logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(List<Long> targetDecomposeHistoryIds);

    /**
    * 逻辑删除目标分解历史版本表信息
    *
    * @param targetDecomposeHistoryDTO
    * @return 结果
    */
    int logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryId(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO);
    /**
    * 批量删除目标分解历史版本表
    *
    * @param TargetDecomposeHistoryDtos
    * @return 结果
    */
    int deleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(List<TargetDecomposeHistoryDTO> TargetDecomposeHistoryDtos);

    /**
    * 逻辑删除目标分解历史版本表信息
    *
    * @param targetDecomposeHistoryDTO
    * @return 结果
    */
    int deleteTargetDecomposeHistoryByTargetDecomposeHistoryId(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO);


    /**
    * 删除目标分解历史版本表信息
    *
    * @param targetDecomposeHistoryId 目标分解历史版本表主键
    * @return 结果
    */
    int deleteTargetDecomposeHistoryByTargetDecomposeHistoryId(Long targetDecomposeHistoryId);
    /**
    * 导入Excel
    * @param list
    */
    void importTargetDecomposeHistory(List<TargetDecomposeHistoryExcel> list);
    /**
    * 导出Excel
    * @param targetDecomposeHistoryDTO
    * @return
    */
    List<TargetDecomposeHistoryExcel> exportTargetDecomposeHistory(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO);
}
