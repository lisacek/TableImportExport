package com.quant.workers;

import com.quant.MainWindow;
import com.quant.cons.CSVFile;
import com.quant.cons.ProductsImport;
import com.quant.exceptions.FailedToSaveCSV;
import com.quant.exceptions.InvalidFileException;
import com.quant.exceptions.UnsupportedFileTypeException;
import com.quant.utils.CsvUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ExportWorker extends SwingWorker<Integer, Integer> {

    private final MainWindow mainWindow;
    private final File file;
    private final JDialog dialog;
    private final JProgressBar progressBar = new JProgressBar();

    public ExportWorker(MainWindow mainWindow, File file) {
        this.mainWindow = mainWindow;
        this.file = file;

        progressBar.setString("Waiting...");
        progressBar.setStringPainted(true);

        mainWindow.setEnabled(false);

        JButton cancelButton = new JButton("Cancel");
        dialog = new JDialog(mainWindow, "Export Progress", true);
        dialog.setLayout(new BorderLayout());

        var progressPanel = new JPanel();
        progressPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        progressBar.setPreferredSize(new Dimension(250, 30));
        progressPanel.add(progressBar);

        var cancelButtonPanel = new JPanel();
        cancelButton.setPreferredSize(new Dimension(100, 30));
        cancelButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        cancelButtonPanel.add(cancelButton);

        dialog.add(progressPanel, BorderLayout.NORTH);
        dialog.add(cancelButtonPanel, BorderLayout.SOUTH);
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(false);
        dialog.setVisible(true);
        dialog.setResizable(false);

        cancelButton.addActionListener(e -> {
            cancel(true);
            dialog.dispose();
        });
    }

    @Override
    protected Integer doInBackground() {
        var table = mainWindow.getTable();
        try {
            progressBar.setValue(0);
            progressBar.setMaximum(1);
            progressBar.setStringPainted(true);

            progressBar.setString("Getting products...");
            var data = table.getData();
            progressBar.setValue(1);

            progressBar.setValue(0);
            progressBar.setString("Exporting...");
            var csvFile = new CSVFile(file);
            csvFile.setData(data);
            csvFile.save();

            progressBar.setMaximum(1);
            progressBar.setString("Done!");
            dialog.dispose();
        } catch (FailedToSaveCSV e) {
            throw new RuntimeException(e);
        }

        return 0;
    }


    @Override
    protected void done() {
        JLabel label = new JLabel("Task Complete");
        dialog.remove(progressBar);
        dialog.add(label);
        dialog.validate();

        mainWindow.setEnabled(true);
    }

}
