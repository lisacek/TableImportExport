package com.quant.components.topbar;

import com.quant.annotations.ComponentInfo;
import com.quant.components.Component;
import com.quant.components.topbar.actions.TopBarAction;
import com.quant.components.topbar.annotations.TopButton;
import com.quant.components.topbar.actions.impl.CSVExportButton;
import com.quant.components.topbar.actions.impl.CSVImportButton;
import com.quant.components.topbar.actions.impl.ExcelExportButton;
import com.quant.utils.ReflectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ComponentInfo(
        borderLayout = BorderLayout.NORTH
)
public class TopBar implements Component {

    private JPanel panel;
    private final List<TopBarAction> actions = new ArrayList<>();

    @Override
    public void init() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        var actionsPackage = TopBarAction.class.getPackage().getName();
        ReflectionUtils.getClassesInPackage(actionsPackage).forEach(clazz -> {
            if(clazz.isAnnotationPresent(TopButton.class)) {
                try {
                    actions.add((TopBarAction) clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public JPanel getJComponent() {
        actions.stream().sorted((a1, a2) -> {
            var annotation1 = a1.getClass().getAnnotation(TopButton.class);
            var annotation2 = a2.getClass().getAnnotation(TopButton.class);

            return Integer.compare(annotation1.order(), annotation2.order());
        }).forEach(action -> {
            var annotation = action.getClass().getAnnotation(TopButton.class);
            if(annotation == null) {
                throw new RuntimeException("TopBarAction must be annotated with @TopButton");
            }

            JButton button = new JButton(annotation.text());
            button.addActionListener(e -> action.actionPerformed());
            panel.add(button);
        });

        return panel;
    }

}
