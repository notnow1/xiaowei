package net.qixiaowei.system.manage.excel.basic;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;

public class CustomVerticalCellStyleStrategy extends AbstractVerticalCellStyleStrategy {
    List<List<String>> levelList;

    public CustomVerticalCellStyleStrategy(List<List<String>> levelList){
        this.levelList=levelList;
    }


    //设置标题栏的列样式
    @Override
    protected WriteCellStyle headCellStyle(Head head) {
        //内容样式策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        if (StringUtils.isNotEmpty(levelList)){
            //靠右
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //设置 自动换行
            contentWriteCellStyle.setWrapped(true);
            // 字体策略
            WriteFont contentWriteFont = new WriteFont();
            // 字体大小
            contentWriteFont.setFontHeightInPoints((short) 11);
            contentWriteCellStyle.setWriteFont(contentWriteFont);
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        }

        return contentWriteCellStyle;
    }
    //设置表格内容的列样式
    @Override
    protected WriteCellStyle contentCellStyle(Head head) {

        return new WriteCellStyle();
    }
}
