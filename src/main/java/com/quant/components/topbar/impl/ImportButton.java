package com.quant.components.topbar.impl;

import com.quant.MainWindow;
import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.exceptions.InvalidFile;
import com.quant.exceptions.UnsupportedFileType;
import com.quant.utils.CsvUtils;
import org.apache.poi.ss.formula.functions.CalendarFieldFunction;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.util.*;
import java.util.Timer;

@TopButton(text = "Import from CSV")
public class ImportButton implements TopBarAction {

    @Override
    public void actionPerformed(MainWindow mainWindow) {
        var fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(java.io.File file) {
                return file.getName().toLowerCase().endsWith(".csv") || file.isDirectory();
            }

            @Override
            public String getDescription() {
                return "CSV files";
            }
        });
        fileChooser.setDialogTitle("Import from CSV");

        var option = fileChooser.showOpenDialog(mainWindow);
        if(option == JFileChooser.APPROVE_OPTION){
            var files = fileChooser.getSelectedFiles();
            if(files.length < 1) return;

            try {
                var bottomBar = mainWindow.getBottomBar();

                var progressBar = bottomBar.getProgressBar();
                progressBar.setValue(0);
                progressBar.setMaximum(files.length);
                progressBar.setString("Loading files...");
                progressBar.setVisible(true);

                var products = CsvUtils.loadProducts(Arrays.stream(files).toList(), progressBar);
                progressBar.setValue(0);
                progressBar.setMaximum(1);
                progressBar.setString("Merging files...");

                var table = mainWindow.getTable();
                var oldProducts = table.getProducts();
                products.removeIf(product -> oldProducts.stream().anyMatch(oldProduct -> oldProduct.getId() == product.getId()));
                products.addAll(oldProducts);

                progressBar.setVisible(false);
                table.setProducts(products);
                bottomBar.setTotalProducts(products.size());
            } catch (UnsupportedFileType e) {
                JOptionPane.showMessageDialog(mainWindow, "Unsupported file type: "+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidFile e) {
                JOptionPane.showMessageDialog(mainWindow, "Invalid file: "+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else{
            System.out.println("No Selection ");
        }
    }

}
