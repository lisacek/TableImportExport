package com.quant.utils;

import com.quant.cons.CSVFile;
import com.quant.cons.Product;
import com.quant.enums.Head;
import com.quant.exceptions.InvalidFile;
import com.quant.exceptions.UnsupportedFileType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CsvUtils {

    public static List<Product> loadProducts(File file) throws UnsupportedFileType, InvalidFile {
        var list = new ArrayList<Product>();
        var csvFile = new CSVFile(file);

        if(!csvFile.checkIfFileValid()) {
            throw new UnsupportedFileType("Unsupported file type");
        }

        var data = csvFile.getData();
        if(data.size() < 2) {
            return list;
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
            list.add(product);
        }

        return list;
    }

    public static List<Product> loadProducts(List<File> files, JProgressBar progressBar) throws UnsupportedFileType, InvalidFile {
        var list = new ArrayList<Product>();
        for (var file : files) {
            var products = loadProducts(file);
            if(progressBar != null) {
                progressBar.setValue(progressBar.getValue() + 1);
            }

            for(var product : products) {
                list.removeIf(p -> p.getId() == product.getId());
                list.add(product);
            }
        }

        return list;
    }

}
