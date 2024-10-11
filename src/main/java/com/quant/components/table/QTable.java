package com.quant.components.table;

import com.quant.enums.Column;
import com.quant.exceptions.ColumnValidationFailedException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class QTable extends JTable {

    public QTable(DefaultTableModel model) {
        super(model);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var previousValue = getValueAt(rowIndex, columnIndex);

        var columnName = this.dataModel.getColumnName(columnIndex);
        var column = Column.getColumn(columnName);

        if(column == null) {
            super.setValueAt(value, rowIndex, columnIndex);
            return;
        }

        try {
            column.validate(value.toString());
            var newValue = value.toString();
            if(column == Column.PRICE) {
                var parsedDouble = Double.parseDouble(newValue);
                newValue = String.format("%.2f Kč", parsedDouble);
            }

            super.setValueAt(newValue, rowIndex, columnIndex);
        } catch (ColumnValidationFailedException e) {
            super.setValueAt(previousValue, rowIndex, columnIndex);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public List<Column> getColumns() {
        return getColumnNames().stream().map(Column::getColumn).toList();
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

}