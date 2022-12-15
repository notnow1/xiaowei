package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.OfficialRankEmolument;
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentExcel;
import net.qixiaowei.operate.cloud.mapper.salary.OfficialRankEmolumentMapper;
import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.api.remote.basic.RemotePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OfficialRankEmolumentService业务层处理
 *
 * @author Graves
 * @since 2022-11-30
 */
@Service
public class OfficialRankEmolumentServiceImpl implements IOfficialRankEmolumentService {
    @Autowired
    private OfficialRankEmolumentMapper officialRankEmolumentMapper;

    @Autowired
    private RemoteOfficialRankSystemService officialRankSystemService;

    @Autowired
    private RemotePostService postService;


    /**
     * 查询职级薪酬表
     *
     * @param officialRankEmolument 职级薪酬表
     * @return 职级薪酬表
     */
    @Override
    public OfficialRankEmolumentDTO selectOfficialRankEmolumentByOfficialRankEmolumentId(OfficialRankEmolumentDTO officialRankEmolument) {
        Long officialRankSystemId = officialRankEmolument.getOfficialRankSystemId();
        OfficialRankSystemDTO officialRankSystemDTO;
        if (StringUtils.isNull(officialRankSystemId)) {
            List<OfficialRankSystemDTO> officialRankSystemDTOS = getOfficialRankSystemDTOS();
            if (StringUtils.isEmpty(officialRankSystemDTOS)) {
                throw new ServiceException("职级配置为空 请检查数据");
            }
            officialRankSystemDTO = officialRankSystemDTOS.get(0);
            officialRankSystemId = officialRankSystemDTO.getOfficialRankSystemId();
        } else {
            officialRankSystemDTO = getOfficialRankSystemById(officialRankSystemId);
        }
        String rankPrefixCode;//前缀
        if (StringUtils.isNull(officialRankSystemDTO.getRankPrefixCode())) {
            rankPrefixCode = "";
        } else {
            rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        }
        List<OfficialRankEmolumentDTO> officialRankEmolumentDTOS = officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemId(officialRankSystemId);
        R<List<PostDTO>> listR = postService.selectPostListByOfficialRank(officialRankSystemId, SecurityConstants.INNER);
        List<PostDTO> postDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("岗位远程调用失败 请联系管理员");
        }
        Integer rankStart = officialRankSystemDTO.getRankStart();
        Integer rankEnd = officialRankSystemDTO.getRankEnd();
        List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = new ArrayList<>();
        if (StringUtils.isEmpty(officialRankEmolumentDTOS)) { // 没有值
            for (Integer end = rankEnd; end >= rankStart; end--) {
                OfficialRankEmolumentDTO officialRankEmolumentDTO = new OfficialRankEmolumentDTO();
                List<Map<String, Object>> postList = getPostList(postDTOS, end);
                officialRankEmolumentDTO.setOfficialRank(end);
                officialRankEmolumentDTO.setPostList(postList);
                officialRankEmolumentDTO.setOfficialRankName(rankPrefixCode + end);
                officialRankEmolumentDTO.setSalaryCap(BigDecimal.ZERO);
                officialRankEmolumentDTO.setSalaryFloor(BigDecimal.ZERO);
                officialRankEmolumentDTO.setSalaryMedian(BigDecimal.ZERO);
                officialRankEmolumentDTO.setSalaryWide(BigDecimal.ZERO);
                officialRankEmolumentDTO.setIncreaseRate(BigDecimal.ZERO);
                officialRankEmolumentDTOList.add(officialRankEmolumentDTO);
            }
            OfficialRankEmolumentDTO officialRankEmolumentDTO = new OfficialRankEmolumentDTO();
            officialRankEmolumentDTO.setOfficialRankSystemId(officialRankSystemDTO.getOfficialRankSystemId());
            officialRankEmolumentDTO.setOfficialRankSystemName(officialRankSystemDTO.getOfficialRankSystemName());
            officialRankEmolumentDTO.setOfficialRankEmolumentDTOList(officialRankEmolumentDTOList);
            return officialRankEmolumentDTO;
        }
        for (Integer end = rankEnd; end >= rankStart; end--) {
            boolean isNotExist = true;
            for (int i = 0; i < officialRankEmolumentDTOS.size(); i++) {
                OfficialRankEmolumentDTO rankEmolumentDTO = officialRankEmolumentDTOS.get(i);
                if (end.equals(rankEmolumentDTO.getOfficialRank())) {
                    OfficialRankEmolumentDTO rankEmolument = new OfficialRankEmolumentDTO();
                    BeanUtils.copyProperties(rankEmolumentDTO, rankEmolument);
                    rankEmolument.setOfficialRank(rankEmolumentDTO.getOfficialRank());
                    rankEmolument.setOfficialRankName(rankPrefixCode + rankEmolumentDTO.getOfficialRank());
                    // 宽幅
                    BigDecimal wide = rankEmolumentDTO.getSalaryCap().subtract(rankEmolumentDTO.getSalaryFloor());
                    rankEmolument.setSalaryWide(wide);
                    // 递增率
                    if (i == officialRankEmolumentDTOS.size() - 1) {
                        rankEmolument.setIncreaseRate(BigDecimal.ZERO);
                    } else {
                        BigDecimal nextSalaryMedian = officialRankEmolumentDTOS.get(i + 1).getSalaryMedian();
                        if (nextSalaryMedian.compareTo(BigDecimal.ZERO) != 0) {
                            BigDecimal increaseRate = (rankEmolumentDTO.getSalaryMedian().subtract(nextSalaryMedian)).divide(nextSalaryMedian, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                            rankEmolument.setIncreaseRate(increaseRate);
                        } else {
                            rankEmolument.setIncreaseRate(BigDecimal.ZERO);
                        }
                    }
                    // 岗位
                    List<Map<String, Object>> postList = getPostList(postDTOS, rankEmolumentDTO.getOfficialRank());
                    rankEmolument.setPostList(postList);
                    officialRankEmolumentDTOList.add(rankEmolument);
                    isNotExist = false;
                    break;
                }
            }
            if (isNotExist) {
                OfficialRankEmolumentDTO officialRankEmolumentDTO = new OfficialRankEmolumentDTO();
                List<Map<String, Object>> postList = getPostList(postDTOS, end);
                officialRankEmolumentDTO.setOfficialRank(end);
                officialRankEmolumentDTO.setPostList(postList);
                officialRankEmolumentDTO.setOfficialRankName(rankPrefixCode + end);
                officialRankEmolumentDTO.setSalaryCap(BigDecimal.ZERO);
                officialRankEmolumentDTO.setSalaryFloor(BigDecimal.ZERO);
                officialRankEmolumentDTO.setSalaryMedian(BigDecimal.ZERO);
                officialRankEmolumentDTO.setSalaryWide(BigDecimal.ZERO);
                officialRankEmolumentDTO.setIncreaseRate(BigDecimal.ZERO);
                officialRankEmolumentDTOList.add(officialRankEmolumentDTO);
            }
        }
        OfficialRankEmolumentDTO officialRankEmolumentDTO = new OfficialRankEmolumentDTO();
        officialRankEmolumentDTO.setOfficialRankSystemId(officialRankSystemDTO.getOfficialRankSystemId());
        officialRankEmolumentDTO.setOfficialRankSystemName(officialRankSystemDTO.getOfficialRankSystemName());
        officialRankEmolumentDTO.setOfficialRankEmolumentDTOList(officialRankEmolumentDTOList);
        return officialRankEmolumentDTO;
    }

