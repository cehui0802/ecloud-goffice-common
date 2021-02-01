package com.telecom.ecloudbpm.goffice.common.utils.excel;

import com.telecom.ecloudbpm.goffice.common.enums.DateFormatEnum;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportFromExcel {
    private final static Logger logger = LoggerFactory.getLogger(ImportFromExcel.class);

    private DateFormatEnum DATE_FORMAT_ENUM = DateFormatEnum.YYYY_MM_DD_HH_MM_SS;

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_ENUM.getName());

    DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_FORMAT_ENUM.getName());

    private final Map<String, Object> DATA_CACHE = new HashMap<>();

    private int readFromRow = 1;

    public int getReadFromRow() {
        return readFromRow;
    }

    public void setReadFromRow(int readFromRow) {
        this.readFromRow = readFromRow;
    }

    public void setDateFormat(DateFormatEnum dateFormatEnum) {
        this.DATE_FORMAT = new SimpleDateFormat(dateFormatEnum.getName());
    }

    public DateFormatEnum getDateFormatEnum() {
        return DATE_FORMAT_ENUM;
    }

    public void setDateFormatEnum(DateFormatEnum dateFormatEnum) {
        this.DATE_FORMAT_ENUM = dateFormatEnum;
        this.DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(dateFormatEnum.getName());
    }

    /**
     * 获取excel表中的固定行和前几列的数据
     *
     * @param inputStream
     * @param row         固定哪一行
     * @param column      固定前多少列
     */
    public List<String> getDataForCell(InputStream inputStream, int row, int column) {
        List<String> list = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            if (rows >= row) {
                Row rowData = sheet.getRow(row);
                int columns = rowData.getPhysicalNumberOfCells();
                if (columns >= column) {
                    for (int i = 0; i < column; i++) {
                        Cell cellData = rowData.getCell(i);
                        cellData.setCellType(CellType.STRING);
                        list.add(cellData.getStringCellValue());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("excel parse exception");
        } finally {
            try {
                inputStream.close();//流关闭
            } catch (Exception e2) {
                logger.error("io close exception");
            }
        }
        return list;
    }

    public <T> List<T> convertToList(Class<T> clazz, InputStream inputStream, String[] inputFields) {
        List<T> list = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        try {
            //创建工作簿
            Workbook workbook = WorkbookFactory.create(inputStream);
            //创建工作表sheet
            Sheet sheet = workbook.getSheetAt(0);
            //sheet中数据的行数
            int rows = sheet.getPhysicalNumberOfRows();
            //表头单元格个数
            int cells = sheet.getRow(this.readFromRow).getPhysicalNumberOfCells();
            logger.info("cell num is {}", cells);
            for (int i = 0; i < cells; i++) {
                try {
                    cacheFieldNameWithSetMethod(clazz, clazz.getDeclaredField(inputFields[i]));
                } catch (NoSuchFieldException e) {
                    logger.warn("NoSuchFieldException", e.getMessage());
                    try {
                        cacheFieldNameWithSetMethod(clazz.getSuperclass(), clazz.getSuperclass().getDeclaredField(inputFields[i]));
                    } catch (Exception ex) {
                        logger.warn("exception found", ex);
                    }

                } catch (NoSuchMethodException e) {
                    logger.warn("noSuchMethodException", e);
                }
            }
            for (int i = readFromRow; i < rows; i++) {
                Row row = sheet.getRow(i);
                T obj = clazz.getConstructor(new Class[]{}).newInstance(new Object[]{});
                for (int index = 0; index < cells; index++) {
                    Cell cell = row.getCell(index);
                    if (cell == null) {
                        cell = row.createCell(index);
                    }
                    Map<String, Object> result = (Map) DATA_CACHE.get(inputFields[index]);
                    if (result != null) {
                        Method method = (Method) result.get("method");
                        Field field = (Field) result.get("field");
                        setAttrValue(method, obj, getCellValue(cell), field.getType());
                    }
                }
                list.add(obj);
            }
        } catch (Exception e) {
            logger.error("excel parse exception");
        } finally {
            try {
                inputStream.close();//流关闭
                long currentTime1 = System.currentTimeMillis();
                logger.info("import parse time interval:" + (currentTime1 - currentTime));
            } catch (Exception e2) {
                logger.error("io close exception");
            }
        }
        return list;
    }

    private <T> void cacheFieldNameWithSetMethod(Class<T> clazz, Field declaredField) throws NoSuchFieldException, NoSuchMethodException {
        Field field = declaredField;
        String methodName = buildMethodName(field.getName());
        Method method = clazz.getMethod(methodName, new Class[]{field.getType()});
        Map<String, Object> temp = new HashMap<>();
        temp.put("method", method);
        temp.put("field", field);
        DATA_CACHE.put(field.getName(), temp);
//        DATA_CACHE.put(field.getName(), methodName);
    }

    //构建set方法名称
    private String buildMethodName(String attr) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(attr);
        StringBuilder sb = new StringBuilder("set");
        if (matcher.find() && attr.charAt(0) != '_') {
            sb.append(matcher.replaceFirst(matcher.group().toUpperCase()));
        } else {
            sb.append(attr);
        }
        return sb.toString();
    }

    private void setAttrValue(Method method, Object obj, String val, Class<?> type) {
        try {
            //参数类型转换 把String放到第一个if里是因为我们导入的数据大都是字符串类型，这样可以减少不必要的判断次数，当数据量很大时候可以体现出来
            if (type == String.class) {
                method.invoke(obj, type.cast(val));
            } else if (type == Date.class) {
                Date date = null;
                try {
                    if (DateFormatEnum.YYYY_MM_DD.getName().equals(this.DATE_FORMAT_ENUM.getName())) {
                        LocalDate localDate = LocalDate.parse(val, DATE_TIME_FORMAT);
                        ZoneId zoneId = ZoneId.systemDefault();
                        ZonedDateTime zdt = localDate.atStartOfDay().atZone(zoneId);
                        date = Date.from(zdt.toInstant());
                    } else {
                        LocalDateTime localDateTime = LocalDateTime.parse(val, DATE_TIME_FORMAT);
                        ZoneId zoneId = ZoneId.systemDefault();
                        ZonedDateTime zdt = localDateTime.atZone(zoneId);
                        date = Date.from(zdt.toInstant());
                    }
                } catch (Exception e) {
                    logger.error("日期转换错误", e);
                }
                method.invoke(obj, date);
            } else if (type == int.class || type == Integer.class || type == long.class || type == Long.class) {
                val = val.substring(0, val.lastIndexOf(".") == -1 ? val.length() : val.lastIndexOf("."));
                method.invoke(obj, Integer.valueOf(val));
            } else if (type == float.class || type == Float.class) {
                method.invoke(obj, Float.valueOf(val));
            } else if (type == double.class || type == Double.class) {
                method.invoke(obj, Double.valueOf(val));
            } else if (type == byte.class || type == Byte.class) {
                method.invoke(obj, Byte.valueOf(val));
            } else if (type == boolean.class || type == Boolean.class) {
                method.invoke(obj, Boolean.valueOf(val));
            } else {
                method.invoke(obj, type.cast(val));
            }
        } catch (Exception e) {
            logger.error("参数转换错误");
        }
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return DATE_FORMAT.format(date);
                } else {
                    return NumberToTextConverter.toText(cell.getNumericCellValue());
                }
            case BOOLEAN:
                Object retBool = cell.getBooleanCellValue();
                return retBool.toString();
            case FORMULA:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return DATE_FORMAT.format(date);
                }
                return cell.getCellFormula();
            case ERROR:
                Object retError = cell.getErrorCellValue();
                return retError.toString();
            case BLANK:
                return "";
            default:
                return cell.toString();
        }
    }
}
