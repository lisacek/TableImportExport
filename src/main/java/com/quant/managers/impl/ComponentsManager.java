package com.quant.managers.impl;

import com.quant.components.Component;
import com.quant.components.bottombar.BottomBar;
import com.quant.components.table.ProductsTable;
import com.quant.components.topbar.TopBar;
import com.quant.managers.Manager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ComponentsManager implements Manager {

    private final List<Component> components = new ArrayList<>();

    @Override
    public void init() {
        components.add(new TopBar());
        components.add(new ProductsTable());
        components.add(new BottomBar());
    }

    @Override
    public void start() {
        components.forEach(Component::init);
    }

    @Override
    public void stop() {

    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    public <T extends Component> T getComponent(Class<T> clazz) {
        return components.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
    }

    public void render(JFrame frame) {
        var components = getComponents();
        for (var component : components) {
            component.renderOnFrame(frame);
        }
    }

}