    /**
     * 获取岗位列表
     *
     * @param postDTOS 岗位DTO
     * @param end      结束
     */
    private List<Map<String, Object>> getPostList(List<PostDTO> postDTOS, Integer end) {
        List<Map<String, Object>> postList = new ArrayList<>();
        if (StringUtils.isNotEmpty(postDTOS)) {
            for (PostDTO postDTO : postDTOS) {
                if (postDTO.getPostRankUpper() >= end && postDTO.getPostRankLower() <= end) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", postDTO.getPostId());
                    map.put("name", postDTO.getPostName());
                    postList.add(map);
                }
            }
        }
        return postList;
    }

    /**
     * 查询职级薪酬表列表
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 职级薪酬表
     */
    @Override
    public List<OfficialRankEmolumentDTO> selectOfficialRankEmolumentList(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
        BeanUtils.copyProperties(officialRankEmolumentDTO, officialRankEmolument);
        return officialRankEmolumentMapper.selectOfficialRankEmolumentList(officialRankEmolument);
    }

    /**
     * 通过ID获取职级体系表
     *
     * @param officialRankSystemId 职级ID
     * @return List
     */
    private OfficialRankSystemDTO getOfficialRankSystemById(Long officialRankSystemId) {
        R<OfficialRankSystemDTO> listR = officialRankSystemService.selectById(officialRankSystemId, SecurityConstants.INNER);
        OfficialRankSystemDTO officialRankSystemDTO = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        return officialRankSystemDTO;
    }

    /**
     * 远程获取职级体系表
     *
     * @return List
     */
    private List<OfficialRankSystemDTO> getOfficialRankSystemDTOS() {
        R<List<OfficialRankSystemDTO>> listR = officialRankSystemService.selectAll(SecurityConstants.INNER);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        return officialRankSystemDTOS;
    }

    /**
     * 新增职级薪酬表
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 结果
     */
    @Override
    public OfficialRankEmolumentDTO insertOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
        BeanUtils.copyProperties(officialRankEmolumentDTO, officialRankEmolument);
        officialRankEmolument.setCreateBy(SecurityUtils.getUserId());
        officialRankEmolument.setCreateTime(DateUtils.getNowDate());
        officialRankEmolument.setUpdateTime(DateUtils.getNowDate());
        officialRankEmolument.setUpdateBy(SecurityUtils.getUserId());
        officialRankEmolument.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        officialRankEmolumentMapper.insertOfficialRankEmolument(officialRankEmolument);
        officialRankEmolumentDTO.setOfficialRankEmolumentId(officialRankEmolument.getOfficialRankEmolumentId());
        return officialRankEmolumentDTO;
    }

    /**
     * 修改职级薪酬表
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 结果
     */
    @Override
    public int updateOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        Long officialRankSystemId = officialRankEmolumentDTO.getOfficialRankSystemId();
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级等级ID为空");
        }
        List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = officialRankEmolumentDTO.getOfficialRankEmolumentDTOList();
        List<OfficialRankEmolumentDTO> officialRankEmolumentDTOS = officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemId(officialRankSystemId);
        if (StringUtils.isEmpty(officialRankEmolumentDTOList)) {
            throw new ServiceException("当前的职级薪酬表为空 请先配置");
        }
        if (StringUtils.isEmpty(officialRankEmolumentDTOS)) {// 新增
            for (OfficialRankEmolumentDTO rankEmolumentDTO : officialRankEmolumentDTOList) {
                rankEmolumentDTO.setOfficialRankSystemId(officialRankSystemId);
            }
            return insertOfficialRankEmoluments(officialRankEmolumentDTOList);
        }
        // 处理脏数据-新增
        List<OfficialRankEmolumentDTO> addRankEmolumentDTOList = new ArrayList<>();
        for (int i = officialRankEmolumentDTOList.size() - 1; i >= 0; i--) {
            OfficialRankEmolumentDTO emolumentDTO = officialRankEmolumentDTOList.get(i);
            if (StringUtils.isNull(emolumentDTO.getOfficialRankEmolumentId())) {
                emolumentDTO.setOfficialRankSystemId(officialRankSystemId);
                addRankEmolumentDTOList.add(emolumentDTO);
                officialRankEmolumentDTOList.remove(i);
            }
        }
        if (StringUtils.isNotEmpty(addRankEmolumentDTOList)) {
            insertOfficialRankEmoluments(addRankEmolumentDTOList);
        }
        // 更新
        List<OfficialRankEmolumentDTO> officialRankEmolumentList = new ArrayList<>();
        for (OfficialRankEmolumentDTO rankEmolumentDTO : officialRankEmolumentDTOList) {
            OfficialRankEmolumentDTO emolumentDTO = new OfficialRankEmolumentDTO();
            emolumentDTO.setOfficialRankEmolumentId(rankEmolumentDTO.getOfficialRankEmolumentId());
            emolumentDTO.setSalaryCap(rankEmolumentDTO.getSalaryCap());
            emolumentDTO.setSalaryFloor(rankEmolumentDTO.getSalaryFloor());
            emolumentDTO.setSalaryMedian(rankEmolumentDTO.getSalaryMedian());
            officialRankEmolumentList.add(emolumentDTO);
        }
        if (StringUtils.isNotEmpty(officialRankEmolumentList)) {
            updateOfficialRankEmoluments(officialRankEmolumentList);//批量更新
        }
        return 1;
    }

    /**
     * 逻辑批量删除职级薪酬表
     *
     * @param officialRankEmolumentIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteOfficialRankEmolumentByOfficialRankEmolumentIds(List<Long> officialRankEmolumentIds) {
        return officialRankEmolumentMapper.logicDeleteOfficialRankEmolumentByOfficialRankEmolumentIds(officialRankEmolumentIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除职级薪酬表信息
     *
     * @param officialRankEmolumentId 职级薪酬表主键
     * @return 结果
     */
    @Override
    public int deleteOfficialRankEmolumentByOfficialRankEmolumentId(Long officialRankEmolumentId) {
        return officialRankEmolumentMapper.deleteOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentId);
    }

    /**
     * 逻辑删除职级薪酬表信息
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 结果
     */
    @Override
    public int logicDeleteOfficialRankEmolumentByOfficialRankEmolumentId(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
        officialRankEmolument.setOfficialRankEmolumentId(officialRankEmolumentDTO.getOfficialRankEmolumentId());
        officialRankEmolument.setUpdateTime(DateUtils.getNowDate());
        officialRankEmolument.setUpdateBy(SecurityUtils.getUserId());
        return officialRankEmolumentMapper.logicDeleteOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolument);
    }

    /**
     * 物理删除职级薪酬表信息
     *
     * @param officialRankEmolumentDTO 职级薪酬表
     * @return 结果
     */

    @Override
    public int deleteOfficialRankEmolumentByOfficialRankEmolumentId(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
        BeanUtils.copyProperties(officialRankEmolumentDTO, officialRankEmolument);
        return officialRankEmolumentMapper.deleteOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolument.getOfficialRankEmolumentId());
    }

    /**
     * 物理批量删除职级薪酬表
     *
     * @param officialRankEmolumentDtos 需要删除的职级薪酬表主键
     * @return 结果
     */

    @Override
    public int deleteOfficialRankEmolumentByOfficialRankEmolumentIds(List<OfficialRankEmolumentDTO> officialRankEmolumentDtos) {
        List<Long> stringList = new ArrayList<>();
        for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDtos) {
            stringList.add(officialRankEmolumentDTO.getOfficialRankEmolumentId());
        }
        return officialRankEmolumentMapper.deleteOfficialRankEmolumentByOfficialRankEmolumentIds(stringList);
    }

    /**
     * 批量新增职级薪酬表信息
     *
     * @param officialRankEmolumentDtos 职级薪酬表对象
     */

    public int insertOfficialRankEmoluments(List<OfficialRankEmolumentDTO> officialRankEmolumentDtos) {
        List<OfficialRankEmolument> officialRankEmolumentList = new ArrayList<>();

        for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDtos) {
            OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
            BeanUtils.copyProperties(officialRankEmolumentDTO, officialRankEmolument);
            officialRankEmolument.setCreateBy(SecurityUtils.getUserId());
            officialRankEmolument.setCreateTime(DateUtils.getNowDate());
            officialRankEmolument.setUpdateTime(DateUtils.getNowDate());
            officialRankEmolument.setUpdateBy(SecurityUtils.getUserId());
            officialRankEmolument.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            officialRankEmolumentList.add(officialRankEmolument);
        }
        return officialRankEmolumentMapper.batchOfficialRankEmolument(officialRankEmolumentList);
    }

    /**
     * 批量修改职级薪酬表信息
     *
     * @param officialRankEmolumentDtos 职级薪酬表对象
     */

    public int updateOfficialRankEmoluments(List<OfficialRankEmolumentDTO> officialRankEmolumentDtos) {
        List<OfficialRankEmolument> officialRankEmolumentList = new ArrayList<>();

        for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDtos) {
            OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
            BeanUtils.copyProperties(officialRankEmolumentDTO, officialRankEmolument);
            officialRankEmolument.setCreateBy(SecurityUtils.getUserId());
            officialRankEmolument.setCreateTime(DateUtils.getNowDate());
            officialRankEmolument.setUpdateTime(DateUtils.getNowDate());
            officialRankEmolument.setUpdateBy(SecurityUtils.getUserId());
            officialRankEmolumentList.add(officialRankEmolument);
        }
        return officialRankEmolumentMapper.updateOfficialRankEmoluments(officialRankEmolumentList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importOfficialRankEmolument(List<OfficialRankEmolumentExcel> list) {
        List<OfficialRankEmolument> officialRankEmolumentList = new ArrayList<>();
        list.forEach(l -> {
            OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
            BeanUtils.copyProperties(l, officialRankEmolument);
            officialRankEmolument.setCreateBy(SecurityUtils.getUserId());
            officialRankEmolument.setCreateTime(DateUtils.getNowDate());
            officialRankEmolument.setUpdateTime(DateUtils.getNowDate());
            officialRankEmolument.setUpdateBy(SecurityUtils.getUserId());
            officialRankEmolument.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            officialRankEmolumentList.add(officialRankEmolument);
        });
        try {
            officialRankEmolumentMapper.batchOfficialRankEmolument(officialRankEmolumentList);
        } catch (Exception e) {
            throw new ServiceException("导入职级薪酬表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param officialRankEmolumentDTO excel
     * @return List
     */
    @Override
    public List<OfficialRankEmolumentExcel> exportOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        OfficialRankEmolument officialRankEmolument = new OfficialRankEmolument();
        BeanUtils.copyProperties(officialRankEmolumentDTO, officialRankEmolument);
        List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = officialRankEmolumentMapper.selectOfficialRankEmolumentList(officialRankEmolument);
        return new ArrayList<>();
    }

    /**
     * 查看该职级的分解信息
     *
     * @param officialEmolumentDTO 职级体系信息
     * @return List
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialDecomposeList(OfficialRankEmolumentDTO officialEmolumentDTO) {
        Long officialRankSystemId = officialEmolumentDTO.getOfficialRankSystemId();
        Integer officialRank = officialEmolumentDTO.getOfficialRank();// 职级
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系ID不能为空");
        }
        if (StringUtils.isNull(officialRank)) {
            throw new ServiceException("职级不能为空");
        }
        R<OfficialRankSystemDTO> listR = officialRankSystemService.selectById(officialRankSystemId, SecurityConstants.INNER);
        OfficialRankSystemDTO officialRankSystemDTO = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        if (StringUtils.isNull(officialRankSystemDTO)) {
            throw new ServiceException("当前职级不存在 请联系管理员");
        }
        Integer rankDecomposeDimension = officialRankSystemDTO.getRankDecomposeDimension();
        if (StringUtils.isNull(rankDecomposeDimension)) {
            return null;
        }
        R<List<OfficialRankDecomposeDTO>> officialDecomposeR = officialRankSystemService.selectOfficialDecomposeBySystemId(officialRankSystemId, rankDecomposeDimension, SecurityConstants.INNER);
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = officialDecomposeR.getData();
        if (officialDecomposeR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        // 获取当前的内容
        OfficialRankEmolumentDTO officialRankEmolumentDTO = officialRankEmolumentMapper.selectOfficialRankEmolumentByRank(officialRankSystemId, officialRank);
        if (StringUtils.isNull(officialRankEmolumentDTO)) {
            for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                officialRankDecomposeDTO.setSalaryCap(BigDecimal.ZERO);
                officialRankDecomposeDTO.setSalaryFloor(BigDecimal.ZERO);
                officialRankDecomposeDTO.setSalaryMedian(BigDecimal.ZERO);
                officialRankDecomposeDTO.setSalaryWide(BigDecimal.ZERO);
            }
            return officialRankDecomposeDTOS;
        }
        BigDecimal salaryCap = officialRankEmolumentDTO.getSalaryCap();
        BigDecimal salaryFloor = officialRankEmolumentDTO.getSalaryFloor();
        BigDecimal salaryMedian = officialRankEmolumentDTO.getSalaryMedian();
        BigDecimal salaryWide = officialRankEmolumentDTO.getSalaryCap().subtract(officialRankEmolumentDTO.getSalaryFloor());
        // 宽幅
        BigDecimal wide = officialRankEmolumentDTO.getSalaryCap().subtract(officialRankEmolumentDTO.getSalaryFloor());
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
            BigDecimal salaryFactor = officialRankDecomposeDTO.getSalaryFactor();
            officialRankDecomposeDTO.setSalaryCap(salaryCap.multiply(salaryFactor));
            officialRankDecomposeDTO.setSalaryFloor(salaryFloor.multiply(salaryFactor));
            officialRankDecomposeDTO.setSalaryMedian(salaryMedian.multiply(salaryFactor));
            officialRankDecomposeDTO.setSalaryWide(salaryWide.multiply(salaryFactor));
        }
        return officialRankDecomposeDTOS;
    }
}

