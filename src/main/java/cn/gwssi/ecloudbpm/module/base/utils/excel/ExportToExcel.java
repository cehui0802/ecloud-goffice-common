package cn.gwssi.ecloudbpm.module.base.utils.excel;

import cn.gwssi.ecloudbpm.module.base.enums.BaseExceptionEnum;
import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportToExcel {

    private Workbook wb = null;

    private CellStyle cellStyle = null;

    private CellStyle headerStyle = null;

    private int headerRowIndex = 0;

    public int getHeaderRowIndex() {
        return headerRowIndex;
    }

    public void setHeaderRowIndex(int headerRowIndex) {
        this.headerRowIndex = headerRowIndex;
    }

    public ExportToExcel() {
        init();
    }

    private void init() {
        wb = new HSSFWorkbook();

        // 设置cell的样式
        cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        //设置首行样式
        headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
    }

    public void createTemplate(String sheetName, String[] columnNames, List<List<Object>> props, String desc) {
        if (StringUtils.isNotBlank(desc)) {
//        if (ToolUtil.isNotEmpty(desc)) {
            this.headerRowIndex = 1; //如果有说明性文字，模板第一行应该是说明性的文字，所以header放在第二行（index = 1）
        }
        createSheet(sheetName, columnNames, props);

        if (StringUtils.isNotBlank(desc)) {
            Sheet sheet = wb.getSheet(sheetName);
            //给第一行填充说明信息。
            CellRangeAddress rangeAddress = new CellRangeAddress(
                    0, //first row (0-based)
                    0, //last row  (0-based)
                    0, //first column (0-based)
                    columnNames.length - 1  //last column  (0-based)
            );

            // 使用RegionUtil类为合并后的单元格添加边框
            RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet); // 下边框
            RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet); // 左边框
            RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet); // 有边框
            RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet); // 上边框

            sheet.addMergedRegion(rangeAddress);

            Row nRow_first = sheet.createRow(0);
            int count = StringUtils.countMatches(desc, "\n");
            nRow_first.setHeightInPoints((count + 2) * sheet.getDefaultRowHeightInPoints());
            Cell nCell = nRow_first.createCell(0);
            CellStyle cs = wb.createCellStyle();
            cs.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cs.setWrapText(true);
            nCell.setCellValue(desc);
            nCell.setCellStyle(cs);
        }
    }

    public void createSheet(String sheetName, String[] columnNames, Collection<?> collection, List<String> fields) {
        Sheet sheet = wb.createSheet(sheetName);
        createHeaderRow(columnNames, sheet);

        //其他行
        int rowIndex = this.headerRowIndex + 1;
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Row dataRow = sheet.createRow(rowIndex);
            Object data = iterator.next();
            Class clazz = data.getClass();
//            Field [] fields = clazz.getDeclaredFields();
            int cellIndex = 0;

            for (String field : fields) {
                Cell cell = dataRow.createCell(cellIndex);
                cell.setCellStyle(cellStyle);
                String methodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                Object value = null;
                try {
                    Method method = clazz.getMethod(methodName, new Class[]{});
                    Type type = method.getGenericReturnType();
                    String typeName = type.getTypeName();
                    value = method.invoke(data, new Object[]{});
                    if (typeName.endsWith("Date") && !ObjectUtils.isEmpty(value)) {
                        value = convertDate((Date) value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (ObjectUtils.isEmpty(value)) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(value + "");
                }
                cellIndex++;
            }
            rowIndex++;
        }
    }

    public void createSheet(String sheetName, String[] columnNames, List<List<Object>> props) {
        Sheet sheet = wb.createSheet(sheetName);

        //表头行
        createHeaderRow(columnNames, sheet);

        //其他行
        int rowNo = this.headerRowIndex + 1;
        for (List<Object> prop : props) {
            int colNo = 0;
            Row nRow = sheet.createRow(rowNo++);
            for (Object entry : prop) {
                Cell cell = nRow.createCell(colNo++);
                cell.setCellValue(entry.toString());
                cell.setCellStyle(cellStyle);
            }
        }
    }

    private String convertDate(Date date) {
        if (date == null || "".equals(date)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String dateStr = date.toString();
        String formatStr = "";
        try {
            formatStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date) formatter.parse(dateStr));
            String hour = dateStr.substring(11, 13);
            formatStr = formatStr.substring(0, 11) + hour + formatStr.substring(13, formatStr.length());
        } catch (Exception e) {

        } finally {
            return formatStr;
        }
    }

    private void createHeaderRow(String[] columnNames, Sheet sheet) {
        Row nRow = sheet.createRow(this.headerRowIndex);
        Cell nCell = nRow.createCell(0);
        nRow.setHeight((short) (15 * 35));
        for (int i = 0; i < columnNames.length; i++) {
            nCell = nRow.createCell(i);
            nCell.setCellValue(columnNames[i]);
            nCell.setCellStyle(this.headerStyle);
            sheet.setColumnWidth(i, (25 * 256));
        }
    }

    public Workbook export(String fileName, HttpServletResponse response) throws BusinessException {
        download(wb, response, fileName);
        return wb;
    }

    public static void download(Workbook wb, HttpServletResponse response, String filename) {
        OutputStream out;
        String decodeFileName;
        try {
            out = response.getOutputStream();
            decodeFileName = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        } catch (Exception e) {
            throw new BusinessException("导出文件失败" + e.getMessage(), BaseExceptionEnum.FILE_EXPORT_FAILED);
        }
        response.setContentType("application/octet-stream;charset=UTF-8");//x-xls
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Pragma", "public");
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s", decodeFileName));
        write(wb, out);
    }

    private static void write(Workbook wb, OutputStream out) {
        try {
            if (null != out) {
                wb.write(out);
                out.flush();
            }
        } catch (IOException e) {
            throw new BusinessException("导出文件失败" + e.getMessage(), BaseExceptionEnum.FILE_EXPORT_FAILED);
        } finally {
            try {
                if (null != wb)
                    wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != out)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setWb(Workbook wb) {
        this.wb = wb;
    }

    public Workbook getWb() {
        return this.wb;
    }
}
