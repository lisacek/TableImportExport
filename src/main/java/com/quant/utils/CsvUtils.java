package com.quant.utils;

import com.quant.cons.CSVFile;
import com.quant.cons.ProductsImport;
import com.quant.cons.Product;
import com.quant.enums.Column;
import com.quant.exceptions.InvalidFileException;
import com.quant.exceptions.UnsupportedFileTypeException;

import java.io.File;
import java.util.HashMap;

public class CsvUtils {

    public static void loadProducts(File file, ProductsImport productsImport) throws UnsupportedFileTypeException, InvalidFileException {
        var csvFile = new CSVFile(file);

        var data = csvFile.getData();
        if(data.size() < 2) {
            return;
        }

        var columns = new HashMap<Column, Integer>();
        var firstRow = data.get(0);

        for (var i = 0; i < firstRow.size(); i++) {
            var cellValue = firstRow.get(i);

            Column head;
            try {
                head = Column.getColumn(cellValue);
            } catch (IllegalArgumentException e) {
                throw new InvalidFileException("Invalid file format");
            }

            if(head != null) {
                columns.put(head, i);
            }
        }

        if(columns.size() != 5) {
            throw new InvalidFileException("Invalid file format");
        }

        for (var i = 1; i < data.size(); i++) {
            var row = data.get(i);

            var idColumn = row.get(columns.get(Column.PRODUCT_ID));
            if(idColumn == null || idColumn.isEmpty() || !ValidationUtils.isLong(idColumn)) {
                throw new InvalidFileException("Product ID is missing or invalid in row " + i);
            }

            long id;
            try {
                id = Long.parseLong(idColumn);
            } catch (NumberFormatException e) {
                throw new InvalidFileException("Product ID is missing or invalid in row " + i);
            }

            var brand = row.get(columns.get(Column.BRAND));
            if(brand == null || brand.isEmpty()) {
                throw new InvalidFileException("Brand is missing in row " + i);
            }

            var name = row.get(columns.get(Column.NAME));
            if(name == null || name.isEmpty()) {
                throw new InvalidFileException("Name is missing in row " + i);
            }

            var amountColumn = row.get(columns.get(Column.AMOUNT));
            if(amountColumn == null || amountColumn.isEmpty() || !ValidationUtils.isInteger(amountColumn)) {
                throw new InvalidFileException("Amount is missing or invalid in row " + i);
            }

            long amount;
            try {
                amount = Long.parseLong(amountColumn);
            } catch (NumberFormatException e) {
                throw new InvalidFileException("Amount is missing or invalid in row " + i);
            }

            var priceColumn = row.get(columns.get(Column.PRICE));
            if(priceColumn == null || priceColumn.isEmpty() || !ValidationUtils.isDouble(priceColumn)) {
                throw new InvalidFileException("Price is missing or invalid in row " + i);
            }

            double price;
            try {
                price = Double.parseDouble(priceColumn);
            } catch (NumberFormatException e) {
                throw new InvalidFileException("Price is missing or invalid in row " + i);
            }

            var product = new Product(id, brand, name, amount, price);
            productsImport.addProduct(product);
        }
    }

    public static void loadProducts(ProductsImport productsImport) throws UnsupportedFileTypeException, InvalidFileException {
        for (var file : productsImport.getFiles()) {
            loadProducts(file, productsImport);
        }
    }

}
