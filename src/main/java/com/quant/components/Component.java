package com.quant.components;

import javax.swing.*;

public interface Component {

    void init();

    JComponent render(JFrame frame);

}
