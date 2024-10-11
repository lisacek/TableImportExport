package com.quant.components.topbar.impl;

import com.quant.MainWindow;
import com.quant.components.topbar.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;

import javax.swing.*;

@TopButton(text = "Export to CSV")
public class ExportButton implements TopBarAction {

    @Override
    public void actionPerformed(MainWindow mainWindow) {
        var fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to CSV");

        var option = fileChooser.showSaveDialog(mainWindow);

        if(option == JFileChooser.APPROVE_OPTION){
            var file = fileChooser.getSelectedFile();
            System.out.println(file.getName());
        }else{
            System.out.println("No Selection ");
        }
    }

}
