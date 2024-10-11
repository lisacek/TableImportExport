package com.quant.components.topbar.impl;

import com.quant.MainWindow;
import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.filters.CSVFileFilter;
import com.quant.workers.ExportWorker;
import com.quant.workers.ImportWorker;

import javax.swing.*;
import java.util.List;

@TopButton(text = "Export to CSV")
public class ExportButton implements TopBarAction {

    @Override
    public void actionPerformed(MainWindow mainWindow) {
        var fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");

        fileChooser.addChoosableFileFilter(new CSVFileFilter());
        var option = fileChooser.showSaveDialog(mainWindow);

        if(option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        var worker = new ExportWorker(mainWindow, fileChooser.getSelectedFile());
        worker.execute();
    }

}
