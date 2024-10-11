package com.quant.components.topbar;

import com.quant.components.Component;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.impl.ExportButton;
import com.quant.components.topbar.impl.ImportButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TopBar implements Component {

    private JPanel panel;
    private final List<TopBarAction> actions = new ArrayList<>();

    @Override
    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        actions.add(new ImportButton());
        actions.add(new ExportButton());
    }

    @Override
    public JPanel render(JFrame frame) {
        actions.forEach(action -> {
            var annotation = action.getClass().getAnnotation(TopButton.class);
            if(annotation == null) {
                throw new RuntimeException("TopBarAction must be annotated with @TopButton");
            }

            JButton button = new JButton(annotation.text());
            button.addActionListener(e -> action.actionPerformed(frame));
            panel.add(button);
        });

        return panel;
    }

}
