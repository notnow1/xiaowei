package net.qixiaowei.system.controller;

import java.util.List;

import net.qixiaowei.system.service.ISysLoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.api.domain.SysLoginInfo;

/**
 * 系统访问记录
 * 
 * 
 */
@RestController
@RequestMapping("/loginInfo")
public class SysLoginInfoController extends BaseController
{
    @Autowired
    private ISysLoginInfoService loginInfoService;

    @Autowired
    private RedisService redisService;

    @RequiresPermissions("system:loginInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginInfo loginInfo)
    {
        startPage();
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        return getDataTable(list);
    }

    @RequiresPermissions("system:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        return toAjax(loginInfoService.deleteLoginInfoByIds(infoIds));
    }

    @RequiresPermissions("system:loginInfo:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        loginInfoService.cleanLoginInfo();
        return AjaxResult.success();
    }

    @RequiresPermissions("system:loginInfo:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName)
    {
        redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + userName);
        return success();
    }

    @InnerAuth
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysLoginInfo loginInfo)
    {
        return toAjax(loginInfoService.insertLoginInfo(loginInfo));
    }
}
