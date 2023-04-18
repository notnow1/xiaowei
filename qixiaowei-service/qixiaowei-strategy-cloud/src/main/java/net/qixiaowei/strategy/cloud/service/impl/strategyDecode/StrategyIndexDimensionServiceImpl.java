package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyIndexDimension;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyIndexDimensionDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyIndexDimensionMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyIndexDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * StrategyIndexDimensionService业务层处理
 *
 * @author Graves
 * @since 2023-03-03
 */
@Service
@Slf4j
public class StrategyIndexDimensionServiceImpl implements IStrategyIndexDimensionService {
    @Autowired
    private StrategyIndexDimensionMapper strategyIndexDimensionMapper;
    @Autowired
    private StrategyMeasureMapper strategyMeasureMapper;//战略举措清单
    @Autowired
    private StrategyMetricsMapper strategyMetricsMapper;//战略衡量指标

    /**
     * 行业吸引力配置预制详情集中度
     */
    private static final List<StrategyIndexDimension> INIT_STRATEGY_INDEX_DIMENSION_PAR = new ArrayList<>(4);
    private static final List<StrategyIndexDimension> INIT_STRATEGY_INDEX_DIMENSION_SON_1 = new ArrayList<>(3);
    private static final List<StrategyIndexDimension> INIT_STRATEGY_INDEX_DIMENSION_SON_2 = new ArrayList<>(4);

