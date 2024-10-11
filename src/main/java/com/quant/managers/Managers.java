package com.quant.managers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Managers {

    private static final List<Manager> managers = new ArrayList<>();

    public static void add(Class<? extends Manager> clazz) {
        try {
            managers.add(clazz.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void add(Manager manager) {
        managers.add(manager);
    }

    public static void init() {
        managers.forEach(Manager::init);
    }

    public static void start() {
        managers.forEach(Manager::start);
    }

    public static void stop() {
        managers.forEach(Manager::stop);
    }

    public static <T extends Manager> T getManager(Class<T> clazz) {
        return managers.stream().filter(clazz::isInstance).map(clazz::cast).findFirst().orElse(null);
    }

}
