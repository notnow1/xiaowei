package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.basic.Country;
import net.qixiaowei.system.manage.api.dto.basic.CountryDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* CountryMapper接口
* @author TANGMICHI
* @since 2022-10-20
*/
public interface CountryMapper{
    /**
    * 查询国家表
    *
    * @param countryId 国家表主键
    * @return 国家表
    */
    CountryDTO selectCountryByCountryId(@Param("countryId")Long countryId);


    /**
    * 批量查询国家表
    *
    * @param countryIds 国家表主键集合
    * @return 国家表
    */
    List<CountryDTO> selectCountryByCountryId(@Param("countryIds") List<Long> countryIds);

    /**
    * 查询国家表列表
    *
    * @param country 国家表
    * @return 国家表集合
    */
    List<CountryDTO> selectCountryList(@Param("country")Country country);

    /**
    * 新增国家表
    *
    * @param country 国家表
    * @return 结果
    */
    int insertCountry(@Param("country")Country country);

    /**
    * 修改国家表
    *
    * @param country 国家表
    * @return 结果
    */
    int updateCountry(@Param("country")Country country);

    /**
    * 批量修改国家表
    *
    * @param countryList 国家表
    * @return 结果
    */
    int updateCountrys(@Param("countryList")List<Country> countryList);
    /**
    * 逻辑删除国家表
    *
    * @param country
    * @return 结果
    */
    int logicDeleteCountryByCountryId(@Param("country")Country country);

    /**
    * 逻辑批量删除国家表
    *
    * @param countryIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteCountryByCountryIds(@Param("countryIds")List<Long> countryIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除国家表
    *
    * @param countryId 国家表主键
    * @return 结果
    */
    int deleteCountryByCountryId(@Param("countryId")Long countryId);

    /**
    * 物理批量删除国家表
    *
    * @param countryIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteCountryByCountryIds(@Param("countryIds")List<Long> countryIds);

    /**
    * 批量新增国家表
    *
    * @param Countrys 国家表列表
    * @return 结果
    */
    int batchCountry(@Param("countrys")List<Country> Countrys);
}
