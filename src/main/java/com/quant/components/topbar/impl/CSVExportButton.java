package com.quant.components.topbar.impl;

import com.quant.MainWindow;
import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.filters.CSVFileFilter;
import com.quant.workers.CSVExportWorker;

import javax.swing.*;

@TopButton(text = "Export to CSV")
public class CSVExportButton implements TopBarAction {

    @Override
    public void actionPerformed(MainWindow mainWindow) {
        if(mainWindow.getTable().getProducts().size() == 0) {
            JOptionPane.showMessageDialog(mainWindow, "No data to export", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");

        fileChooser.setFileFilter(new CSVFileFilter());
        var option = fileChooser.showSaveDialog(mainWindow);

        if(option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        var worker = new CSVExportWorker(mainWindow, fileChooser.getSelectedFile());
        worker.execute();
    }

}
