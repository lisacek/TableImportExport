package com.quant.components.bottombar;

import com.quant.MainWindow;
import com.quant.components.Component;

import javax.swing.*;
import java.awt.*;

public class BottomBar implements Component {

    private JPanel panel;

    private JLabel totalProducts;


    @Override
    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        totalProducts = new JLabel();
        totalProducts.setText("Total Products: 0");
        panel.add(totalProducts, BorderLayout.WEST);
    }

    @Override
    public JComponent render(MainWindow mainWindow) {
        return panel;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts.setText("Total Products: " + totalProducts);
    }

}
