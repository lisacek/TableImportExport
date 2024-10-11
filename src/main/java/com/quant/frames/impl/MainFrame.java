package com.quant.frames.impl;

import com.quant.frames.Frame;
import com.quant.managers.Managers;
import com.quant.managers.impl.ComponentsManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements Frame {

    @Override
    public void create() {
        setTitle("Table Import / Export");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        var manager = Managers.getManager(ComponentsManager.class);
        manager.render(this);
    }

}
