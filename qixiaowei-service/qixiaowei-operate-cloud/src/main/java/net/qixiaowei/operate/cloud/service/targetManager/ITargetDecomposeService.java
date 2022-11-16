package net.qixiaowei.operate.cloud.service.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeExcel;
import org.springframework.web.multipart.MultipartFile;


/**
 * TargetDecomposeService接口
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
public interface ITargetDecomposeService {

    /**
     * 查询经营结果分析报表详情
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    TargetDecomposeDTO selectResultTargetDecomposeByTargetDecomposeId(Long targetDecomposeId);
    /**
     * 查询滚动预测表详情
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    TargetDecomposeDTO selectRollTargetDecomposeByTargetDecomposeId(Long targetDecomposeId);
    /**
     * 查询目标分解(销售订单)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    TargetDecomposeDTO selectOrderTargetDecomposeByTargetDecomposeId(Long targetDecomposeId);

    /**
     * 查询目标分解(销售收入)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    TargetDecomposeDTO selectIncomeTargetDecomposeByTargetDecomposeId(Long targetDecomposeId);

    /**
     * 查询目标分解(销售回款)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    TargetDecomposeDTO selectReturnedTargetDecomposeByTargetDecomposeId(Long targetDecomposeId);

    /**
     * 查询目标分解(自定义)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    TargetDecomposeDTO selectCustomTargetDecomposeByTargetDecomposeId(Long targetDecomposeId);


    /**
     * 查询经营结果分析报表列表
     *
     * @param targetDecomposeDTO 目标分解(经营结果分析)表
     * @return 目标分解(经营结果分析)表集合
     */
    List<TargetDecomposeDTO> resultList(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 查询目标分解(销售订单)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 目标分解(销售订单)表集合
     */
    List<TargetDecomposeDTO> selectOrderList(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 分页查询滚动预测表列表
     *
     * @param targetDecomposeDTO 滚动预测表列表
     * @return 滚动预测表列表集合
     */
    List<TargetDecomposeDTO> rollPageList(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 查询目标分解(销售收入)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 目标分解(销售收入)表集合
     */
    List<TargetDecomposeDTO> selectIncomeList(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 查询目标分解(销售回款)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 目标分解(销售回款)表集合
     */
    List<TargetDecomposeDTO> selectReturnedList(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 查询目标分解(自定义)表列表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 目标分解(自定义)表集合
     */
    List<TargetDecomposeDTO> selectCustomList(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 新增目标分解(销售订单)表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    TargetDecomposeDTO insertOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 新增目标分解(销售收入)表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 结果
     */
    TargetDecomposeDTO insertIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 新增目标分解(销售回款)表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 结果
     */
    TargetDecomposeDTO insertReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 新增目标分解(自定义)表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 结果
     */
    TargetDecomposeDTO insertCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);


    /**
     * 修改滚动预测详情
     *
     * @param targetDecomposeDTO 修改滚动预测详情
     * @return 结果
     */
    int updateRollTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 修改经营结果分析报表详情
     *
     * @param targetDecomposeDTO 修改滚动预测详情
     * @return 结果
     */
    int updateResultTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);
    /**
     * 修改目标分解(销售订单)表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    int updateOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);
    /**
     * 修改目标分解(销售收入)表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 结果
     */
    int updateIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 修改目标分解(销售回款)表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 结果
     */
    int updateReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 修改目标分解(自定义)表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 结果
     */
    int updateCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 批量修改目标分解(销售订单)表
     *
     * @param targetDecomposeDtos 目标分解(销售订单)表
     * @return 结果
     */
    int updateTargetDecomposes(List<TargetDecomposeDTO> targetDecomposeDtos);

    /**
     * 批量新增目标分解(销售订单)表
     *
     * @param targetDecomposeDtos 目标分解(销售订单)表
     * @return 结果
     */
    int insertTargetDecomposes(List<TargetDecomposeDTO> targetDecomposeDtos);

    /**
     * 逻辑批量删除目标分解(销售订单)表
     *
     * @param targetDecomposeIds 需要删除的目标分解(销售订单)表集合
     * @return 结果
     */
    int logicDeleteOrderTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds);
    /**
     * 逻辑批量删除目标分解(销售收入)表
     *
     * @param targetDecomposeIds 需要删除的目标分解(销售收入)表集合
     * @return 结果
     */
    int logicDeleteIncomeTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds);

    /**
     * 逻辑批量删除目标分解(销售回款)表
     *
     * @param targetDecomposeIds 需要删除的目标分解(销售回款)表集合
     * @return 结果
     */
    int logicDeleteReturnedTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds);

    /**
     * 逻辑批量删除目标分解(自定义)表
     *
     * @param targetDecomposeIds 需要删除的目标分解(自定义)表集合
     * @return 结果
     */
    int logicDeleteCustomTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds);

    /**
     * 逻辑删除目标分解(销售订单)表信息
     *
     * @param targetDecomposeDTO
     * @return 结果
     */
    int logicDeleteOrderTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 逻辑删除目标分解(销售收入)表信息
     *
     * @param targetDecomposeDTO
     * @return 结果
     */
    int logicDeleteIncomeTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 逻辑删除目标分解(销售回款)表信息
     *
     * @param targetDecomposeDTO
     * @return 结果
     */
    int logicDeleteReturnedTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 逻辑删除目标分解(自定义)表信息
     *
     * @param targetDecomposeDTO
     * @return 结果
     */
    int logicDeleteCustomTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO);


    /**
     * 导入Excel
     *
     * @param list
     */
    void importTargetDecompose(List<TargetDecomposeExcel> list);
    /**
     * 解析Excel
     *
     * @param file
     */
    TargetDecomposeDTO  excelParseObject(MultipartFile file);

    /**
     * 目标分解(销售订单)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    List<TargetDecomposeExcel> exportOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 目标分解(销售收入)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    List<TargetDecomposeExcel> exportIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 目标分解(销售回款)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    List<TargetDecomposeExcel> exportReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 目标分解(自定义)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    List<TargetDecomposeExcel> exportCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO);

    /**
     * 移交预测负责人
     * @param targetDecomposeDTO
     * @return
     */
    int turnOverPrincipalEmployee(TargetDecomposeDTO targetDecomposeDTO);
}
