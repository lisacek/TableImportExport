package com.quant.components.bottombar;

import com.quant.MainWindow;
import com.quant.components.Component;

import javax.swing.*;
import java.awt.*;

public class BottomBar implements Component {

    private JPanel panel;

    private JProgressBar progressBar;
    private JLabel totalProducts;


    @Override
    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        panel.add(progressBar, BorderLayout.SOUTH);

        totalProducts = new JLabel();
        totalProducts.setText("Total Products: 0");
        panel.add(totalProducts, BorderLayout.WEST);
    }

    @Override
    public JComponent render(MainWindow mainWindow) {
        return panel;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts.setText("Total Products: " + totalProducts);
    }

}
