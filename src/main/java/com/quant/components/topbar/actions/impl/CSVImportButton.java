package com.quant.components.topbar.actions.impl;

import com.quant.components.topbar.actions.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.filters.CSVFileFilter;
import com.quant.frames.impl.MainFrame;
import com.quant.managers.Managers;
import com.quant.managers.impl.FramesManager;
import com.quant.workers.CSVImportWorker;

import javax.swing.*;
import java.util.*;

@TopButton(text = "Import from CSV", order = 0)
public class CSVImportButton implements TopBarAction {

    @Override
    public void actionPerformed() {
        var fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new CSVFileFilter());
        fileChooser.setDialogTitle("Import from CSV");

        var framesManager = Managers.getManager(FramesManager.class);
        var mainFrame = framesManager.getFrame(MainFrame.class);

        var option = fileChooser.showOpenDialog(mainFrame);
        if(option != JFileChooser.APPROVE_OPTION){
            return;
        }

        var files = fileChooser.getSelectedFiles();
        if(files.length < 1) return;

        var worker = new CSVImportWorker(mainFrame, List.of(files));
        worker.execute();
    }

}
