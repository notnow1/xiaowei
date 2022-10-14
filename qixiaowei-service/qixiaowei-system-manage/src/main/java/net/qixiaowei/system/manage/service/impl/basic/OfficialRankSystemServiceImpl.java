package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankSystem;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.mapper.basic.OfficialRankSystemMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.service.basic.IOfficialRankDecomposeService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * OfficialRankSystemService业务层处理
 *
 * @author Graves
 * @since 2022-10-07
 */
@Service
public class OfficialRankSystemServiceImpl implements IOfficialRankSystemService {
    @Autowired
    private OfficialRankSystemMapper officialRankSystemMapper;

    @Autowired
    private IOfficialRankDecomposeService officialRankDecomposeService;

    @Autowired
    private PostMapper postMapper;

    /**
     * 查询职级体系表
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 职级体系表
     */
    @Override
    public OfficialRankSystemDTO selectOfficialRankSystemByOfficialRankSystemId(Long officialRankSystemId) {
        return officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
    }

    /**
     * 查询职级体系表列表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 职级体系表
     */
    @Override
    public List<OfficialRankSystemDTO> selectOfficialRankSystemList(OfficialRankSystemDTO officialRankSystemDTO) {
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = officialRankSystemMapper.selectOfficialRankSystemList(officialRankSystem);
        writeOfficialRank(officialRankSystemDTOS);
        return officialRankSystemDTOS;
    }


    /**
     * 分页查询list
     *
     * @param officialRankSystemDTO
     * @return
     */
    @Override
    public List<OfficialRankSystemDTO> selectOfficialRankSystemPageList(OfficialRankSystemDTO officialRankSystemDTO) {
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = officialRankSystemMapper.selectOfficialRankSystemList(officialRankSystem);
        writeOfficialRank(officialRankSystemDTOS);
        return officialRankSystemDTOS;
    }

