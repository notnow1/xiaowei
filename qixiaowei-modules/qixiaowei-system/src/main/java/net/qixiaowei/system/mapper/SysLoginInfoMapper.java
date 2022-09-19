package net.qixiaowei.system.mapper;

import java.util.List;
import net.qixiaowei.system.api.domain.SysLoginInfo;
import org.springframework.stereotype.Repository;

/**
 * 系统访问日志情况信息 数据层
 * 
 * 
 */
@Repository
public interface SysLoginInfoMapper
{
    /**
     * 新增系统登录日志
     * 
     * @param loginInfo 访问日志对象
     */
    int insertLoginInfo(SysLoginInfo loginInfo);

    /**
     * 查询系统登录日志集合
     * 
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    List<SysLoginInfo> selectLoginInfoList(SysLoginInfo loginInfo);

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    int deleteLoginInfoByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     * 
     * @return 结果
     */
    int cleanLoginInfo();
}
