package net.qixiaowei.strategy.cloud.service.impl.strategyIntent;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyIntent.StrategyIntent;
import net.qixiaowei.strategy.cloud.api.domain.strategyIntent.StrategyIntentOperate;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentOperateDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentOperateMapDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyIntent.StrategyIntentMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyIntent.StrategyIntentOperateMapper;
import net.qixiaowei.strategy.cloud.service.strategyIntent.IStrategyIntentService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * StrategyIntentService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-02-23
 */
@Service
public class StrategyIntentServiceImpl implements IStrategyIntentService {
    @Autowired
    private StrategyIntentMapper strategyIntentMapper;
    @Autowired
    private StrategyIntentOperateMapper strategyIntentOperateMapperr;
    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;

    /**
     * 查询战略意图表
     *
     * @param strategyIntentId 战略意图表主键
     * @return 战略意图表
     */
    @Override
    public StrategyIntentDTO selectStrategyIntentByStrategyIntentId(Long strategyIntentId) {
        StrategyIntentDTO strategyIntentDTO = strategyIntentMapper.selectStrategyIntentByStrategyIntentId(strategyIntentId);
        if (StringUtils.isNull(strategyIntentDTO)) {
            throw new ServiceException("数据不存在！ 请刷新页面重试！");
        }
        //返回战略意图经营数据集合 (需处理)
        List<StrategyIntentOperateDTO> strategyIntentOperateListData = new ArrayList<>();
        //数据库存在数据
        List<StrategyIntentOperateDTO> strategyIntentOperateDTOS = strategyIntentOperateMapperr.selectStrategyIntentOperateByStrategyIntentId(strategyIntentId);
        if (StringUtils.isNotEmpty(strategyIntentOperateDTOS)) {
            List<Long> indicatorIds = strategyIntentOperateDTOS.stream().filter(f -> null != f.getIndicatorId()).map(StrategyIntentOperateDTO::getIndicatorId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(indicatorIds)){
                R<List<IndicatorDTO>> IndicatorDTOList = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
                List<IndicatorDTO> data = IndicatorDTOList.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (StrategyIntentOperateDTO strategyIntentOperateDTO : strategyIntentOperateDTOS) {
                        for (IndicatorDTO datum : data) {
                            if (strategyIntentOperateDTO.getIndicatorId().equals(datum.getIndicatorId())){
                                strategyIntentOperateDTO.setIndicatorName(datum.getIndicatorName());
                                strategyIntentOperateDTO.setIndicatorValueType(datum.getIndicatorValueType());
                            }
                        }
                    }
                }
            }

            Map<Long, List<StrategyIntentOperateDTO>> indicatorMap = strategyIntentOperateDTOS.parallelStream().filter(f -> f.getIndicatorId() != null).collect(Collectors.groupingBy(StrategyIntentOperateDTO::getIndicatorId,LinkedHashMap::new, Collectors.toList()));
            for (Long key : indicatorMap.keySet()) {
                StrategyIntentOperateDTO strategyIntentOperateDTOData = new StrategyIntentOperateDTO();
                //年度指标对应值集合
                List<StrategyIntentOperateMapDTO> strategyIntentOperateMapDTOS = new ArrayList<>();
                //根据指标id分组
                List<StrategyIntentOperateDTO> strategyIntentOperateDTOList = indicatorMap.get(key);
                if (StringUtils.isNotEmpty(strategyIntentOperateDTOList)) {
                    for (StrategyIntentOperateDTO strategyIntentOperateDTO : strategyIntentOperateDTOList) {
                        StrategyIntentOperateMapDTO strategyIntentOperateMapDTO = new StrategyIntentOperateMapDTO();
                        Map<Integer, BigDecimal> yearValue = new HashMap<>();
                        yearValue.put(strategyIntentOperateDTO.getOperateYear(), strategyIntentOperateDTO.getOperateValue());
                        strategyIntentOperateMapDTO.setYearValues(yearValue);
                        strategyIntentOperateMapDTO.setStrategyIntentOperateId(strategyIntentOperateDTO.getStrategyIntentOperateId());
                        //指标id
                        strategyIntentOperateDTOData.setIndicatorId(strategyIntentOperateDTO.getIndicatorId());
                        //指标名称
                        strategyIntentOperateDTOData.setIndicatorName(strategyIntentOperateDTO.getIndicatorName());
                        strategyIntentOperateMapDTOS.add(strategyIntentOperateMapDTO);
                    }
                    //战略意图ID
                    strategyIntentOperateDTOData.setStrategyIntentId(strategyIntentId);
                    //年度指标对应值集合
                    strategyIntentOperateDTOData.setStrategyIntentOperateMapDTOS(strategyIntentOperateMapDTOS);
                    strategyIntentOperateListData.add(strategyIntentOperateDTOData);
                }
            }

        }
        strategyIntentDTO.setStrategyIntentOperateDTOS(strategyIntentOperateListData);
        return strategyIntentDTO;
    }

    /**
     * 查询战略意图表列表
     *
     * @param strategyIntentDTO 战略意图表
     * @return 战略意图表
     */
    @Override
    public List<StrategyIntentDTO> selectStrategyIntentList(StrategyIntentDTO strategyIntentDTO) {

        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        //高级搜索请求参数
        Map<String, Object> params = strategyIntentDTO.getParams();
        this.queryemployeeName(params);
        List<StrategyIntentDTO> strategyIntentDTOS = strategyIntentMapper.selectStrategyIntentList(strategyIntent);
        if (StringUtils.isNotEmpty(strategyIntentDTOS)) {
            Set<Long> createBys = strategyIntentDTOS.stream().map(StrategyIntentDTO::getCreateBy).collect(Collectors.toSet());
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
            List<UserDTO> userDTOList = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(userDTOList)) {
                for (StrategyIntentDTO intentDTO : strategyIntentDTOS) {
                    for (UserDTO userDTO : userDTOList) {
                        if (intentDTO.getCreateBy().equals(userDTO.getUserId())) {
                            intentDTO.setCreateByName(userDTO.getEmployeeName());
                        }
                    }
                }
            }
        }
        return strategyIntentDTOS;
    }

    /**
     * 封装高级查询人员id
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
     * 新增战略意图表
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */
    @Override
    @Transactional
    public StrategyIntentDTO insertStrategyIntent(StrategyIntentDTO strategyIntentDTO) {
        StrategyIntentDTO strategyIntentDTO1 = strategyIntentMapper.selectStrategyIntentByPlanYear(strategyIntentDTO.getPlanYear());
        if (StringUtils.isNotNull(strategyIntentDTO1)) {
            throw new ServiceException("已存在该年度数据");
        }
        //保存数据库数据
        List<StrategyIntentOperate> strategyIntentOperateList = new ArrayList<>();
        //前台传入数据
        List<StrategyIntentOperateDTO> strategyIntentOperateDTOS = strategyIntentDTO.getStrategyIntentOperateDTOS();
        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        strategyIntent.setCreateBy(SecurityUtils.getUserId());
        strategyIntent.setCreateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateBy(SecurityUtils.getUserId());
        strategyIntent.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyIntentMapper.insertStrategyIntent(strategyIntent);

        if (StringUtils.isNotEmpty(strategyIntentOperateDTOS)) {
            int i = 1;
            for (StrategyIntentOperateDTO strategyIntentOperateDTO : strategyIntentOperateDTOS) {
                //年度指标对应值集合
                List<StrategyIntentOperateMapDTO> strategyIntentOperateMapDTOS = strategyIntentOperateDTO.getStrategyIntentOperateMapDTOS();
                if (StringUtils.isNotEmpty(strategyIntentOperateMapDTOS)) {
                    for (StrategyIntentOperateMapDTO strategyIntentOperateMapDTO : strategyIntentOperateMapDTOS) {
                        Map<Integer, BigDecimal> yearValue = strategyIntentOperateMapDTO.getYearValues();
                        //经营年度
                        Integer operateYear = null;
                        //经营值
                        BigDecimal operateValue = null;
                        for (Integer key : yearValue.keySet()) {
                            operateYear = key;
                            operateValue = yearValue.get(key);
                        }
                        StrategyIntentOperate strategyIntentOperate = new StrategyIntentOperate();
                        //战略id
                        strategyIntentOperate.setStrategyIntentId(strategyIntent.getStrategyIntentId());
                        strategyIntentOperate.setSort(i);
                        //指标id
                        strategyIntentOperate.setIndicatorId(strategyIntentOperateDTO.getIndicatorId());
                        //经营年度
                        strategyIntentOperate.setOperateYear(operateYear);
                        //经营值
                        strategyIntentOperate.setOperateValue(operateValue);
                        strategyIntentOperate.setCreateBy(SecurityUtils.getUserId());
                        strategyIntentOperate.setCreateTime(DateUtils.getNowDate());
                        strategyIntentOperate.setUpdateTime(DateUtils.getNowDate());
                        strategyIntentOperate.setUpdateBy(SecurityUtils.getUserId());
                        strategyIntentOperate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);

                        strategyIntentOperateList.add(strategyIntentOperate);

                    }
                    i++;
                }
            }
            if (StringUtils.isNotEmpty(strategyIntentOperateList)) {
                try {
                    strategyIntentOperateMapperr.batchStrategyIntentOperate(strategyIntentOperateList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增战略意图经营表失败");
                }
            }
        }
        strategyIntentDTO.setStrategyIntentId(strategyIntent.getStrategyIntentId());
        return strategyIntentDTO;
    }

    /**
     * 修改战略意图表
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStrategyIntent(StrategyIntentDTO strategyIntentDTO) {
        int i = 0;
        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        strategyIntent.setUpdateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = strategyIntentMapper.updateStrategyIntent(strategyIntent);
        } catch (Exception e) {
            throw new ServiceException("修改战略意图失败");
        }
        //接收前端战略意图经营表集合
        List<StrategyIntentOperateDTO> strategyIntentOperateDTOS = strategyIntentDTO.getStrategyIntentOperateDTOS();
        //数据库已存在的战略意图经营表集合
        List<StrategyIntentOperateDTO> strategyIntentOperateDTOList = strategyIntentOperateMapperr.selectStrategyIntentOperateByStrategyIntentId(strategyIntent.getStrategyIntentId());

        //差集
        List<Long> strategyIntentOperateIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(strategyIntentOperateDTOList)) {
            if (StringUtils.isNotEmpty(strategyIntentOperateDTOS)) {
                List<StrategyIntentOperateMapDTO> strategyIntentOperateMapDTOAll = new ArrayList<>();
                for (StrategyIntentOperateDTO strategyIntentOperateDTO : strategyIntentOperateDTOS) {
                    strategyIntentOperateMapDTOAll.addAll(strategyIntentOperateDTO.getStrategyIntentOperateMapDTOS());
                }
                if (StringUtils.isNotEmpty(strategyIntentOperateMapDTOAll)) {
                    //sterm流求差集
                    strategyIntentOperateIds = strategyIntentOperateDTOList.stream().filter(a ->
                            !strategyIntentOperateMapDTOAll.stream().map(StrategyIntentOperateMapDTO::getStrategyIntentOperateId).collect(Collectors.toList()).contains(a.getStrategyIntentOperateId())
                    ).collect(Collectors.toList()).stream().map(StrategyIntentOperateDTO::getStrategyIntentOperateId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(strategyIntentOperateIds)) {
                        try {
                            strategyIntentOperateMapperr.logicDeleteStrategyIntentOperateByStrategyIntentOperateIds(strategyIntentOperateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除战略意图经营失败");
                        }
                    }
                }

                //新增集合
                List<StrategyIntentOperate> strategyIntentOperateAddList = new ArrayList<>();
                //修改集合
                List<StrategyIntentOperate> strategyIntentOperateUpdateList = new ArrayList<>();
                for (int i1 = 0; i1 < strategyIntentOperateDTOS.size(); i1++) {
                    //年度指标对应值集合
                    List<StrategyIntentOperateMapDTO> strategyIntentOperateMapDTOS = strategyIntentOperateDTOS.get(i1).getStrategyIntentOperateMapDTOS();
                    if (StringUtils.isNotEmpty(strategyIntentOperateMapDTOS)) {
                        for (StrategyIntentOperateMapDTO strategyIntentOperateMapDTO : strategyIntentOperateMapDTOS) {
                            Long strategyIntentOperateId = strategyIntentOperateMapDTO.getStrategyIntentOperateId();
                            StrategyIntentOperate strategyIntentOperate = new StrategyIntentOperate();
                            BeanUtils.copyProperties(strategyIntentOperateDTOS.get(i1), strategyIntentOperate);
                            Map<Integer, BigDecimal> yearValue = strategyIntentOperateMapDTO.getYearValues();
                            //经营年度
                            Integer operateYear = null;
                            //经营值
                            BigDecimal operateValue = null;
                            for (Integer key : yearValue.keySet()) {
                                operateYear = key;
                                operateValue = yearValue.get(key);
                            }
                            //排序
                            strategyIntentOperate.setSort(i1 + 1);
                            if (null != strategyIntentOperateId) {
                                strategyIntentOperate.setStrategyIntentOperateId(strategyIntentOperateId);
                                //经营年度
                                strategyIntentOperate.setOperateYear(operateYear);
                                //经营值
                                strategyIntentOperate.setOperateValue(operateValue);
                                strategyIntentOperate.setUpdateTime(DateUtils.getNowDate());
                                strategyIntentOperate.setUpdateBy(SecurityUtils.getUserId());
                                strategyIntentOperateUpdateList.add(strategyIntentOperate);
                            } else {
                                //经营年度
                                strategyIntentOperate.setOperateYear(operateYear);
                                //经营值
                                strategyIntentOperate.setOperateValue(operateValue);
                                strategyIntentOperate.setStrategyIntentId(strategyIntent.getStrategyIntentId());
                                strategyIntentOperate.setCreateBy(SecurityUtils.getUserId());
                                strategyIntentOperate.setCreateTime(DateUtils.getNowDate());
                                strategyIntentOperate.setUpdateTime(DateUtils.getNowDate());
                                strategyIntentOperate.setUpdateBy(SecurityUtils.getUserId());
                                strategyIntentOperate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                                strategyIntentOperateAddList.add(strategyIntentOperate);

                            }

                        }
                    }

                }

                if (StringUtils.isNotEmpty(strategyIntentOperateAddList)) {
                    try {
                        strategyIntentOperateMapperr.batchStrategyIntentOperate(strategyIntentOperateAddList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增战略意图经营失败");
                    }
                }
                if (StringUtils.isNotEmpty(strategyIntentOperateUpdateList)) {
                    try {
                        strategyIntentOperateMapperr.updateStrategyIntentOperates(strategyIntentOperateUpdateList);
                    } catch (Exception e) {
                        throw new ServiceException("批量修改战略意图经营失败");
                    }
                }
            } else {
                strategyIntentOperateIds = strategyIntentOperateDTOList.stream().map(StrategyIntentOperateDTO::getStrategyIntentOperateId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(strategyIntentOperateIds)) {
                    try {
                        strategyIntentOperateMapperr.logicDeleteStrategyIntentOperateByStrategyIntentOperateIds(strategyIntentOperateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除战略意图经营失败");
                    }
                }
            }

        } else {
            if (StringUtils.isNotEmpty(strategyIntentOperateDTOS)) {
                List<StrategyIntentOperate> strategyIntentOperateList = new ArrayList<>();
                for (int i1 = 0; i1 < strategyIntentOperateDTOS.size(); i1++) {
                    //年度指标对应值集合
                    List<StrategyIntentOperateMapDTO> strategyIntentOperateMapDTOS = strategyIntentOperateDTOS.get(i1).getStrategyIntentOperateMapDTOS();

                    if (StringUtils.isNotEmpty(strategyIntentOperateMapDTOS)) {
                        for (StrategyIntentOperateMapDTO strategyIntentOperateMapDTO : strategyIntentOperateMapDTOS) {
                            StrategyIntentOperate strategyIntentOperate = new StrategyIntentOperate();
                            BeanUtils.copyProperties(strategyIntentOperateDTOS.get(i1), strategyIntentOperate);
                            Map<Integer, BigDecimal> yearValue = strategyIntentOperateMapDTO.getYearValues();
                            //经营年度
                            Integer operateYear = null;
                            //经营值
                            BigDecimal operateValue = null;
                            for (Integer key : yearValue.keySet()) {
                                operateYear = key;
                                operateValue = yearValue.get(key);
                            }
                            //排序
                            strategyIntentOperate.setSort(i1);
                            //经营年度
                            strategyIntentOperate.setOperateYear(operateYear);
                            //经营值
                            strategyIntentOperate.setOperateValue(operateValue);
                            strategyIntentOperate.setStrategyIntentId(strategyIntent.getStrategyIntentId());
                            strategyIntentOperate.setCreateBy(SecurityUtils.getUserId());
                            strategyIntentOperate.setCreateTime(DateUtils.getNowDate());
                            strategyIntentOperate.setUpdateTime(DateUtils.getNowDate());
                            strategyIntentOperate.setUpdateBy(SecurityUtils.getUserId());
                            strategyIntentOperate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            strategyIntentOperateList.add(strategyIntentOperate);
                        }
                    }

                }
                if (StringUtils.isNotEmpty(strategyIntentOperateList)) {
                    try {
                        strategyIntentOperateMapperr.batchStrategyIntentOperate(strategyIntentOperateList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增战略意图经营失败");
                    }
                }
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除战略意图表
     *
     * @param strategyIntentIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteStrategyIntentByStrategyIntentIds(List<Long> strategyIntentIds) {
        int i = 0;
        try {
            i = strategyIntentMapper.logicDeleteStrategyIntentByStrategyIntentIds(strategyIntentIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("逻辑批量删除战略意图失败");
        }
        List<StrategyIntentOperateDTO> strategyIntentOperateDTOList = strategyIntentOperateMapperr.selectStrategyIntentOperateByStrategyIntentIds(strategyIntentIds);
        if (StringUtils.isNotEmpty(strategyIntentIds)) {
            List<Long> strategyIntentOperateIds = strategyIntentOperateDTOList.stream().map(StrategyIntentOperateDTO::getStrategyIntentOperateId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(strategyIntentOperateIds)) {
                try {
                    strategyIntentOperateMapperr.logicDeleteStrategyIntentOperateByStrategyIntentOperateIds(strategyIntentOperateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除战略意图经营失败");
                }
            }
        }
        return i;
    }

    /**
     * 物理删除战略意图表信息
     *
     * @param strategyIntentId 战略意图表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyIntentByStrategyIntentId(Long strategyIntentId) {
        return strategyIntentMapper.deleteStrategyIntentByStrategyIntentId(strategyIntentId);
    }

    /**
     * 逻辑删除战略意图表信息
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteStrategyIntentByStrategyIntentId(StrategyIntentDTO strategyIntentDTO) {
        int i = 0;
        StrategyIntent strategyIntent = new StrategyIntent();
        strategyIntent.setStrategyIntentId(strategyIntentDTO.getStrategyIntentId());
        strategyIntent.setUpdateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = strategyIntentMapper.logicDeleteStrategyIntentByStrategyIntentId(strategyIntent);
        } catch (Exception e) {
            throw new ServiceException("逻辑删除战略意图表");
        }
        List<StrategyIntentOperateDTO> strategyIntentOperateDTOList = strategyIntentOperateMapperr.selectStrategyIntentOperateByStrategyIntentId(strategyIntent.getStrategyIntentId());
        if (StringUtils.isNotEmpty(strategyIntentOperateDTOList)) {
            List<Long> strategyIntentOperateIds = strategyIntentOperateDTOList.stream().map(StrategyIntentOperateDTO::getStrategyIntentOperateId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(strategyIntentOperateIds)) {
                strategyIntentOperateMapperr.logicDeleteStrategyIntentOperateByStrategyIntentOperateIds(strategyIntentOperateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            }

        }
        return i;
    }

    /**
     * 物理删除战略意图表信息
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */

    @Override
    public int deleteStrategyIntentByStrategyIntentId(StrategyIntentDTO strategyIntentDTO) {
        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        return strategyIntentMapper.deleteStrategyIntentByStrategyIntentId(strategyIntent.getStrategyIntentId());
    }

    /**
     * 物理批量删除战略意图表
     *
     * @param strategyIntentDtos 需要删除的战略意图表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyIntentByStrategyIntentIds(List<StrategyIntentDTO> strategyIntentDtos) {
        List<Long> stringList = new ArrayList();
        for (StrategyIntentDTO strategyIntentDTO : strategyIntentDtos) {
            stringList.add(strategyIntentDTO.getStrategyIntentId());
        }
        return strategyIntentMapper.deleteStrategyIntentByStrategyIntentIds(stringList);
    }

    /**
     * 批量新增战略意图表信息
     *
     * @param strategyIntentDtos 战略意图表对象
     */

    public int insertStrategyIntents(List<StrategyIntentDTO> strategyIntentDtos) {
        List<StrategyIntent> strategyIntentList = new ArrayList();

        for (StrategyIntentDTO strategyIntentDTO : strategyIntentDtos) {
            StrategyIntent strategyIntent = new StrategyIntent();
            BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
            strategyIntent.setCreateBy(SecurityUtils.getUserId());
            strategyIntent.setCreateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateBy(SecurityUtils.getUserId());
            strategyIntent.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyIntentList.add(strategyIntent);
        }
        return strategyIntentMapper.batchStrategyIntent(strategyIntentList);
    }

    /**
     * 批量修改战略意图表信息
     *
     * @param strategyIntentDtos 战略意图表对象
     */

    public int updateStrategyIntents(List<StrategyIntentDTO> strategyIntentDtos) {
        List<StrategyIntent> strategyIntentList = new ArrayList();

        for (StrategyIntentDTO strategyIntentDTO : strategyIntentDtos) {
            StrategyIntent strategyIntent = new StrategyIntent();
            BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
            strategyIntent.setCreateBy(SecurityUtils.getUserId());
            strategyIntent.setCreateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateBy(SecurityUtils.getUserId());
            strategyIntentList.add(strategyIntent);
        }
        return strategyIntentMapper.updateStrategyIntents(strategyIntentList);
    }
}

