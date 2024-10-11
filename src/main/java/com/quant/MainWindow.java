package com.quant;

import com.quant.components.table.Table;
import com.quant.components.topbar.TopBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

    public static void main(String[] args) {
        new MainWindow().setVisible(true);
    }

    public MainWindow() {
        setTitle("Table Import / Export");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        var bar = new TopBar();
        bar.init();
        add(bar.render(this), BorderLayout.NORTH);

        var table = new Table();
        table.init();
        add(new JScrollPane(table.render(this)), BorderLayout.CENTER);
    }
}
