package com.quant.utils;

import com.quant.cons.CSVFile;
import com.quant.cons.ProductsImport;
import com.quant.cons.Product;
import com.quant.enums.Head;
import com.quant.exceptions.InvalidFile;
import com.quant.exceptions.UnsupportedFileType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CsvUtils {

    public static void loadProducts(File file, ProductsImport productsImport) throws UnsupportedFileType, InvalidFile {
        var csvFile = new CSVFile(file);

        if(!csvFile.checkIfFileValid()) {
            throw new UnsupportedFileType("Unsupported file type");
        }

        var data = csvFile.getData();
        if(data.size() < 2) {
            return;
        }

        var columns = new HashMap<Head, Integer>();
        var firstRow = data.get(0);

        for (var i = 0; i < firstRow.size(); i++) {
            var cellValue = firstRow.get(i);
            var head = Head.getHead(cellValue);

            if(head != null) {
                columns.put(head, i);
            }
        }

        for (var i = 1; i < data.size(); i++) {
            var row = data.get(i);

            var idColumn = row.get(columns.get(Head.PRODUCT_ID));
            if(idColumn == null || idColumn.isEmpty() || !ValidationUtils.isLong(idColumn)) {
                throw new InvalidFile("Product ID is missing or invalid in row " + i);
            }

            var id = Long.parseLong(idColumn);
            var brand = row.get(columns.get(Head.BRAND));
            var name = row.get(columns.get(Head.NAME));

            var amountColumn = row.get(columns.get(Head.AMOUNT));
            if(amountColumn == null || amountColumn.isEmpty() || !ValidationUtils.isInteger(amountColumn)) {
                throw new InvalidFile("Amount is missing or invalid in row " + i);
            }

            var amount = Integer.parseInt(amountColumn);

            var priceColumn = row.get(columns.get(Head.PRICE));
            if(priceColumn == null || priceColumn.isEmpty() || !ValidationUtils.isDouble(priceColumn)) {
                throw new InvalidFile("Price is missing or invalid in row " + i);
            }

            var price = Double.parseDouble(priceColumn);
            var product = new Product(id, brand, name, amount, price);
            productsImport.addProduct(product);

        }
    }

    public static void loadProducts(ProductsImport productsImport) throws UnsupportedFileType, InvalidFile {
        for (var file : productsImport.getFiles()) {
            loadProducts(file, productsImport);
        }
    }

}
