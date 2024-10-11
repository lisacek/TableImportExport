package com.quant.components.table;

import com.quant.MainWindow;
import com.quant.components.Component;
import com.quant.cons.Product;
import com.quant.enums.Column;
import com.quant.exceptions.ExcelExportFailedException;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table implements Component {

    private QTable table;
    private DefaultTableModel tableModel;
    private List<Product> products = new ArrayList<>();

    @Override
    public void init() {
        Object[][] defaultTableData = {};
        tableModel = new DefaultTableModel(defaultTableData, new Object[]{"Product ID", "Name", "Brand", "Amount", "Price"});

        table = new QTable(tableModel, products);
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
        this.products.clear();
        this.products.addAll(products);
        renderUpdatedTable();
    }

    public List<Product> getProducts() {
        if (products == null) {
            return List.of();
        }

        return products;
    }

    public List<List<String>> getCSVData() {
        var columnNames = table.getColumnNames();

        var data = new ArrayList<List<String>>();
        data.add(columnNames);

        for (var product: products) {
            var productData = new ArrayList<String>();
            for (var column: columnNames) {
                switch (column) {
                    case "Product ID" -> productData.add(Long.toString(product.getId()));
                    case "Name" -> productData.add(product.getName());
                    case "Brand" -> productData.add(product.getBrand());
                    case "Amount" -> productData.add(String.valueOf(product.getAmount()));
                    case "Price" -> productData.add(String.valueOf(product.getPrice()));
                }
            }

            data.add(productData);
        }

        return data;
    }

    public Workbook getExcelData(File file) throws ExcelExportFailedException {
        System.out.println("Creating workbook");
        HSSFWorkbook workbook;
        workbook = HSSFWorkbook.create(InternalWorkbook.createWorkbook());

        System.out.println("Workbook created");
        var sheet = workbook.createSheet("Products");

        System.out.println("Sheet created");
        var columnNames = table.getColumnNames();
        var row = sheet.createRow(0);

        System.out.println("Row created");
        for (int i = 0; i < columnNames.size(); i++) {
            var cell = row.createCell(i);
            cell.setCellValue(columnNames.get(i));
        }

        System.out.println("Cells created");

        for (var product: products) {
            var productRow = sheet.createRow(sheet.getLastRowNum() + 1);
            var productData = new ArrayList<>();
            for (var column: columnNames) {
                switch (column) {
                    case "Product ID" -> productData.add(product.getId());
                    case "Name" -> productData.add(product.getName());
                    case "Brand" -> productData.add(product.getBrand());
                    case "Amount" -> productData.add(product.getAmount());
                    case "Price" -> productData.add(product.getPrice());
                }
            }

            for (int i = 0; i < productData.size(); i++) {
                var cell = productRow.createCell(i);
                var data = productData.get(i);

                if (data instanceof Long) {
                    cell.setCellValue((Long) data);
                } else if (data instanceof Integer) {
                    cell.setCellValue((Integer) data);
                } else if (data instanceof Double) {
                    cell.setCellValue((Double) data);
                } else {
                    cell.setCellValue((String) data);
                }
            }
        }

        System.out.println("Data added");
        return workbook;
    }

}
