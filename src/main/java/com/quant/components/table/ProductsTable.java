package com.quant.components.table;

import com.quant.annotations.ComponentInfo;
import com.quant.columns.Column;
import com.quant.components.Component;
import com.quant.enums.ComponentRender;
import com.quant.managers.Managers;
import com.quant.managers.impl.ColumnsManager;
import com.quant.managers.impl.ProductsManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

@ComponentInfo(
        render = ComponentRender.SCROLL
)
public class ProductsTable implements Component {

    private QTable table;
    private DefaultTableModel tableModel;

    @Override
    public void init() {
        var columnsNames = Managers.getManager(ColumnsManager.class).getColumnsNames();
        tableModel = new DefaultTableModel(new Vector<>(), columnsNames);

        var products = Managers.getManager(ProductsManager.class).getProducts();
        table = new QTable(tableModel);
        table.setToolTipText("Double click to edit");
        table.setFillsViewportHeight(true);

        table.addPropertyChangeListener(evt -> {
            if (!evt.getPropertyName().equals("tableCellEditor")) {
                return;
            }

            if (!table.isEditing()) {
                return;
            }

            var columnIndex = table.getSelectedColumn();
            var column = table.getColumns().get(columnIndex);

            if(column == null) {
                return;
            }

            if(column.getDisplayFormat() == null) return;
            var component = (JTextField) table.getEditorComponent();

            component.setText(column.resolveValue(products.get(table.getSelectedRow())).toString());
        });
    }

    @Override
    public JComponent getJComponent() {
        return table;
    }

    public void renderUpdatedTable() {
        var products = Managers.getManager(ProductsManager.class).getProducts();
        Object[][] tableData = new Object[products.size()][5];
        var columns = table.getColumns();

        for (var product: products) {
            var productData = new Object[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                var column = columns.get(i);
                var value = column.resolveValue(product);

                if(column.getDisplayFormat() != null) {
                    productData[i] = String.format(column.getDisplayFormat(), value);
                    continue;
                }

                productData[i] = value == null ? "null" : value;
            }

            tableData[products.indexOf(product)] = productData;
        }

        var columnsNames = table.getColumnNames();
        tableModel.setDataVector(tableData, columnsNames.toArray());
    }

}
