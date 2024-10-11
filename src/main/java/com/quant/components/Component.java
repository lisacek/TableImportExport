package com.quant.components;

import com.quant.annotations.ComponentInfo;

import javax.swing.*;

public interface Component {

    void init();

    JComponent getJComponent();

    default void renderOnFrame(JFrame frame) {
        var componentInfo = getClass().getAnnotation(ComponentInfo.class);
        if (componentInfo == null) {
            return;
        }

        var componentRender = componentInfo.render();
        var componentBorderLayout = componentInfo.borderLayout();

        switch (componentRender) {
            case BASIC -> frame.add(getJComponent(), componentBorderLayout);
            case SCROLL -> frame.add(new JScrollPane(getJComponent()), componentBorderLayout);
        }
    }

}
