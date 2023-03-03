package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightIndustry;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightIndustryMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightIndustryService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * MarketInsightIndustryService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-03-03
 */
@Service
public class MarketInsightIndustryServiceImpl implements IMarketInsightIndustryService {
    @Autowired
    private MarketInsightIndustryMapper marketInsightIndustryMapper;

    @Autowired
    private RemoteEmployeeService remoteEmployeeService;

    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteIndustryService remoteIndustryService;
    @Autowired
    private RemoteProductService remoteProductService;
    @Autowired
    private RemoteAreaService remoteAreaService;
    /**
     * 查询市场洞察行业表
     *
     * @param marketInsightIndustryId 市场洞察行业表主键
     * @return 市场洞察行业表
     */
    @Override
    public MarketInsightIndustryDTO selectMarketInsightIndustryByMarketInsightIndustryId(Long marketInsightIndustryId) {
        return marketInsightIndustryMapper.selectMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryId);
    }

    /**
     * 查询市场洞察行业表列表
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 市场洞察行业表
     */
    @Override
    public List<MarketInsightIndustryDTO> selectMarketInsightIndustryList(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        List<String> createByList = new ArrayList<>();
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        Map<String, Object> params = marketInsightIndustryDTO.getParams();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        this.queryemployeeName(params);
        marketInsightIndustry.setParams(params);
        if (StringUtils.isNotNull(marketInsightIndustryDTO.getCreateByName())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(marketInsightIndustryDTO.getCreateByName());
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> employeeIds = userListData.stream().map(UserDTO::getEmployeeId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(employeeIds)) {
                employeeIds.forEach(e -> {
                    createByList.add(String.valueOf(e));
                });
            } else {
                createByList.add("");
            }
        }
        marketInsightIndustry.setCreateBys(createByList);
        return marketInsightIndustryMapper.selectMarketInsightIndustryList(marketInsightIndustry);
    }

    /**
     * 封装高级查询人员id
     *
     * @param params
     */
    private void queryemployeeName(Map<String, Object> params) {
        Map<String, Object> params2 = new HashMap<>();
        if (StringUtils.isNotEmpty(params)) {
            for (String key : params.keySet()) {
                switch (key) {
                    case "createByNameEqual":
                        params2.put("employeeNameEqual", params.get("createByNameEqual"));
                        break;
                    case "createByNameNotEqual":
                        params2.put("employeeNameNotEqual", params.get("createByNameNotEqual"));
                        break;
                    case "createByNameLike":
                        params2.put("employeeNameLike", params.get("createByNameLike"));
                        break;
                    case "createByNameNotLike":
                        params2.put("employeeNameNotLike", params.get("createByNameNotLike"));
                        break;
                    default:
                        break;
                }
            }

            if (StringUtils.isNotEmpty(params2)) {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setParams(params2);
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    List<Long> employeeIds = data.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        params.put("createBys", employeeIds);
                    }
                }
            }
        }
    }

    /**
     * 新增市场洞察行业表
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */
    @Override
    public MarketInsightIndustryDTO insertMarketInsightIndustry(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        marketInsightIndustry.setCreateBy(SecurityUtils.getUserId());
        marketInsightIndustry.setCreateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
        marketInsightIndustry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        marketInsightIndustryMapper.insertMarketInsightIndustry(marketInsightIndustry);
        marketInsightIndustryDTO.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
        return marketInsightIndustryDTO;
    }

    /**
     * 修改市场洞察行业表
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */
    @Override
    public int updateMarketInsightIndustry(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightIndustryMapper.updateMarketInsightIndustry(marketInsightIndustry);
    }

    /**
     * 逻辑批量删除市场洞察行业表
     *
     * @param marketInsightIndustryIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteMarketInsightIndustryByMarketInsightIndustryIds(List<Long> marketInsightIndustryIds) {
        return marketInsightIndustryMapper.logicDeleteMarketInsightIndustryByMarketInsightIndustryIds(marketInsightIndustryIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除市场洞察行业表信息
     *
     * @param marketInsightIndustryId 市场洞察行业表主键
     * @return 结果
     */
    @Override
    public int deleteMarketInsightIndustryByMarketInsightIndustryId(Long marketInsightIndustryId) {
        return marketInsightIndustryMapper.deleteMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryId);
    }

    /**
     * 逻辑删除市场洞察行业表信息
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */
    @Override
    public int logicDeleteMarketInsightIndustryByMarketInsightIndustryId(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        marketInsightIndustry.setMarketInsightIndustryId(marketInsightIndustryDTO.getMarketInsightIndustryId());
        marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightIndustryMapper.logicDeleteMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustry);
    }

    /**
     * 物理删除市场洞察行业表信息
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */

    @Override
    public int deleteMarketInsightIndustryByMarketInsightIndustryId(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        return marketInsightIndustryMapper.deleteMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
    }

    /**
     * 物理批量删除市场洞察行业表
     *
     * @param marketInsightIndustryDtos 需要删除的市场洞察行业表主键
     * @return 结果
     */

    @Override
    public int deleteMarketInsightIndustryByMarketInsightIndustryIds(List<MarketInsightIndustryDTO> marketInsightIndustryDtos) {
        List<Long> stringList = new ArrayList();
        for (MarketInsightIndustryDTO marketInsightIndustryDTO : marketInsightIndustryDtos) {
            stringList.add(marketInsightIndustryDTO.getMarketInsightIndustryId());
        }
        return marketInsightIndustryMapper.deleteMarketInsightIndustryByMarketInsightIndustryIds(stringList);
    }

    /**
     * 批量新增市场洞察行业表信息
     *
     * @param marketInsightIndustryDtos 市场洞察行业表对象
     */

    public int insertMarketInsightIndustrys(List<MarketInsightIndustryDTO> marketInsightIndustryDtos) {
        List<MarketInsightIndustry> marketInsightIndustryList = new ArrayList();

        for (MarketInsightIndustryDTO marketInsightIndustryDTO : marketInsightIndustryDtos) {
            MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
            BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
            marketInsightIndustry.setCreateBy(SecurityUtils.getUserId());
            marketInsightIndustry.setCreateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
            marketInsightIndustry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            marketInsightIndustryList.add(marketInsightIndustry);
        }
        return marketInsightIndustryMapper.batchMarketInsightIndustry(marketInsightIndustryList);
    }

    /**
     * 批量修改市场洞察行业表信息
     *
     * @param marketInsightIndustryDtos 市场洞察行业表对象
     */

    public int updateMarketInsightIndustrys(List<MarketInsightIndustryDTO> marketInsightIndustryDtos) {
        List<MarketInsightIndustry> marketInsightIndustryList = new ArrayList();

        for (MarketInsightIndustryDTO marketInsightIndustryDTO : marketInsightIndustryDtos) {
            MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
            BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
            marketInsightIndustry.setCreateBy(SecurityUtils.getUserId());
            marketInsightIndustry.setCreateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
            marketInsightIndustryList.add(marketInsightIndustry);
        }
        return marketInsightIndustryMapper.updateMarketInsightIndustrys(marketInsightIndustryList);
    }
}

