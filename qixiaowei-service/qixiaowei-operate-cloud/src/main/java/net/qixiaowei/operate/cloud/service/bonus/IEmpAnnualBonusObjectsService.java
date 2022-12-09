package net.qixiaowei.operate.cloud.service.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.bonus.EmpAnnualBonusObjectsDTO;



/**
* EmpAnnualBonusObjectsService接口
* @author TANGMICHI
* @since 2022-12-02
*/
public interface IEmpAnnualBonusObjectsService{
    /**
    * 查询个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsId 个人年终奖发放对象表主键
    * @return 个人年终奖发放对象表
    */
    EmpAnnualBonusObjectsDTO selectEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(Long empAnnualBonusObjectsId);

    /**
    * 查询个人年终奖发放对象表列表
    *
    * @param empAnnualBonusObjectsDTO 个人年终奖发放对象表
    * @return 个人年终奖发放对象表集合
    */
    List<EmpAnnualBonusObjectsDTO> selectEmpAnnualBonusObjectsList(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO);

    /**
    * 新增个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsDTO 个人年终奖发放对象表
    * @return 结果
    */
    EmpAnnualBonusObjectsDTO insertEmpAnnualBonusObjects(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO);

    /**
    * 修改个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsDTO 个人年终奖发放对象表
    * @return 结果
    */
    int updateEmpAnnualBonusObjects(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO);

    /**
    * 批量修改个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsDtos 个人年终奖发放对象表
    * @return 结果
    */
    int updateEmpAnnualBonusObjectss(List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDtos);

    /**
    * 批量新增个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsDtos 个人年终奖发放对象表
    * @return 结果
    */
    int insertEmpAnnualBonusObjectss(List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDtos);

    /**
    * 逻辑批量删除个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsIds 需要删除的个人年终奖发放对象表集合
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(List<Long> empAnnualBonusObjectsIds);

    /**
    * 逻辑删除个人年终奖发放对象表信息
    *
    * @param empAnnualBonusObjectsDTO
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO);
    /**
    * 批量删除个人年终奖发放对象表
    *
    * @param EmpAnnualBonusObjectsDtos
    * @return 结果
    */
    int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(List<EmpAnnualBonusObjectsDTO> EmpAnnualBonusObjectsDtos);

    /**
    * 逻辑删除个人年终奖发放对象表信息
    *
    * @param empAnnualBonusObjectsDTO
    * @return 结果
    */
    int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO);


    /**
    * 删除个人年终奖发放对象表信息
    *
    * @param empAnnualBonusObjectsId 个人年终奖发放对象表主键
    * @return 结果
    */
    int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(Long empAnnualBonusObjectsId);

}
