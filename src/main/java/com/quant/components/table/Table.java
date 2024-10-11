package com.quant.components.table;

import com.quant.MainWindow;
import com.quant.components.Component;
import com.quant.cons.Product;
import com.quant.enums.Column;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Table implements Component {

    private QTable table;
    private DefaultTableModel tableModel;
    private List<Product> products;

    @Override
    public void init() {
        Object[][] defaultTableData = {};
        tableModel = new DefaultTableModel(defaultTableData, new Object[]{"Product ID", "Name", "Brand", "Amount", "Price"});

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

            if(column == Column.PRICE) {
                JTextField component = (JTextField) table.getEditorComponent();
                component.setText(component.getText().replace(" Kč", ""));
            }
        });
    }

    @Override
    public JComponent render(MainWindow mainWindow) {
        return table;
    }

    public void renderUpdatedTable() {
        Object[][] tableData = new Object[products.size()][5];
        var columns = table.getColumns();

        for (var product: products) {
            var productData = new Object[columns.size()];
            for (int i = 0; i < columns.size(); i++) {
                var column = columns.get(i);
                switch (column) {
                    case PRODUCT_ID -> productData[i] = product.getId();
                    case NAME -> productData[i] = product.getName();
                    case BRAND -> productData[i] = product.getBrand();
                    case AMOUNT -> productData[i] = product.getAmount();
                    case PRICE -> productData[i] = product.getFormattedPrice();
                }
            }

            tableData[products.indexOf(product)] = productData;
        }

        var columnsNames = table.getColumnNames();
        tableModel.setDataVector(tableData, columnsNames.toArray());
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        renderUpdatedTable();
    }

    public List<Product> getProducts() {
        if (products == null) {
            return List.of();
        }

        return products;
    }

}
