package net.qixiaowei.strategy.cloud.service.impl.businessDesign;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.businessDesign.BusinessDesignParam;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.mapper.businessDesign.BusinessDesignParamMapper;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * BusinessDesignParamService业务层处理
 *
 * @author Graves
 * @since 2023-02-28
 */
@Service
public class BusinessDesignParamServiceImpl implements IBusinessDesignParamService {
    @Autowired
    private BusinessDesignParamMapper businessDesignParamMapper;

    /**
     * 查询业务设计参数表
     *
     * @param businessDesignParamId 业务设计参数表主键
     * @return 业务设计参数表
     */
    @Override
    public BusinessDesignParamDTO selectBusinessDesignParamByBusinessDesignParamId(Long businessDesignParamId) {
        return businessDesignParamMapper.selectBusinessDesignParamByBusinessDesignParamId(businessDesignParamId);
    }

    /**
     * 查询业务设计参数表列表
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 业务设计参数表
     */
    @Override
    public List<BusinessDesignParamDTO> selectBusinessDesignParamList(BusinessDesignParamDTO businessDesignParamDTO) {
        BusinessDesignParam businessDesignParam = new BusinessDesignParam();
        BeanUtils.copyProperties(businessDesignParamDTO, businessDesignParam);
        Map<String, Object> params = businessDesignParamDTO.getParams();
        businessDesignParam.setParams(params);
        return businessDesignParamMapper.selectBusinessDesignParamList(businessDesignParam);
    }

    /**
     * 新增业务设计参数表
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 结果
     */
    @Override
    public BusinessDesignParamDTO insertBusinessDesignParam(BusinessDesignParamDTO businessDesignParamDTO) {
        BusinessDesignParam businessDesignParam = new BusinessDesignParam();
        BeanUtils.copyProperties(businessDesignParamDTO, businessDesignParam);
        businessDesignParam.setCreateBy(SecurityUtils.getUserId());
        businessDesignParam.setCreateTime(DateUtils.getNowDate());
        businessDesignParam.setUpdateTime(DateUtils.getNowDate());
        businessDesignParam.setUpdateBy(SecurityUtils.getUserId());
        businessDesignParam.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        businessDesignParamMapper.insertBusinessDesignParam(businessDesignParam);
        businessDesignParamDTO.setBusinessDesignParamId(businessDesignParam.getBusinessDesignParamId());
        return businessDesignParamDTO;
    }

    /**
     * 修改业务设计参数表
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 结果
     */
    @Override
    public int updateBusinessDesignParam(BusinessDesignParamDTO businessDesignParamDTO) {
        BusinessDesignParam businessDesignParam = new BusinessDesignParam();
        BeanUtils.copyProperties(businessDesignParamDTO, businessDesignParam);
        businessDesignParam.setUpdateTime(DateUtils.getNowDate());
        businessDesignParam.setUpdateBy(SecurityUtils.getUserId());
        return businessDesignParamMapper.updateBusinessDesignParam(businessDesignParam);
    }

    /**
     * 逻辑批量删除业务设计参数表
     *
     * @param businessDesignParamIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteBusinessDesignParamByBusinessDesignParamIds(List<Long> businessDesignParamIds) {
        return businessDesignParamMapper.logicDeleteBusinessDesignParamByBusinessDesignParamIds(businessDesignParamIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除业务设计参数表信息
     *
     * @param businessDesignParamId 业务设计参数表主键
     * @return 结果
     */
    @Override
    public int deleteBusinessDesignParamByBusinessDesignParamId(Long businessDesignParamId) {
        return businessDesignParamMapper.deleteBusinessDesignParamByBusinessDesignParamId(businessDesignParamId);
    }

    /**
     * 根绝业务设计ID查询参数信息
     *
     * @param businessDesignId 业务设计参数表主键
     * @return 结果
     */
    @Override
    public List<BusinessDesignParamDTO> selectBusinessDesignParamByBusinessDesignId(Long businessDesignId) {
        return businessDesignParamMapper.selectBusinessDesignParamByBusinessDesignId(businessDesignId);
    }

