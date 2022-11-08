package net.qixiaowei.operate.cloud.service.targetManager;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;

import java.util.List;


/**
* TargetOutcomeDetailsService接口
* @author Graves
* @since 2022-11-07
*/
public interface ITargetOutcomeDetailsService{
    /**
    * 查询目标结果详情表
    *
    * @param targetOutcomeDetailsId 目标结果详情表主键
    * @return 目标结果详情表
    */
    TargetOutcomeDetailsDTO selectTargetOutcomeDetailsByTargetOutcomeDetailsId(Long targetOutcomeDetailsId);

    /**
    * 查询目标结果详情表列表
    *
    * @param targetOutcomeDetailsDTO 目标结果详情表
    * @return 目标结果详情表集合
    */
    List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsList(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO);

    /**
    * 新增目标结果详情表
    *
    * @param targetOutcomeDetailsDTO 目标结果详情表
    * @return 结果
    */
    TargetOutcomeDetailsDTO insertTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO);

    /**
    * 修改目标结果详情表
    *
    * @param targetOutcomeDetailsDTO 目标结果详情表
    * @return 结果
    */
    int updateTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO);

    /**
    * 批量修改目标结果详情表
    *
    * @param targetOutcomeDetailsDtos 目标结果详情表
    * @return 结果
    */
    int updateTargetOutcomeDetailss(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos);

    /**
    * 批量新增目标结果详情表
    *
    * @param targetOutcomeDetailsDtos 目标结果详情表
    * @return 结果
    */
    int insertTargetOutcomeDetailss(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos);

    /**
    * 逻辑批量删除目标结果详情表
    *
    * @param targetOutcomeDetailsIds 需要删除的目标结果详情表集合
    * @return 结果
    */
    int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(List<Long> targetOutcomeDetailsIds);

    /**
    * 逻辑删除目标结果详情表信息
    *
    * @param targetOutcomeDetailsDTO
    * @return 结果
    */
    int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsId(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO);
    /**
    * 批量删除目标结果详情表
    *
    * @param TargetOutcomeDetailsDtos
    * @return 结果
    */
    int deleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(List<TargetOutcomeDetailsDTO> TargetOutcomeDetailsDtos);

    /**
    * 逻辑删除目标结果详情表信息
    *
    * @param targetOutcomeDetailsDTO
    * @return 结果
    */
    int deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO);


    /**
    * 删除目标结果详情表信息
    *
    * @param targetOutcomeDetailsId 目标结果详情表主键
    * @return 结果
    */
    int deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(Long targetOutcomeDetailsId);
//    /**
//    * 导入Excel
//    * @param list
//    */
//    void importTargetOutcomeDetails(List<TargetOutcomeDetailsExcel> list);
//    /**
//    * 导出Excel
//    * @param targetOutcomeDetailsDTO
//    * @return
//    */
//    List<TargetOutcomeDetailsExcel> exportTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO);
}
