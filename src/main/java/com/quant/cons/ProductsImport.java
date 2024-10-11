package com.quant.cons;

import com.quant.frames.impl.MainFrame;
import com.quant.managers.Managers;
import com.quant.managers.impl.ProductsManager;
import com.quant.utils.MathUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductsImport {

    private final List<Product> products = new ArrayList<>();
    private final List<File> files;
    private final long linesAmount;

    private JProgressBar progressBar;

    public ProductsImport(List<File> files) {
        this.files = files;

        this.linesAmount = files.stream().mapToLong(file -> {
            var csvFile = new CSVFile(file);
            return csvFile.calculateLinesAmount();
        }).sum();
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);

        if (progressBar != null) {
            progressBar.setValue(MathUtils.calculatePercentage(products.size(), linesAmount));
            progressBar.setString("Loading lines... (" + products.size() + "/" + linesAmount + ")");
        }
    }

    public void finishImport() {
        if (progressBar != null) {
            progressBar.setValue(0);
            progressBar.setMaximum(1);
            progressBar.setString("Merging data...");
        }

        var productsManager = Managers.getManager(ProductsManager.class);
        var oldProducts = productsManager.getProducts();
        products.removeIf(product -> oldProducts.stream().anyMatch(oldProduct -> oldProduct.getId() == product.getId()));
        products.addAll(oldProducts);

        productsManager.setProducts(products);
    }

    public long getLinesAmount() {
        return linesAmount;
    }
}
