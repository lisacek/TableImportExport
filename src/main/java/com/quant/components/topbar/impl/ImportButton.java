package com.quant.components.topbar.impl;

import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

@TopButton(text = "Import from CSV")
public class ImportButton implements TopBarAction {

    @Override
    public void actionPerformed(JFrame frame) {
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

        var option = fileChooser.showOpenDialog(frame);
        if(option == JFileChooser.APPROVE_OPTION){
            var files = fileChooser.getSelectedFiles();
            for (var file : files) {
                System.out.println(file.getName());
            }
        }else{
            System.out.println("No Selection ");
        }
    }

}
