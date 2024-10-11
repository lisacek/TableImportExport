package com.quant.workers;

import com.quant.MainWindow;
import com.quant.cons.ProductsImport;
import com.quant.exceptions.InvalidFileException;
import com.quant.exceptions.UnsupportedFileTypeException;
import com.quant.utils.CsvUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

public class CSVImportWorker extends SwingWorker<Integer, Integer> {

    private final MainWindow mainWindow;
    private final List<File> files;

    private final JDialog dialog;
    private final JProgressBar progressBar = new JProgressBar();

    public CSVImportWorker(MainWindow mainWindow, List<File> files) {
        this.mainWindow = mainWindow;
        this.files = files;

        progressBar.setString("Waiting...");
        progressBar.setStringPainted(true);

        JButton cancelButton = new JButton("Cancel");
        dialog = new JDialog(mainWindow, "Import Progress", false);
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
        try {
            progressBar.setValue(0);
            var productsImport = new ProductsImport(files);
            productsImport.setProgressBar(progressBar);

            progressBar.setMaximum(100);
            progressBar.setString("Loading lines... (0/"+ productsImport.getLinesAmount() +")");
            dialog.pack();

            CsvUtils.loadProducts(productsImport);

            productsImport.finishImport(mainWindow);
            dialog.dispose();
        } catch (UnsupportedFileTypeException | InvalidFileException e) {
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return 1;
        }

        return 0;
    }


}
