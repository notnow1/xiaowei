package net.qixiaowei.job.targetManager;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.targetManager.TimeDimension;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteTargetDecomposeHistoryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 目标分解相关job
 */
@Component
public class TargetDecomposeJob {

    @Resource
    private RemoteTargetDecomposeHistoryService remoteTargetDecomposeHistoryService;

    /**
     * 滚动预测生成【年度】历史版本
     */
    @XxlJob("createTargetDecomposeHistoryOfYear")
    public void createTargetDecomposeHistoryOfYear() throws Exception {
        R<?> result = remoteTargetDecomposeHistoryService.cronCreateHistoryList(TimeDimension.YEAR.getCode(), SecurityConstants.INNER);
        if (R.SUCCESS != result.getCode()) {
            XxlJobHelper.handleFail("滚动预测生成【年度】历史版本失败:" + result.getMsg());
        }
        XxlJobHelper.log("滚动预测生成【年度】历史版本成功");
    }

    /**
     * 滚动预测生成【半年度】历史版本
     */
    @XxlJob("createTargetDecomposeHistoryOfSemiAnnual")
    public void createTargetDecomposeHistoryOfSemiAnnual() throws Exception {
        R<?> result = remoteTargetDecomposeHistoryService.cronCreateHistoryList(TimeDimension.SEMI_ANNUAL.getCode(), SecurityConstants.INNER);
        if (R.SUCCESS != result.getCode()) {
            XxlJobHelper.handleFail("滚动预测生成【半年度】历史版本失败:" + result.getMsg());
        }
        XxlJobHelper.log("滚动预测生成【半年度】历史版本成功");
    }

    /**
     * 滚动预测生成【季度】历史版本
     */
    @XxlJob("createTargetDecomposeHistoryOfQuarter")
    public void createTargetDecomposeHistoryOfQuarter() throws Exception {
        R<?> result = remoteTargetDecomposeHistoryService.cronCreateHistoryList(TimeDimension.QUARTER.getCode(), SecurityConstants.INNER);
        if (R.SUCCESS != result.getCode()) {
            XxlJobHelper.handleFail("滚动预测生成【季度】历史版本失败:" + result.getMsg());
        }
        XxlJobHelper.log("滚动预测生成【季度】历史版本成功");
    }

    /**
     * 滚动预测生成【月度】历史版本
     */
    @XxlJob("createTargetDecomposeHistoryOfMonthly")
    public void createTargetDecomposeHistoryOfMonthly() throws Exception {
        R<?> result = remoteTargetDecomposeHistoryService.cronCreateHistoryList(TimeDimension.MONTHLY.getCode(), SecurityConstants.INNER);
        if (R.SUCCESS != result.getCode()) {
            XxlJobHelper.handleFail("滚动预测生成【月度】历史版本失败:" + result.getMsg());
        }
        XxlJobHelper.log("滚动预测生成【月度】历史版本成功");
    }

    /**
     * 滚动预测生成【周】历史版本
     */
    @XxlJob("createTargetDecomposeHistoryOfWeekly")
    public void createTargetDecomposeHistoryOfWeekly() throws Exception {
        R<?> result = remoteTargetDecomposeHistoryService.cronCreateHistoryList(TimeDimension.WEEKLY.getCode(), SecurityConstants.INNER);
        if (R.SUCCESS != result.getCode()) {
            XxlJobHelper.handleFail("滚动预测生成【周】历史版本失败:" + result.getMsg());
        }
        XxlJobHelper.log("滚动预测生成【周】历史版本成功");
    }

}
