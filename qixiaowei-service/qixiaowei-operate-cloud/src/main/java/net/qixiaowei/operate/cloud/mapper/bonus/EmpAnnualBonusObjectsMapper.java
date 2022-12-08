package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpAnnualBonusObjects;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusSnapshotDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmpAnnualBonusObjectsMapper接口
* @author TANGMICHI
* @since 2022-12-05
*/
public interface EmpAnnualBonusObjectsMapper{
    /**
    * 查询个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsId 个人年终奖发放对象表主键
    * @return 个人年终奖发放对象表
    */
    EmpAnnualBonusObjectsDTO selectEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(@Param("empAnnualBonusObjectsId")Long empAnnualBonusObjectsId);

    /**
     * 根据个人年终奖主表id查询个人年终奖发放对象表
     *
     * @param employeeAnnualBonusId 个人年终奖主表id
     * @return 个人年终奖发放对象表
     */
    List<EmpAnnualBonusObjectsDTO> selectEmpAnnualBonusObjectsByEmployeeAnnualBonusId(@Param("employeeAnnualBonusId")Long employeeAnnualBonusId);

    /**
     * 根据个人年终奖主表id集合批量查询个人年终奖发放对象表
     *
     * @param employeeAnnualBonusIds 个人年终奖主表id集合
     * @return 个人年终奖发放对象表
     */
    List<EmpAnnualBonusObjectsDTO> selectEmpAnnualBonusObjectsByEmployeeAnnualBonusIds(@Param("employeeAnnualBonusIds") List<Long> employeeAnnualBonusIds);
    /**
    * 批量查询个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsIds 个人年终奖发放对象表主键集合
    * @return 个人年终奖发放对象表
    */
    List<EmpAnnualBonusObjectsDTO> selectEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(@Param("empAnnualBonusObjectsIds") List<Long> empAnnualBonusObjectsIds);

    /**
    * 查询个人年终奖发放对象表列表
    *
    * @param empAnnualBonusObjects 个人年终奖发放对象表
    * @return 个人年终奖发放对象表集合
    */
    List<EmpAnnualBonusObjectsDTO> selectEmpAnnualBonusObjectsList(@Param("empAnnualBonusObjects")EmpAnnualBonusObjects empAnnualBonusObjects);

    /**
    * 新增个人年终奖发放对象表
    *
    * @param empAnnualBonusObjects 个人年终奖发放对象表
    * @return 结果
    */
    int insertEmpAnnualBonusObjects(@Param("empAnnualBonusObjects")EmpAnnualBonusObjects empAnnualBonusObjects);

    /**
    * 修改个人年终奖发放对象表
    *
    * @param empAnnualBonusObjects 个人年终奖发放对象表
    * @return 结果
    */
    int updateEmpAnnualBonusObjects(@Param("empAnnualBonusObjects")EmpAnnualBonusObjects empAnnualBonusObjects);

    /**
    * 批量修改个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsList 个人年终奖发放对象表
    * @return 结果
    */
    int updateEmpAnnualBonusObjectss(@Param("empAnnualBonusObjectsList")List<EmpAnnualBonusObjects> empAnnualBonusObjectsList);
    /**
    * 逻辑删除个人年终奖发放对象表
    *
    * @param empAnnualBonusObjects
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(@Param("empAnnualBonusObjects")EmpAnnualBonusObjects empAnnualBonusObjects);

    /**
    * 逻辑批量删除个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(@Param("empAnnualBonusObjectsIds")List<Long> empAnnualBonusObjectsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsId 个人年终奖发放对象表主键
    * @return 结果
    */
    int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(@Param("empAnnualBonusObjectsId")Long empAnnualBonusObjectsId);

    /**
    * 物理批量删除个人年终奖发放对象表
    *
    * @param empAnnualBonusObjectsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(@Param("empAnnualBonusObjectsIds")List<Long> empAnnualBonusObjectsIds);

    /**
    * 批量新增个人年终奖发放对象表
    *
    * @param EmpAnnualBonusObjectss 个人年终奖发放对象表列表
    * @return 结果
    */
    int batchEmpAnnualBonusObjects(@Param("empAnnualBonusObjectss")List<EmpAnnualBonusObjects> EmpAnnualBonusObjectss);

    /**
     * 根据个人年终奖id查询子表集合
     * @param employeeAnnualBonusId
     * @return
     */
    List<EmpAnnualBonusSnapshotDTO> selectEmpAnnualBonusObjectsAndSnapshot(@Param("employeeAnnualBonusId") Long employeeAnnualBonusId);
}
