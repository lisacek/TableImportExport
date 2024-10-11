package com.quant.managers.impl;

import com.quant.columns.Column;
import com.quant.managers.Manager;
import com.quant.utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ColumnsManager implements Manager {

    private final List<Column<?, ?>> columns = new ArrayList<>();

    @Override
    public void init() {
        var columnsPackage = Column.class.getPackage().getName() + ".impl";
        var classes = ReflectionUtils.getClassesInPackage(columnsPackage);

        for (var clazz : classes) {
            try {
                columns.add((Column<?, ?>) clazz.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    public Vector<?> getColumnsNames() {
        var names = new Vector<String>();
        columns.forEach(column -> names.add(column.getName()));
        return names;
    }

    public List<Column<?, ?>> getColumns() {
        return columns;
    }

    public <T extends Column<?, ?>> T getColumn(Class<T> clazz) {
        return columns.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
    }

    public Column<?, ?> getByColumnName(String name) {
        return columns.stream().filter(column -> column.getName().equals(name)).findFirst().orElse(null);
    }

}
