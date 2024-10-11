package com.quant.utils;

import com.quant.columns.Column;
import com.quant.cons.CSVFile;
import com.quant.cons.Product;
import com.quant.cons.ProductsImport;
import com.quant.exceptions.ColumnValidationFailedException;
import com.quant.exceptions.InvalidFileException;
import com.quant.exceptions.UnsupportedFileTypeException;
import com.quant.managers.Managers;
import com.quant.managers.impl.ColumnsManager;

import java.io.File;
import java.util.HashMap;

public class CsvUtils {

    public static void loadProducts(File file, ProductsImport productsImport) throws UnsupportedFileTypeException, InvalidFileException {
        var csvFile = new CSVFile(file);

        var data = csvFile.getData();
        if (data.size() < 2) {
            return;
        }

        var columns = new HashMap<Integer, Class<? extends Column>>();
        var firstRow = data.get(0);

        for (var i = 0; i < firstRow.size(); i++) {
            var cellValue = firstRow.get(i);

            var head = Managers.getManager(ColumnsManager.class).getByColumnName(cellValue);
            if (head == null) {
                throw new InvalidFileException("Invalid file format");
            }

            columns.put(i, head.getClass());
        }

        if (columns.size() != 5) {
            throw new InvalidFileException("Invalid file format");
        }

        for (var i = 1; i < data.size(); i++) {
            var row = data.get(i);
            var rowData = new HashMap<Column, Object>();

            for (var rowIndex = 0; rowIndex < row.size(); rowIndex++) {
                var column = Managers.getManager(ColumnsManager.class).getColumn(columns.get(rowIndex));
                if (column == null) {
                    continue;
                }

                try {
                    column.getValidator().validate(row.get(rowIndex));
                } catch (ColumnValidationFailedException e) {
                    throw new InvalidFileException("column validation failed in row " + i + " column " + column.getName() +" : "+ e.getMessage());
                }

                rowData.put(column, row.get(rowIndex));
            }

            try {
                var product = new Product();
                for (var entry : rowData.entrySet()) {
                    entry.getKey().assignValue(product, entry.getValue());
                }

                productsImport.addProduct(product);
            } catch (Exception e) {
                throw new InvalidFileException("Invalid file format");
            }
        }
    }

    public static void loadProducts(ProductsImport productsImport) throws UnsupportedFileTypeException, InvalidFileException {
        for (var file : productsImport.getFiles()) {
            loadProducts(file, productsImport);
        }
    }

}
