package net.qixiaowei.system.manage.service.impl.basic;

import java.util.*;

import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.enums.system.DictionaryTypeCode;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryData;
import net.qixiaowei.system.manage.mapper.basic.DictionaryDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryType;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
import net.qixiaowei.system.manage.mapper.basic.DictionaryTypeMapper;
import net.qixiaowei.system.manage.service.basic.IDictionaryTypeService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * DictionaryTypeService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-15
 */
@Service
public class DictionaryTypeServiceImpl implements IDictionaryTypeService {

    private static final List<DictionaryData> PRODUCT_CATEGORY = new ArrayList<>(6);

    private static final List<DictionaryData> MARKET_INSIGHT_MACRO_VISUAL_ANGLE = new ArrayList<>(4);

    private static final List<DictionaryData> MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE = new ArrayList<>(3);

    private static final List<DictionaryData> MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY = new ArrayList<>(5);

    private static final List<DictionaryData> MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM = new ArrayList<>(6);

    private static final List<DictionaryData> MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY = new ArrayList<>(4);

    private static final List<DictionaryData> MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE = new ArrayList<>(6);

    private static final List<DictionaryData> MARKET_INSIGHT_SELF_CAPACITY_FACTOR = new ArrayList<>(6);

    private static final List<DictionaryData> STRATEGY_MEASURE_SOURCE = new ArrayList<>(7);


