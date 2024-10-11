package com.quant.workers;

import com.quant.frames.impl.MainFrame;
import com.quant.cons.CSVFile;
import com.quant.exceptions.CSVExportFailedException;
import com.quant.managers.Managers;
import com.quant.managers.impl.ProductsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class CSVExportWorker extends SwingWorker<Integer, Integer> {

    private final MainFrame mainFrame;
    private final File file;
    private final JDialog dialog;
    private final JProgressBar progressBar = new JProgressBar();

    public CSVExportWorker(MainFrame mainFrame, File file) {
        this.mainFrame = mainFrame;
        this.file = file;

        progressBar.setString("Waiting...");
        progressBar.setStringPainted(true);

        JButton cancelButton = new JButton("Cancel");
        dialog = new JDialog(mainFrame, "Export Progress", false);
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
        var productsManager = Managers.getManager(ProductsManager.class);
        try {
            progressBar.setValue(0);
            progressBar.setMaximum(1);
            progressBar.setStringPainted(true);

            progressBar.setString("Getting products...");
            var data = productsManager.getCSVData();
            progressBar.setValue(1);

            progressBar.setValue(0);
            progressBar.setString("Exporting...");
            var csvFile = new CSVFile(file);
            csvFile.setData(data);
            csvFile.save();

            progressBar.setMaximum(1);
            progressBar.setString("Done!");
            dialog.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return 1;
        }

        return 0;
    }

}
