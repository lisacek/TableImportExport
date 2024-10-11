package com.quant;

import com.quant.components.bottombar.BottomBar;
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

    private final Table table;
    private final BottomBar bottomBar;

    public MainWindow() {
        setTitle("Table Import / Export");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        var bar = new TopBar();
        bar.init();
        add(bar.render(this), BorderLayout.NORTH);

        bottomBar = new BottomBar();
        bottomBar.init();
        add(bottomBar.render(this), BorderLayout.SOUTH);

        table = new Table();
        table.init();
        add(new JScrollPane(table.render(this)), BorderLayout.CENTER);
    }

    public Table getTable() {
        return table;
    }

    public BottomBar getBottomBar() {
        return bottomBar;
    }
}