    /**
     * 根绝业务设计ID集合查询参数信息
     *
     * @param businessDesignIds 业务设计参数表主键集合
     * @return 结果
     */
    @Override
    public List<BusinessDesignParamDTO> selectBusinessDesignParamByBusinessDesignIds(List<Long> businessDesignIds) {
        return businessDesignParamMapper.selectBusinessDesignParamByBusinessDesignIds(businessDesignIds);
    }

    /**
     * 逻辑删除业务设计参数表信息
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 结果
     */
    @Override
    public int logicDeleteBusinessDesignParamByBusinessDesignParamId(BusinessDesignParamDTO businessDesignParamDTO) {
        BusinessDesignParam businessDesignParam = new BusinessDesignParam();
        businessDesignParam.setBusinessDesignParamId(businessDesignParamDTO.getBusinessDesignParamId());
        businessDesignParam.setUpdateTime(DateUtils.getNowDate());
        businessDesignParam.setUpdateBy(SecurityUtils.getUserId());
        return businessDesignParamMapper.logicDeleteBusinessDesignParamByBusinessDesignParamId(businessDesignParam);
    }

    /**
     * 物理删除业务设计参数表信息
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 结果
     */

    @Override
    public int deleteBusinessDesignParamByBusinessDesignParamId(BusinessDesignParamDTO businessDesignParamDTO) {
        BusinessDesignParam businessDesignParam = new BusinessDesignParam();
        BeanUtils.copyProperties(businessDesignParamDTO, businessDesignParam);
        return businessDesignParamMapper.deleteBusinessDesignParamByBusinessDesignParamId(businessDesignParam.getBusinessDesignParamId());
    }

    /**
     * 物理批量删除业务设计参数表
     *
     * @param businessDesignParamDtos 需要删除的业务设计参数表主键
     * @return 结果
     */

    @Override
    public int deleteBusinessDesignParamByBusinessDesignParamIds(List<BusinessDesignParamDTO> businessDesignParamDtos) {
        List<Long> stringList = new ArrayList();
        for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDtos) {
            stringList.add(businessDesignParamDTO.getBusinessDesignParamId());
        }
        return businessDesignParamMapper.deleteBusinessDesignParamByBusinessDesignParamIds(stringList);
    }

    /**
     * 批量新增业务设计参数表信息
     *
     * @param businessDesignParamDtos 业务设计参数表对象
     */

    public int insertBusinessDesignParams(List<BusinessDesignParamDTO> businessDesignParamDtos) {
        List<BusinessDesignParam> businessDesignParamList = new ArrayList();

        for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDtos) {
            BusinessDesignParam businessDesignParam = new BusinessDesignParam();
            BeanUtils.copyProperties(businessDesignParamDTO, businessDesignParam);
            businessDesignParam.setCreateBy(SecurityUtils.getUserId());
            businessDesignParam.setCreateTime(DateUtils.getNowDate());
            businessDesignParam.setUpdateTime(DateUtils.getNowDate());
            businessDesignParam.setUpdateBy(SecurityUtils.getUserId());
            businessDesignParam.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            businessDesignParamList.add(businessDesignParam);
        }
        return businessDesignParamMapper.batchBusinessDesignParam(businessDesignParamList);
    }

    /**
     * 批量修改业务设计参数表信息
     *
     * @param businessDesignParamDtos 业务设计参数表对象
     */

    public int updateBusinessDesignParams(List<BusinessDesignParamDTO> businessDesignParamDtos) {
        List<BusinessDesignParam> businessDesignParamList = new ArrayList();

        for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDtos) {
            BusinessDesignParam businessDesignParam = new BusinessDesignParam();
            BeanUtils.copyProperties(businessDesignParamDTO, businessDesignParam);
            businessDesignParam.setCreateBy(SecurityUtils.getUserId());
            businessDesignParam.setCreateTime(DateUtils.getNowDate());
            businessDesignParam.setUpdateTime(DateUtils.getNowDate());
            businessDesignParam.setUpdateBy(SecurityUtils.getUserId());
            businessDesignParamList.add(businessDesignParam);
        }
        return businessDesignParamMapper.updateBusinessDesignParams(businessDesignParamList);
    }

}

