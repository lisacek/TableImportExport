package com.quant.managers.impl;

import com.quant.components.bottombar.BottomBar;
import com.quant.components.table.ProductsTable;
import com.quant.components.table.QTable;
import com.quant.cons.Product;
import com.quant.managers.Manager;
import com.quant.managers.Managers;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class ProductsManager implements Manager {

    private final List<Product> products = new ArrayList<>();

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getById(int id) {
        return products.stream().filter(product -> product.getId() == id).findFirst().orElse(null);
    }

    public void addProduct(Product product) {
        products.add(product);

        var components = Managers.getManager(ComponentsManager.class);
        components.getComponent(ProductsTable.class).renderUpdatedTable();
        components.getComponent(BottomBar.class).setTotalProducts(products.size());
    }

    public void setProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);

        var components = Managers.getManager(ComponentsManager.class);
        components.getComponent(ProductsTable.class).renderUpdatedTable();
        components.getComponent(BottomBar.class).setTotalProducts(products.size());
    }


    public Workbook getWorkbook() {
        var table = (QTable) Managers.getManager(ComponentsManager.class).getComponent(ProductsTable.class).getJComponent();

        var workbook = HSSFWorkbook.create(InternalWorkbook.createWorkbook());
        var sheet = workbook.createSheet("Products");

        var columnNames = table.getColumnNames();
        var row = sheet.createRow(0);

        for (int i = 0; i < columnNames.size(); i++) {
            var cell = row.createCell(i);
            cell.setCellValue(columnNames.get(i));
        }

        for (int i = 0; i < table.getRowCount(); i++) {
            var productRow = sheet.createRow(sheet.getLastRowNum() + 1);
            ArrayList<Object> productData;
            try {
                productData = table.getRowData(i);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            for (int rowIndex = 0; rowIndex < productData.size(); rowIndex++) {
                var cell = productRow.createCell(rowIndex);
                var data = productData.get(rowIndex);

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

        return workbook;
    }

    public List<List<String>> getCSVData() {
        var table = (QTable) Managers.getManager(ComponentsManager.class).getComponent(ProductsTable.class).getJComponent();
        var columnNames = table.getColumnNames();

        var data = new ArrayList<List<String>>();
        data.add(columnNames);

        for (int i = 0; i < table.getRowCount(); i++) {
            ArrayList<String> productData;
            try {
                productData = table.getRowDataString(i);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            data.add(productData);
        }

        return data;
    }
}
