package com.quant.components.table;

import com.quant.MainWindow;
import com.quant.components.Component;
import com.quant.cons.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Arrays;
import java.util.List;

public class Table implements Component {

    private JTable table;
    private TableModel tableModel;
    private List<Product> products;

    @Override
    public void init() {
        Object[][] defaultTableData = {
                { "", "", "", "", "" }
        };
        tableModel = new DefaultTableModel(defaultTableData, new Object[]{"Product ID", "Name", "Brand", "Amount", "Price"});

        table = new JTable(tableModel);
        table.setToolTipText("Double click to edit");
        table.setFillsViewportHeight(true);
    }

    @Override
    public JComponent render(MainWindow mainWindow) {
        return table;
    }

    public void renderUpdatedTable() {
        Object[][] tableData = new Object[products.size()][5];
        for (var product: products) {
            tableData[products.indexOf(product)] = new Object[] {
                    product.getId(),
                    product.getName(),
                    product.getBrand(),
                    product.getAmount(),
                    product.getFormattedPrice()
            };
        }
        tableModel = new DefaultTableModel(tableData, new Object[]{"Product ID", "Name", "Brand", "Amount", "Price"});
        table.setModel(tableModel);
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
