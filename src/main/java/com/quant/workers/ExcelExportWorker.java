package com.quant.workers;

import com.quant.MainWindow;
import com.quant.cons.CSVFile;
import com.quant.exceptions.CSVExportFailedException;
import com.quant.exceptions.ExcelExportFailedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExportWorker extends SwingWorker<Integer, Integer> {

    private final MainWindow mainWindow;
    private final File file;
    private final JDialog dialog;
    private final JProgressBar progressBar = new JProgressBar();

    public ExcelExportWorker(MainWindow mainWindow, File file) {
        this.mainWindow = mainWindow;
        this.file = file;

        progressBar.setString("Waiting...");
        progressBar.setStringPainted(true);

        mainWindow.setEnabled(false);

        JButton cancelButton = new JButton("Cancel");
        dialog = new JDialog(mainWindow, "Export Progress", false);
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
        dialog.setVisible(true);
        dialog.setResizable(false);

        cancelButton.addActionListener(e -> {
            cancel(true);
            dialog.dispose();
        });

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                cancel(true);
                dialog.dispose();
            }
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
            var data = table.getExcelData(file);
            progressBar.setValue(1);

            progressBar.setValue(0);
            progressBar.setString("Exporting...");

            if (!file.exists()) {
                var success = file.createNewFile();
                if (!success) throw new ExcelExportFailedException("Failed to create file");
            }

            try (var out = new FileOutputStream(file)) {
                data.write(out);
            }

            progressBar.setMaximum(1);
            progressBar.setString("Done!");
            dialog.dispose();
        } catch (ExcelExportFailedException | IOException e) {
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            done();
            return 1;
        }

        return 0;
    }


    @Override
    protected void done() {
        System.out.println("Done");
        mainWindow.setEnabled(true);
    }

}