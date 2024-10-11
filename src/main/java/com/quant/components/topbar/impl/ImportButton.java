package com.quant.components.topbar.impl;

import com.quant.MainWindow;
import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.workers.ImportWorker;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.util.*;

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

            var worker = new ImportWorker(mainWindow, List.of(files));
            worker.execute();
        } else{
            System.out.println("No Selection ");
        }
    }

}