    /**
     * 拼接职级字段
     *
     * @param officialRankSystemDTOS
     */
    private void writeOfficialRank(List<OfficialRankSystemDTO> officialRankSystemDTOS) {
        String rankPrefixCode;
        int rankStart;
        int rankEnd;
        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDTOS) {
            StringBuilder officialRank = new StringBuilder("");
            rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
            rankStart = officialRankSystemDTO.getRankStart();
            rankEnd = officialRankSystemDTO.getRankEnd();
            while (rankStart < rankEnd) {
                officialRank.append(rankPrefixCode).append(rankStart).append(",");
                rankStart++;
            }
            officialRank.append(rankPrefixCode).append(rankEnd);
            officialRankSystemDTO.setOfficialRank(officialRank.toString());
        }
    }

    /**
     * 新增职级体系表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOfficialRankSystem(OfficialRankSystemDTO officialRankSystemDTO) {
        String officialRankSystemName = officialRankSystemDTO.getOfficialRankSystemName();
        Integer rankStart = officialRankSystemDTO.getRankStart();
        Integer rankEnd = officialRankSystemDTO.getRankEnd();
        String rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = officialRankSystemDTO
                .getOfficialRankDecomposeDTOS();
        if (StringUtils.isEmpty(officialRankSystemName)) {
            throw new ServiceException("职级体系名称不能为空");
        }
        if (StringUtils.isNull(rankStart)) {
            throw new ServiceException("职级体系起始级别不能为空");
        }
        if (StringUtils.isNull(rankEnd)) {
            throw new ServiceException("职级体系终止级别不能为空");
        }
        if (rankEnd < rankStart) {
            throw new ServiceException("职级体系终止级别不能小于职级初始级别");
        }
        if (StringUtils.isEmpty(rankPrefixCode)) {
            throw new ServiceException("职级体系级别前缀不能为空");
        }
        int officialRankNameCheckUnique = officialRankSystemMapper.officialRankNameCheckUnique(officialRankSystemName);
        int rankPrefixCodeCheckUnique = officialRankSystemMapper.rankPrefixCodeCheckUnique(rankPrefixCode);
        if (officialRankNameCheckUnique > 0) {
            throw new ServiceException("职级体系名称重复");
        }
        if (rankPrefixCodeCheckUnique > 0) {
            throw new ServiceException("职级体系级别前缀重复");
        }
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        officialRankSystem.setCreateBy(SecurityUtils.getUserId());
        officialRankSystem.setCreateTime(DateUtils.getNowDate());
        officialRankSystem.setUpdateTime(DateUtils.getNowDate());
        officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
        officialRankSystem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            officialRankSystemMapper.insertOfficialRankSystem(officialRankSystem);
            if (StringUtils.isNotEmpty(officialRankDecomposeDTOS)) {
                officialRankDecomposeService.insertOfficialRankDecomposes(officialRankDecomposeDTOS, officialRankSystem);// 新增操作
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 修改职级体系表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOfficialRankSystem(OfficialRankSystemDTO officialRankSystemDTO) {
        Long officialRankSystemId = officialRankSystemDTO.getOfficialRankSystemId();
        String officialRankSystemName = officialRankSystemDTO.getOfficialRankSystemName();
        Integer rankStart = officialRankSystemDTO.getRankStart();
        Integer rankEnd = officialRankSystemDTO.getRankEnd();
        String rankPrefixCode = officialRankSystemDTO.getRankPrefixCode();
        Integer rankDecomposeDimensionAfter = officialRankSystemDTO.getRankDecomposeDimension();
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOAfter = officialRankSystemDTO
                .getOfficialRankDecomposeDTOS();
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系表id不能为空");
        }
        if (StringUtils.isNotEmpty(officialRankSystemName)) {
            throw new ServiceException("职级体系名称不能进行编辑");
        }
        if (StringUtils.isNull(rankStart)) {
            throw new ServiceException("职级体系起始级别不能为空");
        }
        if (StringUtils.isNull(rankEnd)) {
            throw new ServiceException("职级体系终止级别不能为空");
        }
        if (rankEnd < rankStart) {
            throw new ServiceException("职级体系终止级别不能小于职级初始级别");
        }
        if (StringUtils.isEmpty(rankPrefixCode)) {
            throw new ServiceException("职级体系级别前缀不能为空");
        }
        int officialRankNameCheckUnique = officialRankSystemMapper.officialRankNameCheckUnique(officialRankSystemName);
        int rankPrefixCodeCheckUnique = officialRankSystemMapper.rankPrefixCodeCheckUnique(rankPrefixCode);
        if (officialRankNameCheckUnique > 0) {
            throw new ServiceException("职级体系名称重复");
        }
        if (rankPrefixCodeCheckUnique > 0) {
            throw new ServiceException("职级体系级别前缀重复");
        }
        // 分解为度校验
        List<Integer> decomposeDimensions = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOAfter) {
            Integer decomposeDimension = officialRankDecomposeDTO.getDecomposeDimension();
            if (decomposeDimensions.contains(decomposeDimension)) {
                throw new ServiceException("分解维度不能重复");
            }
            decomposeDimensions.add(decomposeDimension);
        }
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        officialRankSystem.setUpdateTime(DateUtils.getNowDate());
        officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
        officialRankSystemMapper.updateOfficialRankSystem(officialRankSystem);
        // ~先判断officialRankSystemDTO中的officialRankDecomposeDTOS是否为空，若是不为空则进行以下操作
        // 1需要根据officialRankSystemId查询之前的数据
        // 2判断分解rankDecomposeDimension有没有改动
        // 3.1没有改动直接进行operate，operate需要进行跟获取到的BeforeDTO进行取交集和差集
        // 3.2改动了进行新增，并根据officialRankSystemId和rankDecomposeDimension删除之前在official_rank_decompose中的数据
        if (StringUtils.isEmpty(officialRankDecomposeDTOAfter)) {
            return 1;
        }
        Integer rankDecomposeDimensionBefore = officialRankSystem.getRankDecomposeDimension();
        if (rankDecomposeDimensionAfter.equals(rankDecomposeDimensionBefore)) {// 操作
            List<OfficialRankDecomposeDTO> officialRankDecomposeDTOSBefore =
                    officialRankDecomposeService.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId.toString());
            return operateRankDecompose(officialRankDecomposeDTOSBefore, officialRankDecomposeDTOAfter, officialRankSystem);
        } else {// 新增
            officialRankSystem.setRankDecomposeDimension(rankDecomposeDimensionAfter);
            officialRankDecomposeService.insertOfficialRankDecomposes(officialRankDecomposeDTOAfter, officialRankSystem);
            officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankDecompose(officialRankSystemId, rankDecomposeDimensionBefore);
            return 1;
        }
    }

    /**
     * 更新操作
     *
     * @param officialRankDecomposeDTOSBefore
     * @param officialRankDecomposeDTOAfter
     * @return
     */
    private int operateRankDecompose(List<OfficialRankDecomposeDTO> officialRankDecomposeDTOSBefore, List<OfficialRankDecomposeDTO> officialRankDecomposeDTOAfter, OfficialRankSystem officialRankSystem) {
        // 交集
        List<OfficialRankDecomposeDTO> updateOfficialRankDecompose =
                officialRankDecomposeDTOAfter.stream().filter(officialRankDecomposeDTO ->
                        officialRankDecomposeDTOSBefore.stream().map(OfficialRankDecomposeDTO::getOfficialRankDecomposeId)
                                .collect(Collectors.toList()).contains(officialRankDecomposeDTO.getOfficialRankDecomposeId())
                ).collect(Collectors.toList());
        // 差集 Before中After的补集
        List<OfficialRankDecomposeDTO> delOfficialRankDecompose =
                officialRankDecomposeDTOSBefore.stream().filter(officialRankDecomposeDTO ->
                        !officialRankDecomposeDTOAfter.stream().map(OfficialRankDecomposeDTO::getOfficialRankDecomposeId)
                                .collect(Collectors.toList()).contains(officialRankDecomposeDTO.getOfficialRankDecomposeId())
                ).collect(Collectors.toList());
        // 差集 After中Before的补集
        List<OfficialRankDecomposeDTO> addOfficialRankDecompose =
                officialRankDecomposeDTOAfter.stream().filter(officialRankDecomposeDTO ->
                        !officialRankDecomposeDTOSBefore.stream().map(OfficialRankDecomposeDTO::getOfficialRankDecomposeId)
                                .collect(Collectors.toList()).contains(officialRankDecomposeDTO.getOfficialRankDecomposeId())
                ).collect(Collectors.toList());
        try {
            if (StringUtils.isNotEmpty(addOfficialRankDecompose)) {
                officialRankDecomposeService.insertOfficialRankDecomposes(addOfficialRankDecompose, officialRankSystem);
            }
            if (StringUtils.isNotEmpty(updateOfficialRankDecompose)) {
                officialRankDecomposeService.updateOfficialRankDecomposes(updateOfficialRankDecompose, officialRankSystem);
            }
            if (StringUtils.isNotEmpty(delOfficialRankDecompose)) {
                officialRankDecomposeService.deleteOfficialRankDecomposeByOfficialRankDecomposeIds(delOfficialRankDecompose);
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.toString());
        }
        return 1;
    }

    /**
     * 逻辑批量删除职级体系表
     *
     * @param officialRankSystemIds 需要删除的职级体系表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteOfficialRankSystemByOfficialRankSystemIds(List<Long> officialRankSystemIds) {
        if (StringUtils.isEmpty(officialRankSystemIds)) {
            throw new ServiceException("职级体系id列表不能为空");
        }
        List<OfficialRankSystemDTO> existByOfficialRankSystemDTOS = officialRankSystemMapper.isExistByOfficialRankSystemIds(officialRankSystemIds);
        if (existByOfficialRankSystemDTOS.size() < officialRankSystemIds.size()) {
            throw new ServiceException("当前的职级体系已不存在");
        }
        List<PostDTO> quote = isQuote(officialRankSystemIds);
        // todo 引用
        if (StringUtils.isNotEmpty(quote)) {
            throw new ServiceException("当前的职级体系正在被引用");
        }
        int i = officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankSystemIds(officialRankSystemIds);
        if (i == 0) {
            throw new ServiceException("删除失败，请检查相关联的的职级分解是否正常");
        }
        return officialRankSystemMapper.logicDeleteOfficialRankSystemByOfficialRankSystemIds(officialRankSystemIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * todo 职级体系引用校验
     *
     * @param officialRankSystemIds
     * @return
     */
    private List<PostDTO> isQuote(List<Long> officialRankSystemIds) {
//        return postMapper.selectPostByOfficialRankSystemIds(officialRankSystemIds);
        return null;
    }

    /**
     * 物理删除职级体系表信息
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankSystemByOfficialRankSystemId(Long officialRankSystemId) {
        return officialRankSystemMapper.deleteOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
    }

    /**
     * 职级体系表详情
     *
     * @param officialRankSystemId
     * @return
     */
    @Override
    public OfficialRankSystemDTO detailOfficialRankSystem(Long officialRankSystemId) {
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系ID不能为空");
        }
        OfficialRankSystemDTO isExist = officialRankSystemMapper.isExistByOfficialRankSystemId(officialRankSystemId);
        if (StringUtils.isNull(isExist)) {
            throw new ServiceException("该职级体系已不存在");
        }
        return officialRankSystemMapper.selectOfficialRankSystemByOfficialRankSystemId(officialRankSystemId);
