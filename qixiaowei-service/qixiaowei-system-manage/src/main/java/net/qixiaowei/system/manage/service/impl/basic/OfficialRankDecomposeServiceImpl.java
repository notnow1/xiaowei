package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankDecompose;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankSystem;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.mapper.basic.OfficialRankDecomposeMapper;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankDecomposeService;
import net.qixiaowei.system.manage.service.system.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * OfficialRankDecomposeService业务层处理
 *
 * @author Graves
 * @since 2022-10-07
 */
@Service
public class OfficialRankDecomposeServiceImpl implements IOfficialRankDecomposeService {

    @Autowired
    private OfficialRankDecomposeMapper officialRankDecomposeMapper;

    @Autowired
    private RemoteAreaService areaService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IRegionService regionService;

    /**
     * 查询职级分解表
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 职级分解表
     */
    @Override
    public OfficialRankDecomposeDTO selectOfficialRankDecomposeByOfficialRankDecomposeId(Long officialRankDecomposeId) {
        return officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecomposeId);
    }

    /**
     * 查询职级分解表列表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 职级分解表
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeList(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        return officialRankDecomposeMapper.selectOfficialRankDecomposeList(officialRankDecompose);
    }

    /**
     * 新增职级分解表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOfficialRankDecompose(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        officialRankDecompose.setCreateBy(SecurityUtils.getUserId());
        officialRankDecompose.setCreateTime(DateUtils.getNowDate());
        officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
        officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
        officialRankDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return officialRankDecomposeMapper.insertOfficialRankDecompose(officialRankDecompose);
    }

    /**
     * 修改职级分解表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOfficialRankDecompose(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
        officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
        return officialRankDecomposeMapper.updateOfficialRankDecompose(officialRankDecompose);
    }

    /**
     * 逻辑批量删除职级分解
     *
     * @param officialRankDecomposeDtos 需要删除的职级分解表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos) {
        List<Long> officialRankDecomposeIds = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            officialRankDecomposeIds.add(officialRankDecomposeDTO.getOfficialRankDecomposeId());
        }
        return officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(officialRankDecomposeIds, officialRankDecomposeDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除职级分解表信息
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteOfficialRankDecomposeByOfficialRankDecomposeId(Long officialRankDecomposeId) {
        return officialRankDecomposeMapper.deleteOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecomposeId);
    }

    /**
     * 通过officialRankSystemId查找职级分解
     *
     * @param officialRankSystemId
     * @return
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeByOfficialRankSystemId(Long officialRankSystemId) {
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系ID不能为空");
        }
        return officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
    }

    /**
     * 通过officialRankSystemId查找职级分解
     *
     * @param officialRankSystemId 职级体系ID
     * @return
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeAndNameByOfficialRankSystemId(Long officialRankSystemId, Integer rankDecomposeDimension) {
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系ID不能为空");
        }
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
        List<Long> decomposeDimensions = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
            decomposeDimensions.add(officialRankDecomposeDTO.getDecomposeDimension());
        }
        if (StringUtils.isEmpty(decomposeDimensions)) {
            return officialRankDecomposeDTOS;
        }
        switch (rankDecomposeDimension) {
            case 1:
                List<DepartmentDTO> listDepartment = departmentService.selectDepartmentByDepartmentIds(decomposeDimensions);
                if (StringUtils.isNotEmpty(listDepartment) && listDepartment.size() == decomposeDimensions.size()) {
                    for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                        for (DepartmentDTO departmentDTO : listDepartment) {
                            if (departmentDTO.getDepartmentId().equals(officialRankDecomposeDTO.getDecomposeDimension())) {
                                officialRankDecomposeDTO.setDecomposeDimensionName(departmentDTO.getDepartmentName());
                                break;
                            }
                        }
                    }
                }
                break;
            case 2:
                R<List<AreaDTO>> listArea = areaService.getName(decomposeDimensions);
                if (listArea.getCode() == 200 && StringUtils.isNotEmpty(listArea.getData()) && listArea.getData().size() == decomposeDimensions.size()) {
                    for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                        for (AreaDTO areaDTO : listArea.getData()) {
                            if (areaDTO.getAreaId().equals(officialRankDecomposeDTO.getDecomposeDimension())) {
                                officialRankDecomposeDTO.setDecomposeDimensionName(areaDTO.getAreaName());
                                break;
                            }
                        }
                    }
                }
                break;
            case 3:
                Set<Long> decomposeDimensionSet = new HashSet<>(decomposeDimensions);
                List<RegionDTO> regionsByIds = regionService.getRegionsByIds(decomposeDimensionSet);
                if (StringUtils.isNotEmpty(regionsByIds) && regionsByIds.size() == decomposeDimensions.size()) {
                    for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                        for (RegionDTO regionDTO : regionsByIds) {
                            if (regionDTO.getRegionId().equals(officialRankDecomposeDTO.getDecomposeDimension())) {
                                officialRankDecomposeDTO.setDecomposeDimensionName(regionDTO.getRegionName());
                                break;
                            }
                        }
                    }
                }
                break;
            case 4:
                R<List<ProductDTO>> listProduct = productService.getName(decomposeDimensions);
                if (listProduct.getCode() == 200 && StringUtils.isNotEmpty(listProduct.getData()) && listProduct.getData().size() == decomposeDimensions.size()) {
                    for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                        for (ProductDTO productDTO : listProduct.getData()) {
                            if (productDTO.getProductId().equals(officialRankDecomposeDTO.getDecomposeDimension())) {
                                officialRankDecomposeDTO.setDecomposeDimensionName(productDTO.getProductName());
                                break;
                            }
                        }
                    }
                }
                break;
        }
        return officialRankDecomposeDTOS;
    }

    /**
     * 通过officialRankSystemIds查找职级分解
     *
     * @param officialRankSystemIds
     * @return
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeBySystemIds(List<Long> officialRankSystemIds) {
        if (StringUtils.isEmpty(officialRankSystemIds)) {
            throw new ServiceException("officialRankSystemIds为空");
        }
        return officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemIds(officialRankSystemIds);
    }

    /**
     * 通过officialRankSystemId，rankDecomposeDimensionBefore删除rankDecomposeDimension
     *
     * @param officialRankSystemId
     * @param rankDecomposeDimensionBefore
     * @return
     */
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankDecompose(Long officialRankSystemId, Integer rankDecomposeDimensionBefore) {
        return officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecompose(officialRankSystemId, rankDecomposeDimensionBefore);
    }

    /**
     * 根据officialRankSystemIds删除职级分解
     *
     * @param officialRankSystemIds
     * @return
     */
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankSystemIds(List<Long> officialRankSystemIds) {
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS =
                officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemIds(officialRankSystemIds);
        if (StringUtils.isNotEmpty(officialRankDecomposeDTOS)) {
            List<Long> officialRankDecomposeIds = new ArrayList<>();
            for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                officialRankDecomposeIds.add(officialRankDecomposeDTO.getOfficialRankDecomposeId());
            }
            officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(officialRankDecomposeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        }
        return 1;
    }

    /**
     * 通过officialRankSystemId删除职级分解
     *
     * @param officialRankSystemId
     * @return
     */
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankSystemId(Long officialRankSystemId) {
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
        if (StringUtils.isNotEmpty(officialRankDecomposeDTOS)) {
            officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialSystemId(officialRankSystemId, SecurityUtils.getUserId(), DateUtils.getNowDate());
        }
        return 1;
    }

    /**
     * 逻辑删除职级分解表信息
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeId(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        return officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecompose, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除职级分解表信息
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankDecomposeByOfficialRankDecomposeId(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        return officialRankDecomposeMapper.deleteOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecompose.getOfficialRankDecomposeId());
    }

    /**
     * 物理批量删除职级分解表
     *
     * @param officialRankDecomposeDtos 需要删除的职级分解表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankDecomposeByOfficialRankDecomposeIds(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos) {
        List<Long> officialRankDecomposeIds = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            officialRankDecomposeIds.add(officialRankDecomposeDTO.getOfficialRankDecomposeId());
        }
        if (officialRankDecomposeMapper.isExist(officialRankDecomposeIds) != officialRankDecomposeIds.size()) {
            throw new ServiceException("当前的职级分解已不存在");
        }
        return officialRankDecomposeMapper.deleteOfficialRankDecomposeByOfficialRankDecomposeIds(officialRankDecomposeIds);
    }

    /**
     * 批量新增职级分解表信息
     *
     * @param officialRankDecomposeDtos
     * @param officialRankSystem
     */
    @Transactional
    public List<OfficialRankDecompose> insertOfficialRankDecomposes(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos, OfficialRankSystem officialRankSystem) {
        List<OfficialRankDecompose> officialRankDecomposeList = new ArrayList<>();
        Long officialRankSystemId = officialRankSystem.getOfficialRankSystemId();
        Integer rankDecomposeDimension = officialRankSystem.getRankDecomposeDimension();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
            BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
            officialRankDecompose.setOfficialRankSystemId(officialRankSystemId.toString());
            officialRankDecompose.setRankDecomposeDimension(rankDecomposeDimension);
            officialRankDecompose.setCreateBy(SecurityUtils.getUserId());
            officialRankDecompose.setCreateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
            officialRankDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            officialRankDecomposeList.add(officialRankDecompose);
        }
        officialRankDecomposeMapper.batchOfficialRankDecompose(officialRankDecomposeList);
        return officialRankDecomposeList;
    }

    /**
     * 批量修改职级分解表信息
     *
     * @param officialRankDecomposeDtos 职级分解表对象
     */
    @Transactional
    public int updateOfficialRankDecomposes(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos, OfficialRankSystem officialRankSystem) {
        Long officialRankSystemId = officialRankSystem.getOfficialRankSystemId();
        Integer rankDecomposeDimension = officialRankSystem.getRankDecomposeDimension();
        List<OfficialRankDecompose> officialRankDecomposeList = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
            BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
            officialRankDecompose.setOfficialRankSystemId(officialRankSystemId.toString());
            officialRankDecompose.setRankDecomposeDimension(rankDecomposeDimension);
            officialRankDecompose.setCreateBy(SecurityUtils.getUserId());
            officialRankDecompose.setCreateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
            officialRankDecomposeList.add(officialRankDecompose);
        }
        return officialRankDecomposeMapper.updateOfficialRankDecomposes(officialRankDecomposeList);
    }
}