    static {
        //初始化产品分类
        PRODUCT_CATEGORY.add(DictionaryData.builder().dictionaryLabel("通用件").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        PRODUCT_CATEGORY.add(DictionaryData.builder().dictionaryLabel("标准件").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        PRODUCT_CATEGORY.add(DictionaryData.builder().dictionaryLabel("自制件").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        PRODUCT_CATEGORY.add(DictionaryData.builder().dictionaryLabel("外购件").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        PRODUCT_CATEGORY.add(DictionaryData.builder().dictionaryLabel("外协件").dictionaryValue("5").defaultFlag(0).sort(5).status(BusinessConstants.NORMAL).build());
        PRODUCT_CATEGORY.add(DictionaryData.builder().dictionaryLabel("原材料").dictionaryValue("6").defaultFlag(0).sort(6).status(BusinessConstants.NORMAL).build());
        //初始化视角
        MARKET_INSIGHT_MACRO_VISUAL_ANGLE.add(DictionaryData.builder().dictionaryLabel("政策").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_MACRO_VISUAL_ANGLE.add(DictionaryData.builder().dictionaryLabel("经济").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_MACRO_VISUAL_ANGLE.add(DictionaryData.builder().dictionaryLabel("社会").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_MACRO_VISUAL_ANGLE.add(DictionaryData.builder().dictionaryLabel("技术").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        //初始化行业类型
        MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE.add(DictionaryData.builder().dictionaryLabel("改造型").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE.add(DictionaryData.builder().dictionaryLabel("替代型").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE.add(DictionaryData.builder().dictionaryLabel("创新型").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        //初始化客户类别
        MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY.add(DictionaryData.builder().dictionaryLabel("战略大客户").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY.add(DictionaryData.builder().dictionaryLabel("战略拓展客户").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY.add(DictionaryData.builder().dictionaryLabel("战略潜在客户").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY.add(DictionaryData.builder().dictionaryLabel("检验客户（普通客户）").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY.add(DictionaryData.builder().dictionaryLabel("空白客户（零星客户）").dictionaryValue("5").defaultFlag(0).sort(5).status(BusinessConstants.NORMAL).build());
        //初始化对比项目
        MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM.add(DictionaryData.builder().dictionaryLabel("品牌").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM.add(DictionaryData.builder().dictionaryLabel("客户关系").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM.add(DictionaryData.builder().dictionaryLabel("技术").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM.add(DictionaryData.builder().dictionaryLabel("成本").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM.add(DictionaryData.builder().dictionaryLabel("质量").dictionaryValue("5").defaultFlag(0).sort(5).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM.add(DictionaryData.builder().dictionaryLabel("服务").dictionaryValue("6").defaultFlag(0).sort(6).status(BusinessConstants.NORMAL).build());
        //初始化竞争对手类别
        MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY.add(DictionaryData.builder().dictionaryLabel("标杆企业").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY.add(DictionaryData.builder().dictionaryLabel("战略合作").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY.add(DictionaryData.builder().dictionaryLabel("战略竞争").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY.add(DictionaryData.builder().dictionaryLabel("一般竞争").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        //初始化竞争策略类型
        MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE.add(DictionaryData.builder().dictionaryLabel("先发制人策略").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE.add(DictionaryData.builder().dictionaryLabel("硬碰硬策略").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE.add(DictionaryData.builder().dictionaryLabel("迂回策略").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE.add(DictionaryData.builder().dictionaryLabel("各个击破策略").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE.add(DictionaryData.builder().dictionaryLabel("拖延策略").dictionaryValue("5").defaultFlag(0).sort(5).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE.add(DictionaryData.builder().dictionaryLabel("产品价格恶性竞争").dictionaryValue("6").defaultFlag(0).sort(6).status(BusinessConstants.NORMAL).build());
        //初始化能力要素
        MARKET_INSIGHT_SELF_CAPACITY_FACTOR.add(DictionaryData.builder().dictionaryLabel("品牌").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_SELF_CAPACITY_FACTOR.add(DictionaryData.builder().dictionaryLabel("客户关系").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_SELF_CAPACITY_FACTOR.add(DictionaryData.builder().dictionaryLabel("技术").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_SELF_CAPACITY_FACTOR.add(DictionaryData.builder().dictionaryLabel("成本").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_SELF_CAPACITY_FACTOR.add(DictionaryData.builder().dictionaryLabel("质量").dictionaryValue("5").defaultFlag(0).sort(5).status(BusinessConstants.NORMAL).build());
        MARKET_INSIGHT_SELF_CAPACITY_FACTOR.add(DictionaryData.builder().dictionaryLabel("服务").dictionaryValue("6").defaultFlag(0).sort(6).status(BusinessConstants.NORMAL).build());
        //初始化来源
        STRATEGY_MEASURE_SOURCE.add(DictionaryData.builder().dictionaryLabel("差距分析").dictionaryValue("1").defaultFlag(1).sort(1).status(BusinessConstants.NORMAL).build());
        STRATEGY_MEASURE_SOURCE.add(DictionaryData.builder().dictionaryLabel("看宏观").dictionaryValue("2").defaultFlag(0).sort(2).status(BusinessConstants.NORMAL).build());
        STRATEGY_MEASURE_SOURCE.add(DictionaryData.builder().dictionaryLabel("看行业").dictionaryValue("3").defaultFlag(0).sort(3).status(BusinessConstants.NORMAL).build());
        STRATEGY_MEASURE_SOURCE.add(DictionaryData.builder().dictionaryLabel("看客户").dictionaryValue("4").defaultFlag(0).sort(4).status(BusinessConstants.NORMAL).build());
        STRATEGY_MEASURE_SOURCE.add(DictionaryData.builder().dictionaryLabel("看对手").dictionaryValue("5").defaultFlag(0).sort(5).status(BusinessConstants.NORMAL).build());
        STRATEGY_MEASURE_SOURCE.add(DictionaryData.builder().dictionaryLabel("看自身").dictionaryValue("6").defaultFlag(0).sort(6).status(BusinessConstants.NORMAL).build());
        STRATEGY_MEASURE_SOURCE.add(DictionaryData.builder().dictionaryLabel("战略意图").dictionaryValue("7").defaultFlag(0).sort(7).status(BusinessConstants.NORMAL).build());
    }

    @Autowired
    private DictionaryTypeMapper dictionaryTypeMapper;

    @Autowired
    private DictionaryDataMapper dictionaryDataMapper;

    /**
     * 初始化数据
     *
     * @return 是否成功
     */
    @Override
    @Transactional
    public Boolean initData() {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //初始化
        for (DictionaryTypeCode dictionaryTypeCode : DictionaryTypeCode.values()) {
            if (!this.initDictionary(userId, nowDate, dictionaryTypeCode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查询字典类型表
     *
     * @param dictionaryTypeId 字典类型表主键
     * @return 字典类型表
     */
    @Override
    public DictionaryTypeDTO selectDictionaryTypeByDictionaryTypeId(Long dictionaryTypeId) {
        return dictionaryTypeMapper.selectDictionaryTypeByDictionaryTypeId(dictionaryTypeId);
    }

    /**
     * 根据type类型查询字典类型表
     *
     * @param dictionaryType 字典类型表主键
     * @return
     */
    @Override
    public DictionaryTypeDTO selectDictionaryTypeByDictionaryType(String dictionaryType) {
        return dictionaryTypeMapper.selectDictionaryTypeByDictionaryType(dictionaryType);
    }

    /**
     * 查询字典类型表列表
     *
     * @param dictionaryTypeDTO 字典类型表
     * @return 字典类型表
     */
    @Override
    public List<DictionaryTypeDTO> selectDictionaryTypeList(DictionaryTypeDTO dictionaryTypeDTO) {
        DictionaryType dictionaryType = new DictionaryType();
        BeanUtils.copyProperties(dictionaryTypeDTO, dictionaryType);
        return dictionaryTypeMapper.selectDictionaryTypeList(dictionaryType);
    }

    /**
     * 新增字典类型表
     *
     * @param dictionaryTypeDTO 字典类型表
     * @return 结果
     */
    @Override
    public DictionaryTypeDTO insertDictionaryType(DictionaryTypeDTO dictionaryTypeDTO) {
        DictionaryType dictionaryType = new DictionaryType();
        BeanUtils.copyProperties(dictionaryTypeDTO, dictionaryType);
        dictionaryType.setCreateBy(SecurityUtils.getUserId());
        dictionaryType.setCreateTime(DateUtils.getNowDate());
        dictionaryType.setUpdateTime(DateUtils.getNowDate());
        dictionaryType.setUpdateBy(SecurityUtils.getUserId());
        dictionaryType.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        dictionaryTypeMapper.insertDictionaryType(dictionaryType);
        dictionaryTypeDTO.setDictionaryTypeId(dictionaryType.getDictionaryTypeId());
        return dictionaryTypeDTO;
    }

    /**
     * 修改字典类型表
     *
     * @param dictionaryTypeDTO 字典类型表
     * @return 结果
     */
    @Override
    public int updateDictionaryType(DictionaryTypeDTO dictionaryTypeDTO) {
        DictionaryType dictionaryType = new DictionaryType();
        BeanUtils.copyProperties(dictionaryTypeDTO, dictionaryType);
        dictionaryType.setUpdateTime(DateUtils.getNowDate());
        dictionaryType.setUpdateBy(SecurityUtils.getUserId());
        return dictionaryTypeMapper.updateDictionaryType(dictionaryType);
    }

    /**
     * 逻辑批量删除字典类型表
     *
     * @param dictionaryTypeIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteDictionaryTypeByDictionaryTypeIds(List<Long> dictionaryTypeIds) {
        return dictionaryTypeMapper.logicDeleteDictionaryTypeByDictionaryTypeIds(dictionaryTypeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除字典类型表信息
     *
     * @param dictionaryTypeId 字典类型表主键
     * @return 结果
     */
    @Override
    public int deleteDictionaryTypeByDictionaryTypeId(Long dictionaryTypeId) {
        return dictionaryTypeMapper.deleteDictionaryTypeByDictionaryTypeId(dictionaryTypeId);
    }

    /**
     * 逻辑删除字典类型表信息
     *
     * @param dictionaryTypeDTO 字典类型表
     * @return 结果
     */
    @Override
    public int logicDeleteDictionaryTypeByDictionaryTypeId(DictionaryTypeDTO dictionaryTypeDTO) {
        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setDictionaryTypeId(dictionaryTypeDTO.getDictionaryTypeId());
        dictionaryType.setUpdateTime(DateUtils.getNowDate());
        dictionaryType.setUpdateBy(SecurityUtils.getUserId());
        return dictionaryTypeMapper.logicDeleteDictionaryTypeByDictionaryTypeId(dictionaryType);
    }

    /**
     * 物理删除字典类型表信息
     *
     * @param dictionaryTypeDTO 字典类型表
     * @return 结果
     */

    @Override
    public int deleteDictionaryTypeByDictionaryTypeId(DictionaryTypeDTO dictionaryTypeDTO) {
        DictionaryType dictionaryType = new DictionaryType();
        BeanUtils.copyProperties(dictionaryTypeDTO, dictionaryType);
        return dictionaryTypeMapper.deleteDictionaryTypeByDictionaryTypeId(dictionaryType.getDictionaryTypeId());
    }

    /**
     * 物理批量删除字典类型表
     *
     * @param dictionaryTypeDtos 需要删除的字典类型表主键
     * @return 结果
     */

    @Override
    public int deleteDictionaryTypeByDictionaryTypeIds(List<DictionaryTypeDTO> dictionaryTypeDtos) {
        List<Long> stringList = new ArrayList();
        for (DictionaryTypeDTO dictionaryTypeDTO : dictionaryTypeDtos) {
            stringList.add(dictionaryTypeDTO.getDictionaryTypeId());
        }
        return dictionaryTypeMapper.deleteDictionaryTypeByDictionaryTypeIds(stringList);
    }

    /**
     * 批量新增字典类型表信息
     *
     * @param dictionaryTypeDtos 字典类型表对象
     */

    @Override
    public int insertDictionaryTypes(List<DictionaryTypeDTO> dictionaryTypeDtos) {
        List<DictionaryType> dictionaryTypeList = new ArrayList();

        for (DictionaryTypeDTO dictionaryTypeDTO : dictionaryTypeDtos) {
            DictionaryType dictionaryType = new DictionaryType();
            BeanUtils.copyProperties(dictionaryTypeDTO, dictionaryType);
            dictionaryType.setCreateBy(SecurityUtils.getUserId());
            dictionaryType.setCreateTime(DateUtils.getNowDate());
            dictionaryType.setUpdateTime(DateUtils.getNowDate());
            dictionaryType.setUpdateBy(SecurityUtils.getUserId());
            dictionaryType.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            dictionaryTypeList.add(dictionaryType);
        }
        return dictionaryTypeMapper.batchDictionaryType(dictionaryTypeList);
    }

    /**
     * 批量修改字典类型表信息
     *
     * @param dictionaryTypeDtos 字典类型表对象
     */

    @Override
    public int updateDictionaryTypes(List<DictionaryTypeDTO> dictionaryTypeDtos) {
        List<DictionaryType> dictionaryTypeList = new ArrayList();

        for (DictionaryTypeDTO dictionaryTypeDTO : dictionaryTypeDtos) {
            DictionaryType dictionaryType = new DictionaryType();
            BeanUtils.copyProperties(dictionaryTypeDTO, dictionaryType);
            dictionaryType.setCreateBy(SecurityUtils.getUserId());
            dictionaryType.setCreateTime(DateUtils.getNowDate());
            dictionaryType.setUpdateTime(DateUtils.getNowDate());
            dictionaryType.setUpdateBy(SecurityUtils.getUserId());
            dictionaryTypeList.add(dictionaryType);
        }
        return dictionaryTypeMapper.updateDictionaryTypes(dictionaryTypeList);
    }

    /**
     * @description: 初始化枚举类
     * @Author: hzk
     * @date: 2022/12/13 10:47
     * @param: [userId, nowDate,dictionaryTypeCode]
     * @return: boolean
     **/
    public boolean initDictionary(Long userId, Date nowDate, DictionaryTypeCode dictionaryTypeCode) {
        Boolean initDictionary = false;
        //初始化字典类型
        DictionaryType dictionaryType = new DictionaryType();
        dictionaryType.setDictionaryType(dictionaryTypeCode.getCode());
        dictionaryType.setDictionaryName(dictionaryTypeCode.getInfo());
        dictionaryType.setMenuZerothName(dictionaryTypeCode.getMenuZerothName());
        dictionaryType.setMenuFirstName(dictionaryTypeCode.getMenuFirstName());
        dictionaryType.setMenuSecondName(dictionaryTypeCode.getMenuSecondName());
        dictionaryType.setRemark("");
        dictionaryType.setStatus(BusinessConstants.NORMAL);
        dictionaryType.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        dictionaryType.setCreateBy(userId);
        dictionaryType.setUpdateBy(userId);
        dictionaryType.setCreateTime(nowDate);
        dictionaryType.setUpdateTime(nowDate);
        boolean dictionaryTypeSuccess = dictionaryTypeMapper.insertDictionaryType(dictionaryType) > 0;
        if (!dictionaryTypeSuccess) {
            return false;
        }
        Long dictionaryTypeId = dictionaryType.getDictionaryTypeId();
        //初始化字典数据
        List<DictionaryData> dictionaryData = new ArrayList<>();
        List<DictionaryData> initDictionaryDataList = this.getInitDictionaryDataList(dictionaryTypeCode);
        if (StringUtils.isNotEmpty(initDictionaryDataList)) {
            for (DictionaryData initDictionaryData : initDictionaryDataList) {
                initDictionaryData.setDictionaryTypeId(dictionaryTypeId);
                initDictionaryData.setRemark("");
                initDictionaryData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                initDictionaryData.setCreateBy(userId);
                initDictionaryData.setUpdateBy(userId);
                initDictionaryData.setCreateTime(nowDate);
                initDictionaryData.setUpdateTime(nowDate);
                dictionaryData.add(initDictionaryData);
            }
            initDictionary = dictionaryDataMapper.batchDictionaryData(dictionaryData) > 0;
        }
        return initDictionary;
    }

    /**
     * @description: 根据枚举获取到需要初始化对应的字典列表
     * @Author: hzk
     * @date: 2023/3/3 17:24
     * @param: [dictionaryTypeCode]
     * @return: java.util.List<net.qixiaowei.system.manage.api.domain.basic.DictionaryData>
     **/
    private List<DictionaryData> getInitDictionaryDataList(DictionaryTypeCode dictionaryTypeCode) {
        List<DictionaryData> initDictionaryDataList;
        switch (dictionaryTypeCode) {
            case PRODUCT_CATEGORY: {
                initDictionaryDataList = PRODUCT_CATEGORY;
                break;
            }
            case MARKET_INSIGHT_MACRO_VISUAL_ANGLE: {
                initDictionaryDataList = MARKET_INSIGHT_MACRO_VISUAL_ANGLE;
                break;
            }
            case MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE: {
                initDictionaryDataList = MARKET_INSIGHT_INDUSTRY_INDUSTRY_TYPE;
                break;
            }
            case MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY: {
                initDictionaryDataList = MARKET_INSIGHT_CUSTOMER_CUSTOMER_CATEGORY;
                break;
            }
            case MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM: {
                initDictionaryDataList = MARKET_INSIGHT_OPPONENT_COMPARISON_ITEM;
                break;
            }
            case MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY: {
                initDictionaryDataList = MARKET_INSIGHT_OPPONENT_COMPETITOR_CATEGORY;
                break;
            }
            case MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE: {
                initDictionaryDataList = MARKET_INSIGHT_OPPONENT_COMPETITION_STRATEGY_TYPE;
                break;
            }
            case MARKET_INSIGHT_SELF_CAPACITY_FACTOR: {
                initDictionaryDataList = MARKET_INSIGHT_SELF_CAPACITY_FACTOR;
                break;
            }
            case STRATEGY_MEASURE_SOURCE: {
                initDictionaryDataList = STRATEGY_MEASURE_SOURCE;
                break;
            }
            default:
                initDictionaryDataList = null;
                break;
        }
        return initDictionaryDataList;
    }
}

