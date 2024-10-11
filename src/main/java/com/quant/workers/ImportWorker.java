package com.quant.workers;

import com.quant.MainWindow;
import com.quant.cons.ProductsImport;
import com.quant.exceptions.InvalidFileException;
import com.quant.exceptions.UnsupportedFileTypeException;
import com.quant.utils.CsvUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ImportWorker extends SwingWorker<Integer, Integer> {

    private final MainWindow mainWindow;
    private final List<File> files;

    private final JDialog dialog;
    private final JProgressBar progressBar = new JProgressBar();

    public ImportWorker(MainWindow mainWindow, List<File> files) {
        this.mainWindow = mainWindow;
        this.files = files;

        progressBar.setString("Waiting...");
        progressBar.setStringPainted(true);

        mainWindow.setEnabled(false);

        JButton cancelButton = new JButton("Cancel");
        dialog = new JDialog(mainWindow, "Import Progress", true);
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
        try {
            progressBar.setValue(0);
            var productsImport = new ProductsImport(files);
            productsImport.setProgressBar(progressBar);

            progressBar.setMaximum(100);
            progressBar.setString("Loading lines... (0/"+ productsImport.getLinesAmount() +")");

            CsvUtils.loadProducts(productsImport);

            productsImport.finishImport(mainWindow);
            dialog.dispose();
        } catch (UnsupportedFileTypeException e) {
            JOptionPane.showMessageDialog(mainWindow, "Unsupported file type: "+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
        } catch (InvalidFileException e) {
            JOptionPane.showMessageDialog(mainWindow, "Invalid file: "+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
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
