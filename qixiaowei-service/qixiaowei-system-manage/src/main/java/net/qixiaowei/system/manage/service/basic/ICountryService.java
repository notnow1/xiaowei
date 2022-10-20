package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.CountryDTO;


/**
* CountryService接口
* @author TANGMICHI
* @since 2022-10-20
*/
public interface ICountryService{
    /**
    * 查询国家表
    *
    * @param countryId 国家表主键
    * @return 国家表
    */
    CountryDTO selectCountryByCountryId(Long countryId);

    /**
    * 查询国家表列表
    *
    * @param countryDTO 国家表
    * @return 国家表集合
    */
    List<CountryDTO> selectCountryList(CountryDTO countryDTO);

    /**
    * 新增国家表
    *
    * @param countryDTO 国家表
    * @return 结果
    */
    CountryDTO insertCountry(CountryDTO countryDTO);

    /**
    * 修改国家表
    *
    * @param countryDTO 国家表
    * @return 结果
    */
    int updateCountry(CountryDTO countryDTO);

    /**
    * 批量修改国家表
    *
    * @param countryDtos 国家表
    * @return 结果
    */
    int updateCountrys(List<CountryDTO> countryDtos);

    /**
    * 批量新增国家表
    *
    * @param countryDtos 国家表
    * @return 结果
    */
    int insertCountrys(List<CountryDTO> countryDtos);

    /**
    * 逻辑批量删除国家表
    *
    * @param countryIds 需要删除的国家表集合
    * @return 结果
    */
    int logicDeleteCountryByCountryIds(List<Long> countryIds);

    /**
    * 逻辑删除国家表信息
    *
    * @param countryDTO
    * @return 结果
    */
    int logicDeleteCountryByCountryId(CountryDTO countryDTO);
    /**
    * 批量删除国家表
    *
    * @param CountryDtos
    * @return 结果
    */
    int deleteCountryByCountryIds(List<CountryDTO> CountryDtos);

    /**
    * 逻辑删除国家表信息
    *
    * @param countryDTO
    * @return 结果
    */
    int deleteCountryByCountryId(CountryDTO countryDTO);


    /**
    * 删除国家表信息
    *
    * @param countryId 国家表主键
    * @return 结果
    */
    int deleteCountryByCountryId(Long countryId);
}
