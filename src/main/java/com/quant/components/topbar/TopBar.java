package com.quant.components.topbar;

import com.quant.MainWindow;
import com.quant.components.Component;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.impl.CSVExportButton;
import com.quant.components.topbar.impl.CSVImportButton;
import com.quant.components.topbar.impl.ExcelExportButton;

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

        actions.add(new CSVImportButton());
        actions.add(new CSVExportButton());
        actions.add(new ExcelExportButton());
    }

    @Override
    public JPanel render(MainWindow mainWindow) {
        actions.forEach(action -> {
            var annotation = action.getClass().getAnnotation(TopButton.class);
            if(annotation == null) {
                throw new RuntimeException("TopBarAction must be annotated with @TopButton");
            }

            JButton button = new JButton(annotation.text());
            button.addActionListener(e -> action.actionPerformed(mainWindow));
            panel.add(button);
        });

        return panel;
    }

}
