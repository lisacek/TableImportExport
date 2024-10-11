package com.quant.components.topbar.actions.impl;

import com.quant.frames.impl.MainFrame;
import com.quant.components.topbar.actions.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.filters.ExcelFileFilter;
import com.quant.managers.Managers;
import com.quant.managers.impl.FramesManager;
import com.quant.managers.impl.ProductsManager;
import com.quant.workers.ExcelExportWorker;

import javax.swing.*;

@TopButton(text = "Export to Excel", order = 2)
public class ExcelExportButton implements TopBarAction {

    @Override
    public void actionPerformed() {
        var products = Managers.getManager(ProductsManager.class).getProducts();
        var mainFrame = Managers.getManager(FramesManager.class).getFrame(MainFrame.class);

        if(products.size() == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No data to export", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to Excel");

        fileChooser.setFileFilter(new ExcelFileFilter());
        var option = fileChooser.showSaveDialog(mainFrame);

        if(option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        var worker = new ExcelExportWorker(mainFrame, fileChooser.getSelectedFile());
        worker.execute();
    }

}
