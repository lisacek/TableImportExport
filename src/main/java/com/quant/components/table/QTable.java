package com.quant.components.table;

import com.quant.annotations.ColumnName;
import com.quant.columns.Column;
import com.quant.cons.Product;
import com.quant.exceptions.ColumnValidationFailedException;
import com.quant.managers.Managers;
import com.quant.managers.impl.ColumnsManager;
import com.quant.managers.impl.ProductsManager;
import com.quant.utils.ReflectionUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QTable extends JTable {

    public QTable(DefaultTableModel model) {
        super(model);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var previousValue = getValueAt(rowIndex, columnIndex);

        var columnName = this.dataModel.getColumnName(columnIndex);
        var column = Managers.getManager(ColumnsManager.class).getByColumnName(columnName);

        if(column == null) {
            super.setValueAt(value, rowIndex, columnIndex);
            return;
        }

        try {
            column.getValidator().validate(value.toString());
            var products = Managers.getManager(ProductsManager.class).getProducts();
            var product = products.get(rowIndex);

            Object normalizedValue = ReflectionUtils.normalizeValue(value, column.getValueType());
            for (var productColumn : Product.class.getDeclaredFields()) {
                if (productColumn.isAnnotationPresent(ColumnName.class)) {
                    var annotation = productColumn.getAnnotation(ColumnName.class);
                    if (annotation.value().equals(columnName)) {
                        try {
                            productColumn.setAccessible(true);
                            productColumn.set(product, normalizedValue);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            super.setValueAt(column.formatDisplayValue(normalizedValue), rowIndex, columnIndex);
        } catch (ColumnValidationFailedException e) {
            super.setValueAt(previousValue, rowIndex, columnIndex);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public List<Column> getColumns() {
        var columnsManager = Managers.getManager(ColumnsManager.class);
        return getColumnNames().stream().map(columnsManager::getByColumnName).collect(Collectors.toList());
    }

    public List<String> getColumnNames() {
        var columnsIterator = this.getColumnModel().getColumns().asIterator();
        var columns = new ArrayList<String>();

        while (columnsIterator.hasNext()) {
            var column = columnsIterator.next();
            columns.add(column.getHeaderValue().toString());
        }

        return columns;
    }

    public ArrayList<Object> getRowData(int row) throws IllegalAccessException {
        var columns = getColumnNames();
        var products = Managers.getManager(ProductsManager.class).getProducts();
        var product = products.get(row);
        var values = new ArrayList<>();
        for (var column : columns) {
            var fields = product.getClass().getDeclaredFields();
            for (var field : fields) {
                if (field.getName().equalsIgnoreCase(column) || field.isAnnotationPresent(ColumnName.class) && field.getAnnotation(ColumnName.class).value().equalsIgnoreCase(column)) {
                    field.setAccessible(true);
                    values.add(field.get(product).toString());
                }
            }
        }

        return values;
    }

    public ArrayList<String> getRowDataString(int row) throws IllegalAccessException {
        return getRowData(row).stream().map(Object::toString).collect(Collectors.toCollection(ArrayList::new));
    }
}
