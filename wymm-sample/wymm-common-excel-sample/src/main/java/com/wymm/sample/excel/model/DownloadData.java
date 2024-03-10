package com.wymm.sample.excel.model;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.metadata.data.FormulaData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.wymm.common.excel.annotation.ExcelOption;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基础数据类
 **/
@Getter
@Setter
@EqualsAndHashCode
public class DownloadData {
    
    @ExcelProperty("序号")
    private WriteCellData<String> idx;
    
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
    
    @DateTimeFormat(DatePattern.NORM_DATETIME_PATTERN)
    @ExcelProperty("时间")
    private LocalDateTime time;
    
    @ExcelProperty("可选下拉框")
    //@ExcelOption(options = {"选项a", "选项b"})
    @ExcelOption("DICT")
    private String nameOptional;
    
    public Double getDoubleData() {
        //throw new RuntimeException("日期标题 异常");
        return doubleData;
    }
    
    public DownloadData() {
        WriteCellData<String> formula = new WriteCellData<>();
        FormulaData formulaData = new FormulaData();
        formulaData.setFormulaValue("ROW()-1\n");
        formula.setFormulaData(formulaData);
        idx = formula;
    }
}
