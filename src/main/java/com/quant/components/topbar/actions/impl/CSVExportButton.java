package com.quant.components.topbar.actions.impl;

import com.quant.frames.impl.MainFrame;
import com.quant.components.topbar.actions.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.filters.CSVFileFilter;
import com.quant.managers.Managers;
import com.quant.managers.impl.FramesManager;
import com.quant.managers.impl.ProductsManager;
import com.quant.workers.CSVExportWorker;

import javax.swing.*;

@TopButton(text = "Export to CSV", order = 1)
public class CSVExportButton implements TopBarAction {

    @Override
    public void actionPerformed() {
        var products = Managers.getManager(ProductsManager.class).getProducts();
        var mainFrame = Managers.getManager(FramesManager.class).getFrame(MainFrame.class);

        if(products.size() == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No data to export", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");

        fileChooser.setFileFilter(new CSVFileFilter());
        var option = fileChooser.showSaveDialog(mainFrame);

        if(option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        var worker = new CSVExportWorker(mainFrame, fileChooser.getSelectedFile());
        worker.execute();
    }

}
