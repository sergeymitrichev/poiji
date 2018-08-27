package com.poiji.bind.mapping;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import com.poiji.bind.Unmarshaller;
import com.poiji.exception.IllegalCastException;
import com.poiji.exception.PoijiInstantiationException;
import com.poiji.option.PoijiOptions;
import com.poiji.util.Casting;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

/**
 * This is the main class that converts the excel sheet fromExcel Java object
 * Created by hakan on 16/01/2017.
 */
abstract class HSSFUnmarshaller implements Unmarshaller {

    private final DataFormatter dataFormatter;
    protected final PoijiOptions options;
    private final Casting casting;
    private Map<String, Integer> titles;
    private Map<Integer, String> _titles;

    HSSFUnmarshaller(PoijiOptions options) {
        this.options = options;
        dataFormatter = new DataFormatter();
        titles = new HashMap<String, Integer>();
        _titles = new HashMap<Integer, String>();
        casting = Casting.getInstance();
    }

    @Override
    public <T> void unmarshal(Class<T> type, Consumer<? super T> consumer) {
        Workbook workbook = workbook();
        Sheet sheet = workbook.getSheetAt(options.sheetIndex());

        int skip = options.skip();
        int maxPhysicalNumberOfRows = sheet.getPhysicalNumberOfRows() + 1 - skip;

        loadColumnTitles(sheet, maxPhysicalNumberOfRows);

        for (Row currentRow : sheet) {
            if (!skip(currentRow, skip) && !isRowEmpty(currentRow)) {
                T t = deserialize0(currentRow, type);
                consumer.accept(t);
            }
        }
    }

    private void loadColumnTitles(Sheet sheet, int maxPhysicalNumberOfRows) {
        if (maxPhysicalNumberOfRows > 0) {
            Row firstRow = sheet.getRow(0);
            for (Cell cell : firstRow) {
                titles.put(cell.getStringCellValue(), cell.getColumnIndex());
                _titles.put(cell.getColumnIndex(), cell.getStringCellValue());
            }
        }
    }

    private <T> T deserialize0(Row currentRow, Class<T> type) {
        T instance;
        try {
            instance = type.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new PoijiInstantiationException("Cannot create a new instance of " + type.getName());
        }

        return setFieldValue(currentRow, type, instance);
    }

    private <T> T tailSetFieldValue(Row currentRow, Class<? super T> type, T instance) {
        for (Field field : type.getDeclaredFields()) {
            ExcelRow excelRow = field.getAnnotation(ExcelRow.class);
            if (excelRow != null) {
                Object o;
                o = casting.castValue(field.getType(), valueOf(currentRow.getRowNum()), options);
                setFieldData(instance, field, o);
            }
            ExcelCell index = field.getAnnotation(ExcelCell.class);
            if (index != null) {
                constructTypeValue(currentRow, instance, field, index.value());
            } else {

                ExcelCellName excelCellName = field.getAnnotation(ExcelCellName.class);
                if (excelCellName != null) {
                    Integer titleColumn = titles.get(excelCellName.value());
                    List<Integer> titleColumns = _titles.entrySet().stream()
                            .filter(x -> x.getValue().equals(excelCellName.value()))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());

                    titleColumns.forEach(x -> constructTypeValue(currentRow, instance, field, x));
                }
            }
        }
        return instance;
    }

    private <T> void constructTypeValue(Row currentRow, T instance, Field field, int column) {
        Class<?> fieldType = field.getType();
        Cell cell = currentRow.getCell(column);

        if (cell != null) {
            String value = dataFormatter.formatCellValue(cell);
            Object o = casting.castValue(fieldType, value, options);
            if (fieldType.getName().equals("java.util.List")) {
                addFieldData(instance, field, o);
            } else {
                setFieldData(instance, field, o);
            }
        }
    }

    private <T> void addFieldData(T instance, Field field, Object o) {
        try {
            field.setAccessible(true);
            List list = (List) field.get(instance);
            list.add(o);
            setFieldData(instance, field, list);
        } catch (IllegalAccessException e) {
            throw new IllegalCastException("Unexpected cast type {" + o + "} of field" + field.getName());
        }

    }

    private <T> void setFieldData(T instance, Field field, Object o) {
        try {
            field.setAccessible(true);
            field.set(instance, o);
        } catch (IllegalAccessException e) {
            throw new IllegalCastException("Unexpected cast type {" + o + "} of field" + field.getName());
        }
    }

    private <T> T setFieldValue(Row currentRow, Class<? super T> subclass, T instance) {
        return subclass == null
                ? instance
                : tailSetFieldValue(currentRow, subclass, setFieldValue(currentRow, subclass.getSuperclass(), instance));
    }

    private boolean skip(final Row currentRow, int skip) {
        return currentRow.getRowNum() + 1 <= skip;
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK)
                return false;
        }
        return true;
    }

    protected abstract Workbook workbook();
}
