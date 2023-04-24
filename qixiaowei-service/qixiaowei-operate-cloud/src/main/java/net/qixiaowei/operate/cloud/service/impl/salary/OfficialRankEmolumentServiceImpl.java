package net.qixiaowei.operate.cloud.service.impl.salary;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.common.utils.uuid.IdUtils;
import net.qixiaowei.integration.redis.service.RedisService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;


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

    @Autowired
    private RedisService redisService;


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
                officialRankEmolumentDTO.setOfficialRankSystemId(officialRankSystemId);
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
                officialRankEmolumentDTO.setOfficialRankSystemId(officialRankSystemId);
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
    @Transactional
    public int updateOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        Long officialRankSystemId = officialRankEmolumentDTO.getOfficialRankSystemId();
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级等级ID为空");
        }
        List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = officialRankEmolumentDTO.getOfficialRankEmolumentDTOList();
        if (StringUtils.isEmpty(officialRankEmolumentDTOList)) {
            throw new ServiceException("当前的职级薪酬表为空 请先配置");
        }
        for (OfficialRankEmolumentDTO emolumentDTO : officialRankEmolumentDTOList) {
            if (Optional.ofNullable(emolumentDTO.getSalaryCap()).orElse(BigDecimal.ZERO)
                    .compareTo(Optional.ofNullable(emolumentDTO.getSalaryFloor()).orElse(BigDecimal.ZERO)) < 0) {
                throw new ServiceException("工资上限应大于等于工资下限");
            }
            if (Optional.ofNullable(emolumentDTO.getSalaryCap()).orElse(BigDecimal.ZERO).compareTo(Optional.ofNullable(emolumentDTO.getSalaryMedian()).orElse(BigDecimal.ZERO)) < 0
                    || Optional.ofNullable(emolumentDTO.getSalaryFloor()).orElse(BigDecimal.ZERO).compareTo(Optional.ofNullable(emolumentDTO.getSalaryMedian()).orElse(BigDecimal.ZERO)) > 0) {
                throw new ServiceException("工资中位值应在工资上下限范围内");
            }
        }
        List<OfficialRankEmolumentDTO> officialRankEmolumentDTOS = officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemId(officialRankSystemId);
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

    @Override
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

    @Override
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
     * @param officialRankEmolumentDTO 职级DTO
     * @param file                     文件
     */
    @Override
    public Map<Object, Object> importOfficialRankEmolument(OfficialRankEmolumentDTO officialRankEmolumentDTO, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        Long officialRankSystemId = officialRankEmolumentDTO.getOfficialRankSystemId();
        if (StringUtils.isNull(officialRankSystemId)) {
            throw new ServiceException("职级等级ID为空");
        }
        OfficialRankSystemDTO officialRankSystemById = getOfficialRankSystemById(officialRankSystemId);
        try {
            List<Object> errorExcelList = new ArrayList<>();
            List<Map<Integer, String>> successExcels = new ArrayList<>();
            List<Map<Integer, String>> listMap = getMaps(file);
            for (Map<Integer, String> map : listMap) {
                StringBuilder errorNote = new StringBuilder();
                // 判断小数点后2位的数字的正则表达式
                if (!ExcelUtils.isNumber(map.get(1))) {
                    errorNote.append("工资上限格式不正确；");
                }
                if (!ExcelUtils.isNumber(map.get(2))) {
                    errorNote.append("工资下限格式不正确；");
                }
                if (!ExcelUtils.isNumber(map.get(3))) {
                    errorNote.append("工资中位数格式不正确；");
                }
                if (errorNote.length() == 0) {
                    BigDecimal salaryCap = new BigDecimal(Optional.ofNullable(map.get(1)).orElse("0"));
                    BigDecimal salaryFloor = new BigDecimal(Optional.ofNullable(map.get(2)).orElse("0"));
                    BigDecimal salaryMedian = new BigDecimal(Optional.ofNullable(map.get(3)).orElse("0"));
                    String rankPrefixCode = officialRankSystemById.getRankPrefixCode();
                    if (!map.get(0).contains(rankPrefixCode)) {
                        errorNote.append("职级前缀不正确；");
                    }
                    if (salaryCap.compareTo(salaryFloor) < 0) {
                        errorNote.append("工资上限应大于等于工资下限；");
                    }
                    if (salaryCap.compareTo(salaryMedian) < 0 || salaryFloor.compareTo(salaryMedian) > 0) {
                        errorNote.append("工资中位值应在工资上下限范围内；");
                    }
                }
                if (errorNote.length() != 0) {
                    List<String> errorList = new ArrayList<>();
                    errorList.add(errorNote.substring(0, errorNote.length() - 1));
                    errorList.addAll(map.values());
                    errorExcelList.add(errorList);
                } else {
                    successExcels.add(map);
                }
            }
            List<Object> successExcelList = new ArrayList<>();
            if (StringUtils.isNotEmpty(successExcels)) {
                List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = new ArrayList<>();
                for (Map<Integer, String> map : successExcels) {
                    String rankPrefixCode = officialRankSystemById.getRankPrefixCode();
                    Integer rank = Integer.valueOf(map.get(0).replace(rankPrefixCode, ""));
                    OfficialRankEmolumentDTO emolumentDTO = new OfficialRankEmolumentDTO();
                    emolumentDTO.setOfficialRank(rank);
                    emolumentDTO.setOfficialRankSystemId(officialRankSystemById.getOfficialRankSystemId());
                    emolumentDTO.setSalaryCap(new BigDecimal(Optional.ofNullable(map.get(1)).orElse("0")));
                    emolumentDTO.setSalaryFloor(new BigDecimal(Optional.ofNullable(map.get(2)).orElse("0")));
                    emolumentDTO.setSalaryMedian(new BigDecimal(Optional.ofNullable(map.get(3)).orElse("0")));
                    officialRankEmolumentDTOList.add(emolumentDTO);
                    successExcelList.add(map);
                }
                List<OfficialRankEmolumentDTO> officialRankEmolumentDTOS = officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemId(officialRankSystemId);
                if (StringUtils.isEmpty(officialRankEmolumentDTOS)) {// 新增
                    this.insertOfficialRankEmoluments(officialRankEmolumentDTOList);
                } else {
                    for (OfficialRankEmolumentDTO emolumentDTO : officialRankEmolumentDTOList) {
                        for (OfficialRankEmolumentDTO rankEmolumentDTO : officialRankEmolumentDTOS) {
                            if (rankEmolumentDTO.getOfficialRank().equals(emolumentDTO.getOfficialRank())) {
                                emolumentDTO.setOfficialRankEmolumentId(rankEmolumentDTO.getOfficialRankEmolumentId());
                                break;
                            }
                        }
                    }
                    try {
                        this.updateOfficialRankEmoluments(officialRankEmolumentDTOList);//批量更新
                    } catch (Exception e) {
                        throw new ServiceException("更新绩效考核表Excel失败");
                    }
                }
            }
            String uuId;
            String simpleUUID = IdUtils.simpleUUID();
            if (StringUtils.isNotEmpty(errorExcelList)) {
                uuId = CacheConstants.ERROR_EXCEL_KEY + simpleUUID;
                redisService.setCacheObject(uuId, errorExcelList, 12L, TimeUnit.HOURS);
            }
            return ExcelUtils.parseExcelResult(successExcelList, errorExcelList, false, simpleUUID);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
    }

    /**
     * 获取list
     *
     * @param file 文件
     * @return 结果
     */
    private static List<Map<Integer, String>> getMaps(MultipartFile file) throws IOException {
        List<Map<Integer, String>> listMap;
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        listMap = read.sheet(0).doReadSync();
        if (StringUtils.isEmpty(listMap)) {
            throw new ServiceException("职级确定薪酬Excel没有数据 请检查");
        }
        String sheetName = EasyExcel.read(file.getInputStream()).build().excelExecutor().sheetList().get(0).getSheetName();
        if (StringUtils.equals(sheetName, "职级确定薪酬导入错误报告")) {
            Map<Integer, String> head = listMap.get(1);
            if (head.size() != 5) {
                throw new ServiceException("职级确定薪酬模板不正确 请检查");
            }
            List<Map<Integer, String>> objects = new ArrayList<>();
            for (Map<Integer, String> map1 : listMap) {
                Map<Integer, String> map2 = new TreeMap<>();
                for (int i = 1; i < map1.size(); i++) {
                    map2.put(i - 1, map1.get(i));
                }
                objects.add(map2);
            }
            listMap = objects;
        } else if (StringUtils.equals(sheetName, "职级确定薪酬导入")) {
            Map<Integer, String> head = listMap.get(1);
            if (head.size() != 4) {
                throw new ServiceException("职级确定薪酬模板不正确 请检查");
            }
        } else {
            throw new ServiceException("模板错误");
        }
        listMap.remove(1);
        listMap.remove(0);
        return listMap;
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

    /**
     * 根据岗位ID获取职级确定薪酬提示
     *
     * @param postId 岗位id
     * @return String
     */
    @Override
    public String selectByPostId(Long postId) {
        R<PostDTO> postDTOR = postService.selectPostByPostId(postId, SecurityConstants.INNER);
        if (postDTOR.getCode() != 200) {
            throw new ServiceException("远程获取岗位信息失败");
        }
        PostDTO postDTO = postDTOR.getData();
        if (StringUtils.isNull(postDTO)) {
            postDTO.getRankPrefixCode();
        }
        return null;
    }

    /**
     * 职级确定薪酬详情
     *
     * @param officialRankSystemId 职级体系ID
     * @param officialRank         职级
     * @return String
     */
    @Override
    public String officialRankInfo(Long officialRankSystemId, Integer officialRank) {
        OfficialRankEmolumentDTO emolumentDTO = officialRankEmolumentMapper.selectOfficialRankEmolumentByRank(officialRankSystemId, officialRank);
        if (StringUtils.isNull(emolumentDTO)) {
            throw new ServiceException("");
        }
        BigDecimal salaryMedian = emolumentDTO.getSalaryMedian();// 中位数
        BigDecimal salaryCap = emolumentDTO.getSalaryCap();// 上限
        BigDecimal salaryFloor = emolumentDTO.getSalaryFloor();// 下限
        StringBuilder sb = new StringBuilder("职级薪酬");
        sb.append(salaryFloor).append("-").append(salaryCap).append(",中位值").append(salaryMedian);
        return sb.toString();
    }

}