//        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = officialRankDecomposeService.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId.toString());
    }

    /**
     * 逻辑删除职级体系表信息
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteOfficialRankSystemByOfficialRankSystemId(OfficialRankSystemDTO officialRankSystemDTO) {
        Long officialRankSystemId = officialRankSystemDTO.getOfficialRankSystemId();
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级体系id不能为空");
        }
        if (StringUtils.isNull(officialRankSystemMapper.isExistByOfficialRankSystemId(officialRankSystemId))) {
            throw new ServiceException("当前的职级体系已不存在");
        }
        List<Long> officialRankSystemIds = new ArrayList<>();
        List<PostDTO> quote = isQuote(officialRankSystemIds);
        if (StringUtils.isNotEmpty(quote)) {
            StringBuffer stringBuffer = new StringBuffer(quote.get(0).getOfficialRankSystemName());
//            if (quote){
//
//            }
//                for (PostDTO postDTO : quote) {
//
//                }
//            stringBuffer.append()
            throw new ServiceException(stringBuffer + "职级体系正在被引用");
        }
        int i = officialRankDecomposeService.logicDeleteOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
        if (i == 0) {
            throw new ServiceException("删除失败，请检查相关联的的职级分解是否正常");
        }
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        return officialRankSystemMapper.logicDeleteOfficialRankSystemByOfficialRankSystemId(officialRankSystem, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除职级体系表信息
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankSystemByOfficialRankSystemId(OfficialRankSystemDTO officialRankSystemDTO) {
        OfficialRankSystem officialRankSystem = new OfficialRankSystem();
        BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
        return officialRankSystemMapper.deleteOfficialRankSystemByOfficialRankSystemId(officialRankSystem.getOfficialRankSystemId());
    }

    /**
     * 物理批量删除职级体系表
     *
     * @param officialRankSystemDtos 需要删除的职级体系表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankSystemByOfficialRankSystemIds(List<OfficialRankSystemDTO> officialRankSystemDtos) {
        List<Long> stringList = new ArrayList<>();
        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDtos) {
            stringList.add(officialRankSystemDTO.getOfficialRankSystemId());
        }
        return officialRankSystemMapper.deleteOfficialRankSystemByOfficialRankSystemIds(stringList);
    }

    /**
     * 批量新增职级体系表信息
     *
     * @param officialRankSystemDtos 职级体系表对象
     */
    @Transactional
    public int insertOfficialRankSystems(List<OfficialRankSystemDTO> officialRankSystemDtos) {
        List<OfficialRankSystem> officialRankSystemList = new ArrayList<>();

        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDtos) {
            OfficialRankSystem officialRankSystem = new OfficialRankSystem();
            BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
            officialRankSystem.setCreateBy(SecurityUtils.getUserId());
            officialRankSystem.setCreateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
            officialRankSystem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            officialRankSystemList.add(officialRankSystem);
        }
        return officialRankSystemMapper.batchOfficialRankSystem(officialRankSystemList);
    }

    /**
     * 批量修改职级体系表信息
     *
     * @param officialRankSystemDtos 职级体系表对象
     */
    @Transactional
    public int updateOfficialRankSystems(List<OfficialRankSystemDTO> officialRankSystemDtos) {
        List<OfficialRankSystem> officialRankSystemList = new ArrayList<>();

        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDtos) {
            OfficialRankSystem officialRankSystem = new OfficialRankSystem();
            BeanUtils.copyProperties(officialRankSystemDTO, officialRankSystem);
            officialRankSystem.setCreateBy(SecurityUtils.getUserId());
            officialRankSystem.setCreateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateTime(DateUtils.getNowDate());
            officialRankSystem.setUpdateBy(SecurityUtils.getUserId());
            officialRankSystemList.add(officialRankSystem);
        }
        return officialRankSystemMapper.updateOfficialRankSystems(officialRankSystemList);
    }
}

