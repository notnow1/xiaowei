package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Country;
import net.qixiaowei.system.manage.api.dto.basic.CountryDTO;
import net.qixiaowei.system.manage.mapper.basic.CountryMapper;
import net.qixiaowei.system.manage.service.basic.ICountryService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* CountryService业务层处理
* @author TANGMICHI
* @since 2022-10-20
*/
@Service
public class CountryServiceImpl implements ICountryService{
    @Autowired
    private CountryMapper countryMapper;

    /**
    * 查询国家表
    *
    * @param countryId 国家表主键
    * @return 国家表
    */
    @Override
    public CountryDTO selectCountryByCountryId(Long countryId)
    {
    return countryMapper.selectCountryByCountryId(countryId);
    }

    /**
    * 查询国家表列表
    *
    * @param countryDTO 国家表
    * @return 国家表
    */
    @Override
    public List<CountryDTO> selectCountryList(CountryDTO countryDTO)
    {
    Country country=new Country();
    BeanUtils.copyProperties(countryDTO,country);
    return this.createTree(countryMapper.selectCountryList(country),0);
    }

    /**
     * 返回国籍名称附带excel父子级名称
     * @return
     */
    @Override
    public List<CountryDTO> selectCountryList() {
        Country country=new Country();
        this.createTree(countryMapper.selectCountryList(country),0);
        return  this.treeToList(countryMapper.selectCountryList(country));
    }

    /**
     * 树形结构
     *
     * @param lists
     * @param pid
     * @return
     */
    private List<CountryDTO> createTree(List<CountryDTO> lists, int pid) {
        List<CountryDTO> tree = new ArrayList<>();
        for (CountryDTO catelog : lists) {
            if (catelog.getParentCountryId() == pid) {
                if (pid==0){
                    catelog.setCountryExcelName(catelog.getCountryExcelName());
                } else {
                    List<CountryDTO> countryDTOList = lists.stream().filter(f -> f.getCountryId() == pid).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(countryDTOList)){
                        catelog.setCountryExcelName(countryDTOList.get(0).getCountryExcelName()+"/"+catelog.getCountryExcelName());
                    }
                }
                catelog.setChildren(createTree(lists, Integer.parseInt(catelog.getCountryId().toString())));
                tree.add(catelog);
            }
        }
        return tree;
    }


    /**
     * 树形结构转list
     *
     * @param
     * @return
     */
    private List<CountryDTO> treeToList(List<CountryDTO> depts) {

        List<CountryDTO> resultList = new ArrayList<>();
        for (CountryDTO countryDTO : depts) {
            if (countryDTO.getChildren() != null && countryDTO.getChildren().size() > 0) {
                resultList.add(countryDTO);
                treeToList(countryDTO.getChildren());
            } else {
                resultList.add(countryDTO);
            }
        }
        return resultList;
    }

    /**
    * 新增国家表
    *
    * @param countryDTO 国家表
    * @return 结果
    */
    @Override
    public CountryDTO insertCountry(CountryDTO countryDTO){
    Country country=new Country();
    BeanUtils.copyProperties(countryDTO,country);
    country.setCreateBy(SecurityUtils.getUserId());
    country.setCreateTime(DateUtils.getNowDate());
    country.setUpdateTime(DateUtils.getNowDate());
    country.setUpdateBy(SecurityUtils.getUserId());
    country.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    countryMapper.insertCountry(country);
    countryDTO.setCountryId(country.getCountryId());
    return countryDTO;
    }

    /**
    * 修改国家表
    *
    * @param countryDTO 国家表
    * @return 结果
    */
    @Override
    public int updateCountry(CountryDTO countryDTO)
    {
    Country country=new Country();
    BeanUtils.copyProperties(countryDTO,country);
    country.setUpdateTime(DateUtils.getNowDate());
    country.setUpdateBy(SecurityUtils.getUserId());
    return countryMapper.updateCountry(country);
    }

    /**
    * 逻辑批量删除国家表
    *
    * @param countryIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteCountryByCountryIds(List<Long> countryIds){
    return countryMapper.logicDeleteCountryByCountryIds(countryIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除国家表信息
    *
    * @param countryId 国家表主键
    * @return 结果
    */
    @Override
    public int deleteCountryByCountryId(Long countryId)
    {
    return countryMapper.deleteCountryByCountryId(countryId);
    }

     /**
     * 逻辑删除国家表信息
     *
     * @param  countryDTO 国家表
     * @return 结果
     */
     @Override
     public int logicDeleteCountryByCountryId(CountryDTO countryDTO)
     {
     Country country=new Country();
     country.setCountryId(countryDTO.getCountryId());
     country.setUpdateTime(DateUtils.getNowDate());
     country.setUpdateBy(SecurityUtils.getUserId());
     return countryMapper.logicDeleteCountryByCountryId(country);
     }

     /**
     * 物理删除国家表信息
     *
     * @param  countryDTO 国家表
     * @return 结果
     */
     
     @Override
     public int deleteCountryByCountryId(CountryDTO countryDTO)
     {
     Country country=new Country();
     BeanUtils.copyProperties(countryDTO,country);
     return countryMapper.deleteCountryByCountryId(country.getCountryId());
     }
     /**
     * 物理批量删除国家表
     *
     * @param countryDtos 需要删除的国家表主键
     * @return 结果
     */
     
     @Override
     public int deleteCountryByCountryIds(List<CountryDTO> countryDtos){
     List<Long> stringList = new ArrayList();
     for (CountryDTO countryDTO : countryDtos) {
     stringList.add(countryDTO.getCountryId());
     }
     return countryMapper.deleteCountryByCountryIds(stringList);
     }

    /**
    * 批量新增国家表信息
    *
    * @param countryDtos 国家表对象
    */
    
    @Override
    public int insertCountrys(List<CountryDTO> countryDtos){
      List<Country> countryList = new ArrayList();

    for (CountryDTO countryDTO : countryDtos) {
      Country country =new Country();
      BeanUtils.copyProperties(countryDTO,country);
       country.setCreateBy(SecurityUtils.getUserId());
       country.setCreateTime(DateUtils.getNowDate());
       country.setUpdateTime(DateUtils.getNowDate());
       country.setUpdateBy(SecurityUtils.getUserId());
       country.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      countryList.add(country);
    }
    return countryMapper.batchCountry(countryList);
    }

    /**
    * 批量修改国家表信息
    *
    * @param countryDtos 国家表对象
    */
    
    @Override
    public int updateCountrys(List<CountryDTO> countryDtos){
     List<Country> countryList = new ArrayList();

     for (CountryDTO countryDTO : countryDtos) {
     Country country =new Country();
     BeanUtils.copyProperties(countryDTO,country);
        country.setCreateBy(SecurityUtils.getUserId());
        country.setCreateTime(DateUtils.getNowDate());
        country.setUpdateTime(DateUtils.getNowDate());
        country.setUpdateBy(SecurityUtils.getUserId());
     countryList.add(country);
     }
     return countryMapper.updateCountrys(countryList);
    }
}

