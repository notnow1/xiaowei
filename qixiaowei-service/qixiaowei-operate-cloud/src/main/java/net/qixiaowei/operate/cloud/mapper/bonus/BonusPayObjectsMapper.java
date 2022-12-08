package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayObjects;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* BonusPayObjectsMapper接口
* @author TANGMICHI
* @since 2022-12-08
*/
public interface BonusPayObjectsMapper{
    /**
    * 查询奖金发放对象表
    *
    * @param bonusPayObjectsId 奖金发放对象表主键
    * @return 奖金发放对象表
    */
    BonusPayObjectsDTO selectBonusPayObjectsByBonusPayObjectsId(@Param("bonusPayObjectsId")Long bonusPayObjectsId);


    /**
    * 批量查询奖金发放对象表
    *
    * @param bonusPayObjectsIds 奖金发放对象表主键集合
    * @return 奖金发放对象表
    */
    List<BonusPayObjectsDTO> selectBonusPayObjectsByBonusPayObjectsIds(@Param("bonusPayObjectsIds") List<Long> bonusPayObjectsIds);

    /**
    * 查询奖金发放对象表列表
    *
    * @param bonusPayObjects 奖金发放对象表
    * @return 奖金发放对象表集合
    */
    List<BonusPayObjectsDTO> selectBonusPayObjectsList(@Param("bonusPayObjects")BonusPayObjects bonusPayObjects);

    /**
    * 新增奖金发放对象表
    *
    * @param bonusPayObjects 奖金发放对象表
    * @return 结果
    */
    int insertBonusPayObjects(@Param("bonusPayObjects")BonusPayObjects bonusPayObjects);

    /**
    * 修改奖金发放对象表
    *
    * @param bonusPayObjects 奖金发放对象表
    * @return 结果
    */
    int updateBonusPayObjects(@Param("bonusPayObjects")BonusPayObjects bonusPayObjects);

    /**
    * 批量修改奖金发放对象表
    *
    * @param bonusPayObjectsList 奖金发放对象表
    * @return 结果
    */
    int updateBonusPayObjectss(@Param("bonusPayObjectsList")List<BonusPayObjects> bonusPayObjectsList);
    /**
    * 逻辑删除奖金发放对象表
    *
    * @param bonusPayObjects
    * @return 结果
    */
    int logicDeleteBonusPayObjectsByBonusPayObjectsId(@Param("bonusPayObjects")BonusPayObjects bonusPayObjects);

    /**
    * 逻辑批量删除奖金发放对象表
    *
    * @param bonusPayObjectsIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteBonusPayObjectsByBonusPayObjectsIds(@Param("bonusPayObjectsIds")List<Long> bonusPayObjectsIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除奖金发放对象表
    *
    * @param bonusPayObjectsId 奖金发放对象表主键
    * @return 结果
    */
    int deleteBonusPayObjectsByBonusPayObjectsId(@Param("bonusPayObjectsId")Long bonusPayObjectsId);

    /**
    * 物理批量删除奖金发放对象表
    *
    * @param bonusPayObjectsIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteBonusPayObjectsByBonusPayObjectsIds(@Param("bonusPayObjectsIds")List<Long> bonusPayObjectsIds);

    /**
    * 批量新增奖金发放对象表
    *
    * @param BonusPayObjectss 奖金发放对象表列表
    * @return 结果
    */
    int batchBonusPayObjects(@Param("bonusPayObjectss")List<BonusPayObjects> BonusPayObjectss);
}