    static {
        INIT_STRATEGY_INDEX_DIMENSION_PAR.add(StrategyIndexDimension.builder().indexDimensionName("财务层面").indexDimensionCode("F").parentIndexDimensionId(0L).ancestors("").sort(1).level(1).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_PAR.add(StrategyIndexDimension.builder().indexDimensionName("客户层面").indexDimensionCode("C").parentIndexDimensionId(0L).ancestors("").sort(2).level(1).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_PAR.add(StrategyIndexDimension.builder().indexDimensionName("内部经营层面").indexDimensionCode("I").parentIndexDimensionId(0L).ancestors("").sort(3).level(1).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_PAR.add(StrategyIndexDimension.builder().indexDimensionName("学习与成长层面").indexDimensionCode("L").parentIndexDimensionId(0L).ancestors("").sort(4).level(1).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_SON_1.add(StrategyIndexDimension.builder().indexDimensionName("客户管理").indexDimensionCode("I").sort(1).level(2).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_SON_1.add(StrategyIndexDimension.builder().indexDimensionName("产品创新").indexDimensionCode("I").sort(2).level(2).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_SON_1.add(StrategyIndexDimension.builder().indexDimensionName("运营管理").indexDimensionCode("I").sort(3).level(2).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_SON_1.add(StrategyIndexDimension.builder().indexDimensionName("负责的公民").indexDimensionCode("I").sort(4).level(2).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_SON_2.add(StrategyIndexDimension.builder().indexDimensionName("人力资本").indexDimensionCode("L").sort(1).level(2).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_SON_2.add(StrategyIndexDimension.builder().indexDimensionName("信息资本").indexDimensionCode("L").sort(2).level(2).status(1).build());
        INIT_STRATEGY_INDEX_DIMENSION_SON_2.add(StrategyIndexDimension.builder().indexDimensionName("组织资本").indexDimensionCode("L").sort(3).level(2).status(1).build());
    }

    /**
     * 查询战略指标维度表
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 战略指标维度表
     */
    @Override
    public StrategyIndexDimensionDTO selectStrategyIndexDimensionByStrategyIndexDimensionId(Long strategyIndexDimensionId) {
        return strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimensionId);
    }

    /**
     * 查询战略指标维度表列表
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 战略指标维度表
     */
    @Override
    public List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionList(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        List<StrategyIndexDimensionDTO> list = strategyIndexDimensionMapper.selectStrategyIndexDimensionList(strategyIndexDimension);
        if (StringUtils.isNotEmpty(list)) {
            Set<Long> userIds = list.stream().map(StrategyIndexDimensionDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            list.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
        return list;
    }


    /**
     * 获取战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO 战略指标维度表DTO
     * @return 结果
     */
    @Override
    public List<Tree<Long>> selectStrategyIndexDimensionTreeList(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        List<StrategyIndexDimensionDTO> list = strategyIndexDimensionMapper.selectStrategyIndexDimensionList(new StrategyIndexDimension());
        if (StringUtils.isNotEmpty(list)) {
            Set<Long> userIds = list.stream().map(StrategyIndexDimensionDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            list.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
        return getTreeList(list);
    }

    /**
     * 获取树结构
     *
     * @param list 列表
     * @return 结果
     */
    private static List<Tree<Long>> getTreeList(List<StrategyIndexDimensionDTO> list) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("strategyIndexDimensionId");
        treeNodeConfig.setNameKey("indexDimensionName");
        treeNodeConfig.setParentIdKey("parentIndexDimensionId");
        return TreeUtil.build(list, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
                    tree.setId(treeNode.getStrategyIndexDimensionId());
                    tree.setParentId(treeNode.getParentIndexDimensionId());
                    tree.setName(treeNode.getIndexDimensionName());
                    tree.setWeight(treeNode.getSort());
                    tree.putExtra("indexDimensionCode", treeNode.getIndexDimensionCode());
                    tree.putExtra("parentIndexDimensionName", treeNode.getParentIndexDimensionName());
                    tree.putExtra("level", treeNode.getLevel());
                    tree.putExtra("levelName", treeNode.getLevel() + "级");
                    tree.putExtra("status", treeNode.getStatus());
                    tree.putExtra("createBy", treeNode.getCreateBy());
                    tree.putExtra("createByName", treeNode.getCreateByName());
                    tree.putExtra("createTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", treeNode.getCreateTime()));
                }
        );
    }

    /**
     * 查询有效的树结构列表
     *
     * @param strategyIndexDimensionDTO 战略指标维度dto
     * @return 结果
     */
    @Override
    public List<Tree<Long>> selectStrategyIndexDimensionEffectiveTreeList(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        Integer status = strategyIndexDimensionDTO.getStatus();
        if (StringUtils.isNull(status)) {
            throw new ServiceException("请传入状态");
        }
        List<StrategyIndexDimensionDTO> list = strategyIndexDimensionMapper.selectStrategyIndexDimensionList(new StrategyIndexDimension().setStatus(status));
        return getTreeList(list);
    }

    /**
     * 获取战略指标维度根节点
     *
     * @return List
     */
    @Override
    public List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionRootList(StrategyIndexDimensionDTO indexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(indexDimensionDTO, strategyIndexDimension);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionList(strategyIndexDimension);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOTree = this.createTree(strategyIndexDimensionDTOS, 0L);
        List<StrategyIndexDimensionDTO> rootList = new ArrayList<>();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOTree) {
            List<StrategyIndexDimensionDTO> children = strategyIndexDimensionDTO.getChildren();
            strategyIndexDimensionDTO.setRootIndexDimensionName(strategyIndexDimensionDTO.getIndexDimensionName());
            if (StringUtils.isNotEmpty(children))
                setRootNameValue(children, strategyIndexDimensionDTO, rootList);
            else
                rootList.add(strategyIndexDimensionDTO);
        }
        for (int i = 0; i < rootList.size(); i++) {
            rootList.get(i).setSort(i);
        }
        return rootList;
    }

    /**
     * 获取所有的战略指标维度根节点
     *
     * @return List
     */
    @Override
    public List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionAllRootList(StrategyIndexDimensionDTO indexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(indexDimensionDTO, strategyIndexDimension);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionList(strategyIndexDimension);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOTree = this.createTree(strategyIndexDimensionDTOS, 0L);
        List<StrategyIndexDimensionDTO> rootList = new ArrayList<>();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOTree) {
            List<StrategyIndexDimensionDTO> children = strategyIndexDimensionDTO.getChildren();
            strategyIndexDimensionDTO.setRootIndexDimensionName(strategyIndexDimensionDTO.getIndexDimensionName());
            if (StringUtils.isNotEmpty(children)) {
                setAllRootNameValue(children, strategyIndexDimensionDTO, rootList);
            }
            rootList.add(strategyIndexDimensionDTO);
        }
        for (int i = 0; i < rootList.size(); i++) {
            rootList.get(i).setSort(i);
        }
        return rootList;
    }

    /**
     * 规划业务单元列表-不带本身
     *
     * @param strategyIndexDimensionId 主键
     * @return List
     */
    @Override
    public List<Tree<Long>> selectStrategyIndexDimensionOtherList(Long strategyIndexDimensionId) {
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionOtherList(strategyIndexDimensionId);
        //自定义属性名
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("strategyIndexDimensionId");
        treeNodeConfig.setNameKey("indexDimensionName");
        treeNodeConfig.setParentIdKey("parentIndexDimensionId");
        return TreeUtil.build(strategyIndexDimensionDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
                    tree.setId(treeNode.getStrategyIndexDimensionId());
                    tree.setParentId(treeNode.getParentIndexDimensionId());
                    tree.setName(treeNode.getIndexDimensionName());
                    tree.setWeight(treeNode.getSort());
                    tree.putExtra("indexDimensionCode", treeNode.getIndexDimensionCode());
                    tree.putExtra("level", treeNode.getLevel());
                    tree.putExtra("levelName", treeNode.getLevel() + "级");
                    tree.putExtra("status", treeNode.getStatus());
                    tree.putExtra("createBy", treeNode.getCreateBy());
                    tree.putExtra("createTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", treeNode.getCreateTime()));
                }
        );
    }

    /**
     * 获取层级列表
     *
     * @return List
     */
    @Override
    public List<Integer> selectStrategyIndexDimensionLevelList() {
        return strategyIndexDimensionMapper.selectStrategyIndexDimensionLevelList();
    }

    /**
     * 初始化战略指标维度
     *
     * @return 真假
     */
    @Override
    @Transactional
    public boolean initStrategyIndexDimension(Long userId) {
        Date nowDate = DateUtils.getNowDate();
        //战略指标维度主表
        List<StrategyIndexDimension> strategyIndexDimensions = new ArrayList<>();
        for (StrategyIndexDimension strategyIndexDimension : INIT_STRATEGY_INDEX_DIMENSION_PAR) {
            StrategyIndexDimension strategyIndexDimension1 = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimension, strategyIndexDimension1);
            strategyIndexDimension1.setStrategyIndexDimensionId(null);
            strategyIndexDimension1.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyIndexDimension1.setCreateBy(userId);
            strategyIndexDimension1.setUpdateBy(userId);
            strategyIndexDimension1.setCreateTime(nowDate);
            strategyIndexDimension1.setUpdateTime(nowDate);
            strategyIndexDimensions.add(strategyIndexDimension1);
        }
        try {
            strategyIndexDimensionMapper.batchStrategyIndexDimension(strategyIndexDimensions);
        } catch (Exception e) {
            log.error("插入父级战略指标维度失败{}", e.getMessage());
            throw new ServiceException("插入父级战略指标维度失败！！！");
        }
        StrategyIndexDimension strategyIndexDimension1 = strategyIndexDimensions.get(2);
        StrategyIndexDimension strategyIndexDimension2 = strategyIndexDimensions.get(3);
        if (StringUtils.isNull(strategyIndexDimension1) || StringUtils.isNull(strategyIndexDimension2)) {
            throw new ServiceException("初始化父级战略指标维度失败！！！");
        }
        List<StrategyIndexDimension> strategyIndexDimensionSon = new ArrayList<>();
        for (StrategyIndexDimension strategyIndexDimension : INIT_STRATEGY_INDEX_DIMENSION_SON_1) {
            StrategyIndexDimension strategyIndexDimension3 = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimension, strategyIndexDimension3);
            strategyIndexDimension3.setStrategyIndexDimensionId(null);
            strategyIndexDimension3.setParentIndexDimensionId(strategyIndexDimension1.getStrategyIndexDimensionId());
            strategyIndexDimension3.setAncestors(strategyIndexDimension1.getStrategyIndexDimensionId().toString());
            strategyIndexDimension3.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyIndexDimension3.setCreateBy(userId);
            strategyIndexDimension3.setUpdateBy(userId);
            strategyIndexDimension3.setCreateTime(nowDate);
            strategyIndexDimension3.setUpdateTime(nowDate);
            strategyIndexDimensionSon.add(strategyIndexDimension3);
        }
        for (StrategyIndexDimension strategyIndexDimension : INIT_STRATEGY_INDEX_DIMENSION_SON_2) {
            StrategyIndexDimension strategyIndexDimension3 = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimension, strategyIndexDimension3);
            strategyIndexDimension3.setStrategyIndexDimensionId(null);
            strategyIndexDimension3.setParentIndexDimensionId(strategyIndexDimension2.getStrategyIndexDimensionId());
            strategyIndexDimension3.setAncestors(strategyIndexDimension2.getStrategyIndexDimensionId().toString());
            strategyIndexDimension3.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyIndexDimension3.setCreateBy(userId);
            strategyIndexDimension3.setUpdateBy(userId);
            strategyIndexDimension3.setCreateTime(nowDate);
            strategyIndexDimension3.setUpdateTime(nowDate);
            strategyIndexDimensionSon.add(strategyIndexDimension3);
        }
        try {
            strategyIndexDimensionMapper.batchStrategyIndexDimension(strategyIndexDimensionSon);
        } catch (Exception e) {
            log.error("初始化子级战略指标维度失败{}", e.getMessage());
            throw new ServiceException("初始化子级战略指标维度失败！！！");
        }
        return true;
    }


    /**
     * 树形结构
     *
     * @param strategyIndexDimensionDTOChild  子级列表
     * @param parentStrategyIndexDimensionDTO 父级DTO
     * @param rootList                        根目录list
     */
    private void setRootNameValue(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOChild, StrategyIndexDimensionDTO parentStrategyIndexDimensionDTO, List<StrategyIndexDimensionDTO> rootList) {
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOChild) {
            strategyIndexDimensionDTO.setRootIndexDimensionName(parentStrategyIndexDimensionDTO.getRootIndexDimensionName() + "-" + strategyIndexDimensionDTO.getIndexDimensionName());
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getChildren()))
                setRootNameValue(strategyIndexDimensionDTO.getChildren(), strategyIndexDimensionDTO, rootList);
            else
                rootList.add(strategyIndexDimensionDTO);
        }
    }

    /**
     * 树形结构
     *
     * @param children                  子级列表
     * @param strategyIndexDimensionDTO 父级DTO
     * @param rootList                  根目录list
     */
    private void setAllRootNameValue(List<StrategyIndexDimensionDTO> children, StrategyIndexDimensionDTO strategyIndexDimensionDTO, List<StrategyIndexDimensionDTO> rootList) {
        for (StrategyIndexDimensionDTO child : children) {
            List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = child.getChildren();
            child.setRootIndexDimensionName(strategyIndexDimensionDTO.getRootIndexDimensionName() + "-" + child.getIndexDimensionName());
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTOS)) {
                setAllRootNameValue(strategyIndexDimensionDTOS, child, rootList);
            }
            rootList.add(child);
        }
    }


    /**
     * 树形结构
     *
     * @param pid   父级ID
     * @param lists 列表
     * @return List
     */
    private List<StrategyIndexDimensionDTO> createTree(List<StrategyIndexDimensionDTO> lists, Long pid) {
        List<StrategyIndexDimensionDTO> tree = new ArrayList<>();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : lists) {
            if (Objects.equals(strategyIndexDimensionDTO.getParentIndexDimensionId(), pid)) {
                strategyIndexDimensionDTO.setChildren(createTree(lists, strategyIndexDimensionDTO.getStrategyIndexDimensionId()));
                tree.add(strategyIndexDimensionDTO);
            }
        }
        return tree;
    }

    /**
     * 新增战略指标维度表
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */
    @Override
    @Transactional
    public StrategyIndexDimensionDTO insertStrategyIndexDimension(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        String strategyIndexDimensionCode = strategyIndexDimensionDTO.getIndexDimensionCode();
        if (StringUtils.isEmpty(strategyIndexDimensionCode)) {
            throw new ServiceException("战略指标维度编码不能为空");
        }
        String parentAncestors = "";//仅在非一级战略指标维度时有用
        Integer parentLevel = 1;
        Long parentStrategyIndexDimensionId = strategyIndexDimensionDTO.getParentIndexDimensionId();
        if (StringUtils.isNotNull(strategyIndexDimensionDTO.getLevel()) && strategyIndexDimensionDTO.getLevel() == 1) {
            List<StrategyIndexDimensionDTO> strategyIndexDimensionsByCode = strategyIndexDimensionMapper.checkUnique(strategyIndexDimensionCode);
            if (StringUtils.isNotEmpty(strategyIndexDimensionsByCode)) {
                throw new ServiceException("新增战略指标维度" + strategyIndexDimensionDTO.getIndexDimensionName() + "失败,战略指标维度序号重复");
            }
        }
        if (parentStrategyIndexDimensionId != 0L) {
            StrategyIndexDimensionDTO parentStrategyIndexDimension = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionId(parentStrategyIndexDimensionId);
            if (parentStrategyIndexDimension == null) {
                throw new ServiceException("该上级战略指标维度不存在");
            }
            parentAncestors = parentStrategyIndexDimension.getAncestors();
            parentLevel = parentStrategyIndexDimension.getLevel();
            Integer status = parentStrategyIndexDimension.getStatus();
            // 如果父节点不为正常状态,则不允许新增子节点
            if (BusinessConstants.DISABLE.equals(status)) {
                throw new ServiceException("上级战略指标维度失效，不允许新增子节点");
            }
        }
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        if (parentStrategyIndexDimensionId != 0L) {
            strategyIndexDimension.setAncestors(StringUtils.isEmpty(parentAncestors) ? parentStrategyIndexDimensionId.toString() : parentAncestors + "," + parentStrategyIndexDimensionId);
            strategyIndexDimension.setLevel(parentLevel + 1);
        } else {
            strategyIndexDimension.setParentIndexDimensionId(Constants.TOP_PARENT_ID);
            strategyIndexDimension.setAncestors(parentAncestors);
            strategyIndexDimension.setLevel(parentLevel);
        }
        // 插入顺序
        strategyIndexDimension.setSort(strategyIndexDimensionMapper.selectMaxSort(strategyIndexDimension.getLevel(), parentStrategyIndexDimensionId));
        strategyIndexDimension.setCreateBy(SecurityUtils.getUserId());
        strategyIndexDimension.setCreateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        strategyIndexDimension.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyIndexDimensionMapper.insertStrategyIndexDimension(strategyIndexDimension);
        strategyIndexDimensionDTO.setStrategyIndexDimensionId(strategyIndexDimension.getStrategyIndexDimensionId());
        return strategyIndexDimensionDTO;
    }

    /**
     * 修改战略指标维度表
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStrategyIndexDimension(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        String strategyIndexDimensionCode = strategyIndexDimensionDTO.getIndexDimensionCode();
        Long strategyIndexDimensionId = strategyIndexDimensionDTO.getStrategyIndexDimensionId();
        StrategyIndexDimensionDTO strategyIndexDimensionById = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimensionId);
        if (StringUtils.isEmpty(strategyIndexDimensionCode))
            throw new ServiceException("战略指标维度编码不能为空");
        if (StringUtils.isNull(strategyIndexDimensionById))
            throw new ServiceException("该战略指标维度不存在");
        if (!strategyIndexDimensionById.getIndexDimensionCode().equals(strategyIndexDimensionDTO.getIndexDimensionCode())) {
            this.changeSonCodes(strategyIndexDimensionId, strategyIndexDimensionDTO.getIndexDimensionCode());
        }
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        String ancestors = "";//仅在非一级指标时有用
        int parentLevel = 1;
        Integer status = strategyIndexDimensionDTO.getStatus();
        Long parentStrategyIndexDimensionId = strategyIndexDimensionDTO.getParentIndexDimensionId();
        if (parentStrategyIndexDimensionId != 0L) {
            StrategyIndexDimensionDTO parentStrategyIndexDimension = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionId(parentStrategyIndexDimensionId);
            if (StringUtils.isNull(parentStrategyIndexDimension))
                throw new ServiceException("该上级战略指标维度不存在");
            // 如果父节点不为正常状态,则不允许新增子节点
            if (BusinessConstants.DISABLE.equals(parentStrategyIndexDimension.getStatus()))
                throw new ServiceException("上级战略指标维度失效，不允许编辑子节点");
            // 等级
            parentLevel = parentStrategyIndexDimension.getLevel() + 1;
            // 路径修改
            if (!strategyIndexDimensionById.getParentIndexDimensionId().equals(parentStrategyIndexDimensionId)) {
                ancestors = parentStrategyIndexDimension.getAncestors();
                if (StringUtils.isNotEmpty(ancestors)) {
                    ancestors = ancestors + ",";
                }
                ancestors = ancestors + parentStrategyIndexDimensionId;
            }
        } else {
            ancestors = "";
            strategyIndexDimension.setParentIndexDimensionId(Constants.TOP_PARENT_ID);
        }
        this.changeSonStatus(strategyIndexDimensionId, status);
        strategyIndexDimension.setAncestors(ancestors);
        strategyIndexDimension.setStatus(strategyIndexDimensionDTO.getStatus());
        strategyIndexDimension.setLevel(parentLevel);
        strategyIndexDimension.setParentIndexDimensionId(parentStrategyIndexDimensionId);
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        /**
         *  修改子级
         *         List<StrategyIndexDimension> strategyIndexDimensionUpdateList = this.changeSonStrategyIndexDimension(strategyIndexDimensionId, strategyIndexDimension);
         *         if (StringUtils.isNotEmpty(strategyIndexDimensionUpdateList)) {
         *             try {
         *                 strategyIndexDimensionMapper.updateStrategyIndexDimensions(strategyIndexDimensionUpdateList);
         *             } catch (Exception e) {
         *                 throw new ServiceException("批量修改战略指标维度子级信息失败");
         *             }
         *         }
         */
        return strategyIndexDimensionMapper.updateStrategyIndexDimension(strategyIndexDimension);
    }

    /**
     * 修改子级的编码
     *
     * @param strategyIndexDimensionId id
     * @param indexDimensionCode       编码
     */
    private void changeSonCodes(Long strategyIndexDimensionId, String indexDimensionCode) {
        //先查再批量更新
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectSon(strategyIndexDimensionId);
        List<Long> strategyIndexDimensionIds = strategyIndexDimensionDTOS.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(strategyIndexDimensionIds)) {
            strategyIndexDimensionIds.add(strategyIndexDimensionId);
            List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOList = new ArrayList<>();
            for (Long id : strategyIndexDimensionIds) {
                StrategyIndexDimensionDTO strategyIndexDimension = new StrategyIndexDimensionDTO();
                strategyIndexDimension.setStrategyIndexDimensionId(id);
                strategyIndexDimension.setIndexDimensionCode(indexDimensionCode);
                strategyIndexDimensionDTOList.add(strategyIndexDimension);
            }
            updateStrategyIndexDimensions(strategyIndexDimensionDTOList);
        }
    }

    /**
     * 批量修改子级的祖级列表ID
     *
     * @param strategyIndexDimensionId 战略指标维度ID
     * @param strategyIndexDimension   战略指标维度
     */
    private List<StrategyIndexDimension> changeSonStrategyIndexDimension(Long strategyIndexDimensionId, StrategyIndexDimension strategyIndexDimension) {
        List<StrategyIndexDimension> strategyIndexDimensionUpdateList = new ArrayList<>();
        Map<Long, Integer> map = new HashMap<>();
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOList = strategyIndexDimensionMapper.selectAncestors(strategyIndexDimensionId);
        for (int i1 = 0; i1 < strategyIndexDimensionDTOList.size(); i1++) {
            map.put(strategyIndexDimensionDTOList.get(i1).getStrategyIndexDimensionId(), i1);
        }
        if (StringUtils.isNotEmpty(strategyIndexDimensionDTOList) && strategyIndexDimensionDTOList.size() > 1) {
            for (int i1 = 1; i1 < strategyIndexDimensionDTOList.size(); i1++) {
                if (i1 == 1) {
                    StrategyIndexDimension strategyIndexDimension2 = new StrategyIndexDimension();
                    if (StringUtils.isBlank(strategyIndexDimension.getAncestors())) {
                        strategyIndexDimensionDTOList.get(i1).setAncestors(strategyIndexDimension.getStrategyIndexDimensionId().toString());
                    } else {
                        strategyIndexDimensionDTOList.get(i1).setAncestors(strategyIndexDimension.getAncestors() + "," + strategyIndexDimension.getStrategyIndexDimensionId());
                    }
                    strategyIndexDimensionDTOList.get(i1).setLevel(strategyIndexDimension.getLevel() + 1);
                    if (null != strategyIndexDimension.getStatus() && strategyIndexDimension.getStatus() == 0) {
                        strategyIndexDimensionDTOList.get(i1).setStatus(strategyIndexDimension.getStatus());
                    }
                    strategyIndexDimensionDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                    strategyIndexDimensionDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                    strategyIndexDimensionDTOList.get(i1).setParentIndexDimensionId(strategyIndexDimension.getStrategyIndexDimensionId());
                    BeanUtils.copyProperties(strategyIndexDimensionDTOList.get(i1), strategyIndexDimension2);
                    strategyIndexDimensionUpdateList.add(strategyIndexDimension2);
                } else {
                    StrategyIndexDimension strategyIndexDimension2 = new StrategyIndexDimension();
                    StrategyIndexDimensionDTO strategyIndexDimensionDTO2;
                    if (strategyIndexDimensionDTOList.get(i1 - 1).getStrategyIndexDimensionId().equals(strategyIndexDimensionDTOList.get(i1).getParentIndexDimensionId())) {
                        //父级
                        strategyIndexDimensionDTO2 = strategyIndexDimensionDTOList.get(i1 - 1);
                    } else {
                        //父级
                        strategyIndexDimensionDTO2 = strategyIndexDimensionDTOList.get(map.get(strategyIndexDimensionDTOList.get(i1).getParentIndexDimensionId()));
                    }
                    if (StringUtils.isBlank(strategyIndexDimensionDTO2.getAncestors())) {
                        strategyIndexDimensionDTOList.get(i1).setAncestors(strategyIndexDimensionDTO2.getStrategyIndexDimensionId().toString());
                    } else {
                        strategyIndexDimensionDTOList.get(i1).setAncestors(strategyIndexDimensionDTO2.getAncestors() + "," + strategyIndexDimensionDTO2.getStrategyIndexDimensionId());
                    }
                    strategyIndexDimensionDTOList.get(i1).setLevel(strategyIndexDimensionDTO2.getLevel() + 1);
                    if (null != strategyIndexDimension.getStatus() && strategyIndexDimension.getStatus() == 0) {
                        strategyIndexDimensionDTOList.get(i1).setParentIndexDimensionId(strategyIndexDimension.getStrategyIndexDimensionId());
                    }
                    strategyIndexDimensionDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                    strategyIndexDimensionDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                    strategyIndexDimensionDTOList.get(i1).setParentIndexDimensionId(strategyIndexDimensionDTO2.getStrategyIndexDimensionId());
                    BeanUtils.copyProperties(strategyIndexDimensionDTOList.get(i1), strategyIndexDimension2);
                    strategyIndexDimensionUpdateList.add(strategyIndexDimension2);
                }
            }
        }
        return strategyIndexDimensionUpdateList;
    }

    /**
     * 修改状态
     *
     * @param strategyIndexDimensionId 战略指标ID
     * @param status                   寒夜
     */
    private void changeSonStatus(Long strategyIndexDimensionId, Integer status) {
        if (BusinessConstants.DISABLE.equals(status)) {//失效会影响子级
            //先查再批量更新
            List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectSon(strategyIndexDimensionId);
            List<Long> strategyIndexDimensionIds = strategyIndexDimensionDTOS.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(strategyIndexDimensionIds)) {
                strategyIndexDimensionIds.add(strategyIndexDimensionId);
                List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOList = new ArrayList<>();
                for (Long id : strategyIndexDimensionIds) {
                    StrategyIndexDimensionDTO strategyIndexDimension = new StrategyIndexDimensionDTO();
                    strategyIndexDimension.setStrategyIndexDimensionId(id);
                    strategyIndexDimension.setStatus(status);
                    strategyIndexDimensionDTOList.add(strategyIndexDimension);
                }
                updateStrategyIndexDimensions(strategyIndexDimensionDTOList);
            }
        }
    }

    /**
     * 赋值
     *
     * @param strategyIndexDimensionDTOS      战略指标维度列表列表
     * @param parentStrategyIndexDimensionDTO 父级战略指标维度列表DTO
     */
    private void setStrategyAncestors(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, StrategyIndexDimensionDTO parentStrategyIndexDimensionDTO) {
        Long parentStrategyId = parentStrategyIndexDimensionDTO.getStrategyIndexDimensionId();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            strategyIndexDimensionDTO.setAncestors((StringUtils.isNull(parentStrategyIndexDimensionDTO.getAncestors()) ? "" : parentStrategyIndexDimensionDTO.getAncestors() + ",") + parentStrategyId);
            strategyIndexDimensionDTO.setParentIndexDimensionId(parentStrategyId);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS()))
                setStrategyAncestors(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), strategyIndexDimensionDTO);
        }
    }

    /**
     * 处理战略指标维度表
     *
     * @param strategyIndexDimensionDTOS      战略指标维度表
     * @param parentStrategyIndexDimensionDTO 父级战略指标维度DTO
     * @param strategyIndexDimensionIds       战略指标维度ID集合
     */
    private void operateStrategyList(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, StrategyIndexDimensionDTO parentStrategyIndexDimensionDTO, List<Long> strategyIndexDimensionIds) {
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            Long strategyIndexDimensionId = strategyIndexDimensionDTO.getStrategyIndexDimensionId();
            if (StringUtils.isNull(strategyIndexDimensionId)) {
                StrategyIndexDimensionDTO insertStrategyIndexDimension = insertStrategyIndexDimension(strategyIndexDimensionDTO);
                strategyIndexDimensionId = insertStrategyIndexDimension.getStrategyIndexDimensionId();
            } else if (strategyIndexDimensionIds.contains(strategyIndexDimensionId)) {
                editStrategyIndexDimension(strategyIndexDimensionDTO);
            } else {
                throw new ServiceException("战略指标配置维度部分数据已不存在");
            }
            strategyIndexDimensionDTO.setStrategyIndexDimensionId(strategyIndexDimensionId);
            strategyIndexDimensionDTO.setParentIndexDimensionId(parentStrategyIndexDimensionDTO.getStrategyIndexDimensionId());
            List<StrategyIndexDimensionDTO> sonStrategyIndexDimensionDTOS = strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS();
            if (StringUtils.isNotEmpty(sonStrategyIndexDimensionDTOS)) {
                operateStrategyList(sonStrategyIndexDimensionDTOS, strategyIndexDimensionDTO, strategyIndexDimensionIds);
            }
        }
    }

    /**
     * 更新战略指标维度表
     *
     * @param strategyIndexDimensionDTO 战略指标维度DTO
     */
    private void editStrategyIndexDimension(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        strategyIndexDimensionMapper.updateStrategyIndexDimension(strategyIndexDimension);
    }

    /**
     * 逻辑批量删除战略指标维度表
     *
     * @param strategyIndexDimensionIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(List<Long> strategyIndexDimensionIds) {
        if (StringUtils.isEmpty(strategyIndexDimensionIds)) {
            throw new ServiceException("请选择战略指标维度");
        }
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionIds(strategyIndexDimensionIds);
        if (strategyIndexDimensionDTOS.size() != strategyIndexDimensionIds.size()) {
            throw new ServiceException("部分战略指标维度已被删除");
        }
        isQuote(strategyIndexDimensionIds);
        return strategyIndexDimensionMapper.logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(strategyIndexDimensionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 判断是否被引用
     *
     * @param strategyIndexDimensionIds 战略指标维度ID集合
     */
    private void isQuote(List<Long> strategyIndexDimensionIds) {
        Set<Long> strategyIndexDimensionIdSet = new HashSet<>(strategyIndexDimensionIds);
        List<StrategyIndexDimensionDTO> sonStrategyIndexDimensionDTOList = strategyIndexDimensionMapper.selectSons(strategyIndexDimensionIds);
        if (StringUtils.isNotEmpty(sonStrategyIndexDimensionDTOList)) {
            strategyIndexDimensionIdSet.addAll(sonStrategyIndexDimensionDTOList.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toSet()));
        }
        // 战略举措清单
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureByStrategyIndexDimensionIds(new ArrayList<>(strategyIndexDimensionIdSet));
        if (StringUtils.isNotEmpty(strategyMeasureDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        // 战略衡量指标
        List<StrategyMetricsDTO> strategyMetricsDTOS = strategyMetricsMapper.selectStrategyMetricsByStrategyIndexDimensionIds(new ArrayList<>(strategyIndexDimensionIdSet));
        if (StringUtils.isNotEmpty(strategyMetricsDTOS)) {
            throw new ServiceException("数据被引用！");
        }
    }

    /**
     * 物理删除战略指标维度表信息
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyIndexDimensionByStrategyIndexDimensionId(Long strategyIndexDimensionId) {
        return strategyIndexDimensionMapper.deleteStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimensionId);
    }

    /**
     * 逻辑删除战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionId(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        Long strategyIndexDimensionId = strategyIndexDimensionDTO.getStrategyIndexDimensionId();
        if (StringUtils.isNull(strategyIndexDimensionId)) {
            throw new ServiceException("请传入ID");
        }
        StrategyIndexDimensionDTO strategyIndexDimensionDTOById = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimensionId);
        if (StringUtils.isNull(strategyIndexDimensionDTOById)) {
            throw new ServiceException("当前战略指标维度已不存在");
        }
        isQuote(Collections.singletonList(strategyIndexDimensionId));
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        strategyIndexDimension.setStrategyIndexDimensionId(strategyIndexDimensionDTO.getStrategyIndexDimensionId());
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        return strategyIndexDimensionMapper.logicDeleteStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimension);
    }

    /**
     * 物理删除战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */

    @Override
    public int deleteStrategyIndexDimensionByStrategyIndexDimensionId(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        return strategyIndexDimensionMapper.deleteStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimension.getStrategyIndexDimensionId());
    }

    /**
     * 物理批量删除战略指标维度表
     *
     * @param strategyIndexDimensionDtos 需要删除的战略指标维度表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyIndexDimensionByStrategyIndexDimensionIds(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDtos) {
            stringList.add(strategyIndexDimensionDTO.getStrategyIndexDimensionId());
        }
        return strategyIndexDimensionMapper.deleteStrategyIndexDimensionByStrategyIndexDimensionIds(stringList);
    }

    /**
     * 批量新增战略指标维度表信息
     *
     * @param strategyIndexDimensionDtos 战略指标维度表对象
     */

    public List<StrategyIndexDimension> insertStrategyIndexDimensions(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos) {
        List<StrategyIndexDimension> strategyIndexDimensionList = new ArrayList<>();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDtos) {
            StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
            strategyIndexDimension.setCreateBy(SecurityUtils.getUserId());
            strategyIndexDimension.setCreateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
            strategyIndexDimension.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyIndexDimensionList.add(strategyIndexDimension);
        }
        strategyIndexDimensionMapper.batchStrategyIndexDimension(strategyIndexDimensionList);
        return strategyIndexDimensionList;
    }

    /**
     * 批量修改战略指标维度表信息
     *
     * @param strategyIndexDimensionDtos 战略指标维度表对象
     */

    public List<StrategyIndexDimension> updateStrategyIndexDimensions(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos) {
        List<StrategyIndexDimension> strategyIndexDimensionList = new ArrayList<>();
        setListWithDTO(strategyIndexDimensionDtos, strategyIndexDimensionList);
        strategyIndexDimensionMapper.updateStrategyIndexDimensions(strategyIndexDimensionList);
        return strategyIndexDimensionList;
    }


    /**
     * 为需要更新的list赋值
     *
     * @param strategyIndexDimensionDTOS dtoList
     * @param strategyIndexDimensionList 更新list
     */
    private void setListWithDTO(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, List<StrategyIndexDimension> strategyIndexDimensionList) {
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
            strategyIndexDimension.setCreateBy(SecurityUtils.getUserId());
            strategyIndexDimension.setCreateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
            strategyIndexDimensionList.add(strategyIndexDimension);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS())) {
                this.setListWithDTO(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), strategyIndexDimensionList);
            }
        }
    }
}

