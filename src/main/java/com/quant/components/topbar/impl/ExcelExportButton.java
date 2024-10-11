package com.quant.components.topbar.impl;

import com.quant.MainWindow;
import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.filters.CSVFileFilter;
import com.quant.components.topbar.filters.ExcelFileFilter;
import com.quant.workers.CSVExportWorker;
import com.quant.workers.ExcelExportWorker;

import javax.swing.*;

@TopButton(text = "Export to Excel")
public class ExcelExportButton implements TopBarAction {

    @Override
    public void actionPerformed(MainWindow mainWindow) {
        if(mainWindow.getTable().getProducts().size() == 0) {
            JOptionPane.showMessageDialog(mainWindow, "No data to export", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to Excel");

        fileChooser.setFileFilter(new ExcelFileFilter());
        var option = fileChooser.showSaveDialog(mainWindow);

        if(option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        var worker = new ExcelExportWorker(mainWindow, fileChooser.getSelectedFile());
        worker.execute();
    }

}
