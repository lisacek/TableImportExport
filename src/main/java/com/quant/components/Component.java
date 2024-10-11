package com.quant.components;

import com.quant.MainWindow;

import javax.swing.*;

public interface Component {

    void init();

    JComponent render(MainWindow mainWindow);

}
