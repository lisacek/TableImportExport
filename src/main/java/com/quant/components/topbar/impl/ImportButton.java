package com.quant.components.topbar.impl;

import com.quant.MainWindow;
import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.filters.CSVFileFilter;
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
        fileChooser.setFileFilter(new CSVFileFilter());
        fileChooser.setDialogTitle("Import from CSV");

        var option = fileChooser.showOpenDialog(mainWindow);
        if(option != JFileChooser.APPROVE_OPTION){
            return;
        }

        var files = fileChooser.getSelectedFiles();
        if(files.length < 1) return;

        var worker = new ImportWorker(mainWindow, List.of(files));
        worker.execute();
    }

}
