## EasyExcel 简化封装

简化 Controller 使用 EasyExcel 导出 Excel

## 声明式导出

### 下载模板和上传导入数据
#### 下载模板（基于文件模板）
该示例将导出原本的模板文件，若要填充字典等数据，请参考 **导出数据到 xlsx 中（填充数据到多个sheet）**
```
@ExcelResponse(
        fileName = "XXX导入模板",
        fileNameTimeSuffix = false,
        template = "excel/template.xlsx"
)
@GetMapping("/downloadTemplate")
public void downloadTemplate() {
}
```

#### 下载模板（基于实体类）
可选写入处理器：
- DictOptionWriteHandler      -- 基于 `@ExcelOption` 设置单元格下拉框
- DateFormatStyleWriteHandler -- 基于 `com.alibaba.excel.annotation.format.DateTimeFormat` 设置单元格日期格式
- TitleStatStyleWriteHandler  -- 设置表头单元格中的 `*` 变为红色
```
@ExcelResponse(
        fileName = "XXX导入模板",
        fileNameTimeSuffix = false,
        inMemory = true,
        writeSheets = @WriteSheetParam(sheetName = "sheet1", head = TemplateExcel.class)
)
@GetMapping("/downloadTemplate")
public void downloadTemplate() {
    ExcelHandleHelper.registerWriteHandler(
            new LongestMatchColumnWidthStyleStrategy(),
            new DictOptionWriteHandler(),
            new DateFormatStyleWriteHandler(),
            new TitleStatStyleWriteHandler());
}
```

#### 上传文件（少量数据）
以下时最简单的读取，少量数据；若数据量过大，建议使用 `ReadListener`
```
@PostMapping("/uploadExcel")
public void uploadExcel(MultipartFile file) throws IOException {
    List<Object> objects = EasyExcel.read(file.getInputStream())
            .head(TemplateExcel.class)
            .doReadAllSync();

    for (Object object : objects) {
        System.out.println(object);
    }
}
```

### 导出数据到 xlsx 中（写入数据）
```
@ExcelResponse(
        fileName = "XXX记录导出",
        writeSheets = @WriteSheetParam(sheetName = "sheetNameHR", head = DownloadData.class)
)
@GetMapping("/download")
public List<DownloadData> download() {
    return data();
}
```

### 导出数据到 xlsx 中（写入数据到多个sheet）
```
@ExcelResponse(fileName = "XXX记录导出")
@GetMapping("/downloadByExcelWriterProcess")
public ExcelWriterProcess downloadByBuilder() {
    return excelWriter -> {
        WriteSheet writeSheet = EasyExcel.writerSheet("配置信息")
                .head(ConfigInfo.class)
                .build();
        excelWriter.write(configInfo(), writeSheet);
        WriteSheet writeSheet2 = EasyExcel.writerSheet("使用记录")
                .head(ConfigUseRecord.class)
                .build();
        excelWriter.write(configUseRecord(), writeSheet2);
    };
}
```

### 导出数据到 xlsx 中（填充数据到多个sheet）
excle 单元格填充格式：{data1.name}，其中name是写入对象 DownloadData 的字段
```
@ExcelResponse(
        fileName = "XXX记录导出", template = "excel/fillComposite.xlsx"
)
@GetMapping("/downloadOneExcelMutiFill")
public ExcelWriterProcess downloadOneExcelMutiFill() {
    List<DownloadData> data = data();
    List<DownloadData> data2 = data();
    return excelWriter -> {
        excelWriter.fill(new FillWrapper("data1", data), EasyExcel.writerSheet("meta")
                .build()
        );
        excelWriter.fill(new FillWrapper("data2", data2), EasyExcel.writerSheet("meta")
                .build()
        );
        excelWriter.fill(new FillWrapper("data3", data2()), EasyExcel.writerSheet("meta-data3")
                .build()
        );
    };
}
```

