package com.quant.components.bottombar;

import com.quant.annotations.ComponentInfo;
import com.quant.components.Component;

import javax.swing.*;
import java.awt.*;

@ComponentInfo(
        borderLayout = BorderLayout.SOUTH
)
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
    public JComponent getJComponent() {
        return panel;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts.setText("Total Products: " + totalProducts);
    }

}
